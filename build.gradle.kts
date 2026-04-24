import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("me.modmuss50.mod-publish-plugin") version "1.0.+"
}

val obfuscated = sc.current.parsed < "26.1"
plugins.apply(if(obfuscated) "net.fabricmc.fabric-loom-remap" else "net.fabricmc.fabric-loom")
val loom = the<LoomGradleExtensionAPI>()
val modImplementation = if(obfuscated) configurations.named("modImplementation") else configurations.implementation
val modJar = if(obfuscated) tasks.named<Zip>("remapJar") else tasks.named<Zip>("jar")

version = "${property("mod_version")}+${sc.current.version}"

base {
    archivesName = "nostalgic_f3"
}

repositories {
    mavenCentral()
    // YACL
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    // Mod Menu
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:${sc.current.version}")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")

    if (obfuscated) {
        "mappings"(loom.officialMojangMappings())
    }
}

tasks.processResources {
    inputs.property("mod_version", project.property("mod_version"))
    inputs.property("minecraft_range", project.property("minecraft_range"))
    inputs.property("loader_version", project.property("loader_version"))
    inputs.property("java_version", project.property("java_version"))
    inputs.property("yacl_version", project.property("yacl_version"))
    inputs.property("modmenu_version", project.property("modmenu_version"))

    filesMatching("fabric.mod.json") {
        expand(
            "version" to inputs.properties.getValue("mod_version"),
            "loader_version" to inputs.properties.getValue("loader_version"),
            "minecraft_range" to inputs.properties.getValue("minecraft_range"),
            "yacl_version" to inputs.properties.getValue("yacl_version"),
            "modmenu_version" to inputs.properties.getValue("modmenu_version")
        )
    }

    val mixinJava = "JAVA_${inputs.properties.getValue("java_version")}"
    filesMatching("*.mixins.json") { expand("java" to mixinJava) }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(modJar.flatMap { it.archiveFile } /*, remapSourcesJar.map { it.archiveFile }*/)
    into(rootProject.layout.buildDirectory.file("libs"))
    dependsOn("build")
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
    withSourcesJar()
    val j = JavaVersion.valueOf("VERSION_${project.property("java_version")}")
    targetCompatibility = j
    sourceCompatibility = j
}

publishMods {
    file = modJar.flatMap { it.archiveFile }
    displayName = "${property("mod_version")} for ${sc.current.version}"
    version = property("mod_version") as String
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    dryRun = providers.environmentVariable("MODRINTH_TOKEN").getOrNull() == null

    modrinth {
        projectId = "m2IluZHV"
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(property("minecraft_targets_publishing").toString().split(' '))
    }
}

tasks.test {
    useJUnitPlatform()
}
