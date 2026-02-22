package dev.ms0503.mcserverautocloser

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.SHADOW_JAR_TASK_NAME
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapTaskConfiguration.REMAP_JAR_TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories

class PlatformCommonPartsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.apply {
                apply("com.gradleup.shadow")
            }
            val common = configurations.register("common") {
                isCanBeConsumed = false
                isCanBeResolved = true
            }
            val shadowBundle = configurations.register("shadowBundle") {
                isCanBeConsumed = false
                isCanBeResolved = true
            }
            repositories {
                maven {
                    name = "shedaniel"
                    url = uri("https://maven.shedaniel.me")
                }
            }
            dependencies {
                common(project(configuration = "namedElements", path = ":common")) {
                    isTransitive = false
                }
            }
            tasks.named<ShadowJar>(SHADOW_JAR_TASK_NAME) {
                archiveClassifier.set("dev-shadow")
                configurations.set(listOf(shadowBundle.get()))
            }
            tasks.named<RemapJarTask>(REMAP_JAR_TASK_NAME) {
                inputFile.set(tasks.named<ShadowJar>(SHADOW_JAR_TASK_NAME).get().archiveFile)
            }
        }
    }
}
