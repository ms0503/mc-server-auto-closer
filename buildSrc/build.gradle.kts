import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

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

dependencies {
    implementation(gradleApi())
    implementation(libs.forge.gradle)
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

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}
