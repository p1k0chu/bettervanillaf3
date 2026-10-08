plugins {
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("net.fabricmc.fabric-loom")
}

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
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    implementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    implementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "fabric_loader" to project.property("fmj.fabric_loader"),
        "minecraft" to project.property("fmj.minecraft"),
        "yacl" to project.property("fmj.yacl_version"),
        "modmenu" to project.property("fmj.modmenu_version")
    )
    inputs.properties(props)
    filesMatching("fabric.mod.json") { expand(props) }

    val mixinJava = "JAVA_${project.property("java_version")}"
    inputs.property("java_version", project.property("java_version"))
    filesMatching("*.mixins.json") { expand("java" to mixinJava) }
}

loom {
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
    file = tasks.jar.flatMap { it.archiveFile }
    displayName = property("mod_version") as String
    version = property("mod_version") as String

    val changebuilder = StringBuilder()
    file("CHANGELOG.md")?.let {
        if (it.exists()) {
            changebuilder.append(it.readText())
        }
    }
    changelog = if (changebuilder.isEmpty()) "* nothing" else changebuilder.toString()

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
