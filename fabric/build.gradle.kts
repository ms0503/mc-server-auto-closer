import dev.ms0503.mcserverautocloser.Constants.MOD_ID
import dev.ms0503.mcserverautocloser.Constants.MOD_NAME
import dev.ms0503.mcserverautocloser.Constants.VERSION

plugins {
    `common-parts`
    `platform-common-parts`
}

version = "${VERSION}+${libs.versions.minecraft.get()}"

architectury {
    fabric()
    platformSetupLoomIde()
}

val common: Configuration by configurations.getting
val developmentFabric: Configuration by configurations.getting
val shadowBundle: Configuration by configurations.getting

configurations {
    compileClasspath.configure {
        extendsFrom(common.get())
    }
    developmentFabric.extendsFrom(common.get())
    runtimeClasspath.configure {
        extendsFrom(common.get())
    }
}

repositories {
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    minecraft(libs.minecraft)
    modImplementation(libs.architectury.fabric)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.kotlin.fabric)
    modImplementation(libs.modmenu)
    shadowBundle(project(configuration = "transformProductionFabric", path = ":common"))
}

tasks.processResources {
    val replaceProperties = mapOf(
        "mod_id" to MOD_ID,
        "mod_name" to MOD_NAME,
        "version" to VERSION
    )
    filesMatching("fabric.mod.json") {
        expand(replaceProperties)
    }
    inputs.properties(replaceProperties)
}
