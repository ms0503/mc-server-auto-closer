plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven {
        name = "Architectury"
        url = uri("https://maven.architectury.dev")
    }
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net")
    }
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.architectury.loom)
    implementation(libs.kotlin.plugin)
    implementation(libs.minotaur)
    implementation(libs.shadow.plugin)
}

gradlePlugin {
    plugins {
        create("common-parts") {
            id = "common-parts"
            implementationClass = "dev.ms0503.mcserverautocloser.CommonPartsPlugin"
        }
        create("platform-common-parts") {
            id = "platform-common-parts"
            implementationClass = "dev.ms0503.mcserverautocloser.PlatformCommonPartsPlugin"
        }
    }
}
