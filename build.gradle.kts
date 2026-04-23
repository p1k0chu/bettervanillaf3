import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("com.modrinth.minotaur") version "2.+"
}

val obfuscated = sc.current.parsed < "26.1"
plugins.apply(if(obfuscated) "net.fabricmc.fabric-loom-remap" else "net.fabricmc.fabric-loom")
val loom = the<LoomGradleExtensionAPI>()
val modImplementation = if(obfuscated) configurations.named("modImplementation") else configurations.implementation

version = "${property("mod_version")}+${sc.current.version}"

repositories {
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    if (obfuscated) {
        "mappings"(loom.officialMojangMappings())
    }
}

tasks.processResources {
    inputs.property("mod_version", project.property("mod_version"))
    inputs.property("minecraft_range", project.property("minecraft_range"))
    inputs.property("loader_version", project.property("loader_version"))
    inputs.property("java_version", project.property("java_version"))
    
    filesMatching("fabric.mod.json") {
        expand(
            "version" to inputs.properties.getValue("mod_version"),
            "loader_version" to inputs.properties.getValue("loader_version"),
            "minecraft_version" to inputs.properties.getValue("minecraft_range")
        )
    }

    val mixinJava = "JAVA_${inputs.properties.getValue("java_version")}"
    filesMatching("*.mixins.json") { expand("java" to mixinJava) }
}

extensions.configure<LoomGradleExtensionAPI>() {
    splitEnvironmentSourceSets()

    mods {
        create("nostalgic-f3") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
    val j = JavaVersion.valueOf("VERSION_${project.property("java_version")}")
    targetCompatibility = j
    sourceCompatibility = j
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "m2IluZHV"
    syncBodyFrom = rootProject.file("README.md").readText()

    versionNumber = "$version"
    versionType = "release" // `release`, `beta` or `alpha`
    gameVersions.add(sc.current.version)

    uploadFile.set(tasks.jar)
    loaders.add("fabric")
}

tasks.test {
    useJUnitPlatform()
}
