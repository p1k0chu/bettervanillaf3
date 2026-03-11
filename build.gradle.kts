plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
}

val minecraftVersion = project.property("minecraft_version") as String
val loaderVersion = project.property("loader_version") as String
val modId = project.property("mod_id") as String

repositories {
    mavenCentral()
}

base {
    archivesName = modId
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(
            "version" to version,
            "loader_version" to loaderVersion,
            "minecraft_version" to minecraftVersion,
        )
    }
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
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

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_$modId" }
    }
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "m2IluZHV"
    syncBodyFrom = rootProject.file("README.md").readText()

    versionNumber = "$version-mc$minecraftVersion"
    versionType = "release" // `release`, `beta` or `alpha`
    gameVersions.add(minecraftVersion)

    uploadFile.set(tasks.remapJar)
    loaders.add("fabric")
}

tasks.test {
    useJUnitPlatform()
}
