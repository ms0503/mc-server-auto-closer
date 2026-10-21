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

evaluationDependsOn(":common")

val common: Configuration by configurations.getting
val shadowBundle: Configuration by configurations.getting

configurations {
    compileClasspath.configure {
        extendsFrom(common.get())
    }
    runtimeClasspath.configure {
        extendsFrom(common.get())
    }
}

minecraft {
    mappings("stable", "39-1.12")
    runs {
        create("server") {
            mods {
                create(MOD_ID) {
                    source(sourceSets["main"])
                }
            }
            workingDirectory(project.file("run"))
        }
    }
}

repositories {
    // // CleanroomMC's Maven repository only has 1.x
    // maven {
    //     name = "CleanroomMC"
    //     url = uri("https://maven.cleanroommc.com")
    // }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        forRepositories(fg.repository)
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    runtimeOnly(libs.kotlin.forge)
    shadowBundle(project(":common"))
}

tasks.processResources {
    val replaceProperties = mapOf(
        "description" to DESCRIPTION.replace("\n", "\\n"),
        "mod_id" to MOD_ID,
        "mod_name" to MOD_NAME,
        "version" to VERSION
    )
    filesMatching("mcmod.info") {
        expand(replaceProperties)
    }
    inputs.properties(replaceProperties)
}

modrinth {
    dependencies.addAll(
        ModDependency("1mPcAmuy", DependencyType.REQUIRED) // Forgelin-Continuous
    )
    loaders.add("forge")
    token = env.MODRINTH_TOKEN.value
    versionName = "v${version} for Forge"
}
