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

architectury {
    neoForge()
    platformSetupLoomIde()
}

loom {
    neoForge {
    }
}

val common: Configuration by configurations.getting
val developmentNeoForge: Configuration by configurations.getting
val shadowBundle: Configuration by configurations.getting

configurations {
    compileClasspath.configure {
        extendsFrom(common.get())
    }
    developmentNeoForge.extendsFrom(common.get())
    runtimeClasspath.configure {
        extendsFrom(common.get())
    }
}

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge")
    }
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    minecraft(libs.minecraft)
    neoForge(libs.neoforge)
    modImplementation(libs.architectury.neoforge)
    modRuntimeOnly(libs.kotlin.neoforge)
    shadowBundle(project(configuration = "transformProductionNeoForge", path = ":common"))
}

tasks.processResources {
    val replaceProperties = mapOf(
        "description" to DESCRIPTION,
        "mod_id" to MOD_ID,
        "mod_name" to MOD_NAME,
        "version" to VERSION
    )
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
    inputs.properties(replaceProperties)
}

modrinth {
    dependencies.addAll(
        ModDependency("lhGA9TYQ", DependencyType.REQUIRED), // Architectury API
        ModDependency("ordsPcFz", DependencyType.REQUIRED) // Kotlin for Forge
    )
    loaders.add("neoforge")
    token = env.MODRINTH_TOKEN.value
    versionName = "v${version} for NeoForge"
}
