import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

val obfuscated = sc.current.parsed < "26.1"
plugins.apply(if(obfuscated) "net.fabricmc.fabric-loom-remap" else "net.fabricmc.fabric-loom")
val loom = the<LoomGradleExtensionAPI>()
val modImplementation = if(obfuscated) configurations.named("modImplementation") else configurations.implementation
val modJar = if(obfuscated) tasks.named<Zip>("remapJar") else tasks.named<Zip>("jar")

version = "${property("mod_version")}+${sc.current.version}"

base {
    archivesName = rootProject.name
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
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")

    if (obfuscated) {
        "mappings"(loom.officialMojangMappings())
    }
}

tasks.processResources {
    val props = mapOf(
        "version" to project.property("mod_version"),
        "fabric_loader" to project.property("fmj.fabric_loader"),
        "minecraft" to project.property("fmj.minecraft"),
        "yacl" to project.property("fmj.yacl_version"),
        "modmenu" to project.property("fmj.modmenu_version")
    )
    props.forEach { k, v -> inputs.property(k, v) }
    filesMatching("fabric.mod.json") { expand(props) }

    val mixinJava = "JAVA_${project.property("java_version")}"
    inputs.property("java_version", project.property("java_version"))
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
        create(rootProject.name) {
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
        requires {
            slug = "yacl"
        }
        optional {
            slug = "modmenu"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
