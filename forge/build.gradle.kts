
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.SHADOW_JAR_TASK_NAME
import com.gtnewhorizons.retrofuturagradle.minecraft.RunMinecraftTask
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
    mcVersion.set(libs.versions.minecraft.get())
    mcpMappingChannel.set("stable")
    mcpMappingVersion.set("12")
}

repositories {
    maven {
        name = "mavenpattern"
        url = uri("https://mvn.falsepattern.com/releases")
    }
}

dependencies {
    runtimeOnly(libs.kotlin.forge)
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
    token.set(env.MODRINTH_TOKEN.value)
    versionName.set("v${version} for Forge")
}

tasks.named("compileMcLauncherKotlin") {
    dependsOn("createMcLauncherFiles")
}

tasks.named("compilePatchedMcKotlin") {
    dependsOn("decompressDecompiledSources")
}

tasks.named("compileInjectedTagsKotlin") {
    dependsOn("injectTags")
}

tasks.named<RunMinecraftTask>("runServer") {
    dependsOn(SHADOW_JAR_TASK_NAME)
    extraJvmArgs.add("-Djava.awt.headless=true")
}
