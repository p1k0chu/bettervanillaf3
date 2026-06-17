pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version providers.gradleProperty("plugins.loom")
        id("net.fabricmc.fabric-loom-remap") version providers.gradleProperty("plugins.loom")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
    id("dev.kikugie.stonecutter") version providers.gradleProperty("plugins.stonecutter")
}

stonecutter {
    create(rootProject) {
        versions("1.21.10", "1.21.11", "26.1.2")
        vcsVersion = "26.1.2"
    }
}

rootProject.name = "bettervanillaf3"
