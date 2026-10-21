pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net")
        }
        maven {
            mavenContent {
                includeGroup("com.gtnewhorizons")
                includeGroupByRegex("com\\.gtnewhorizons\\..+")
            }
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
        }
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when(requested.id.id) {
                "com.gtnewhorizons.retrofuturagradle" -> useModule("com.gtnewhorizons:retrofuturagradle:${requested.version}")
            }
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "mc-server-auto-closer"

include("common")
include("forge")
