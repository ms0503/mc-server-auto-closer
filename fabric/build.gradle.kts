import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
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
}

dependencies {
    mappings(loom.officialMojangMappings())
    minecraft(libs.minecraft)
    modImplementation(libs.architectury.fabric)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.kotlin.fabric)
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

modrinth {
    dependencies.addAll(
        ModDependency("Ha28R6CL", DependencyType.REQUIRED), // Fabric Language Kotlin
        ModDependency("P7dR8mSH", DependencyType.REQUIRED), // Fabric API
        ModDependency("lhGA9TYQ", DependencyType.REQUIRED), // Architectury API
        ModDependency("mOgUt4GM", DependencyType.OPTIONAL) // Modmenu (optional)
    )
    loaders.add("fabric")
    versionName = "v${version} for Fabric"
}
