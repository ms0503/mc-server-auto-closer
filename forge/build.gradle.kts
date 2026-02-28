import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.ms0503.mcserverautocloser.Constants.DESCRIPTION
import dev.ms0503.mcserverautocloser.Constants.MOD_ID
import dev.ms0503.mcserverautocloser.Constants.MOD_NAME
import dev.ms0503.mcserverautocloser.Constants.VERSION

plugins {
    `common-parts`
    `platform-common-parts`
}

version = "${VERSION}+${libs.versions.minecraft.get()}"

architectury {
    forge()
    platformSetupLoomIde()
}

loom {
    forge {
    }
}

val common: Configuration by configurations.getting
val developmentForge: Configuration by configurations.getting
val shadowBundle: Configuration by configurations.getting

configurations {
    compileClasspath.configure {
        extendsFrom(common.get())
    }
    developmentForge.extendsFrom(common.get())
    runtimeClasspath.configure {
        extendsFrom(common.get())
    }
}

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    minecraft(libs.minecraft)
    forge(libs.forge)
    modImplementation(libs.architectury.forge)
    modImplementation(libs.kotlin.forge)
    shadowBundle(project(configuration = "transformProductionForge", path = ":common"))
}

tasks.processResources {
    val replaceProperties = mapOf(
        "description" to DESCRIPTION,
        "mod_id" to MOD_ID,
        "mod_name" to MOD_NAME,
        "version" to VERSION
    )
    filesMatching("META-INF/mods.toml") {
        expand(replaceProperties)
    }
    inputs.properties(replaceProperties)
}

modrinth {
    dependencies.addAll(
        ModDependency("lhGA9TYQ", DependencyType.REQUIRED), // Architectury API
        ModDependency("ordsPcFz", DependencyType.REQUIRED) // Kotlin for Forge
    )
    loaders.add("forge")
    versionName = "v${version} for Forge"
}
