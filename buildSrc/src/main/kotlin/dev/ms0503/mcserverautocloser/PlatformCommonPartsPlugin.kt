package dev.ms0503.mcserverautocloser

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.SHADOW_JAR_TASK_NAME
import com.modrinth.minotaur.ModrinthExtension
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapTaskConfiguration.REMAP_JAR_TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories

class PlatformCommonPartsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs: VersionCatalog = project
            .extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")
        with(project) {
            plugins.apply {
                apply("com.gradleup.shadow")
                apply("com.modrinth.minotaur")
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
            tasks.named("modrinth") {
                dependsOn(tasks.named("build"))
            }
            configure<ModrinthExtension> {
                changelog.set(rootProject.file("CHANGELOG.md").readText())
                debugMode.set(false)
                gameVersions.add(libs.findVersion("minecraft").get().toString())
                projectId.set(rootProject.properties["modrinth_project_id"] as String)
                syncBodyFrom.set(rootProject.file("README.md").readText())
                uploadFile.set(tasks.named<RemapJarTask>("remapJar"))
                versionNumber.set(version.toString())
                versionType.set("release")
            }
        }
    }
}
