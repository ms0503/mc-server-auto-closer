package dev.ms0503.mcserverautocloser

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.SHADOW_JAR_TASK_NAME
import com.gtnewhorizons.retrofuturagradle.mcp.ReobfuscatedJar
import com.modrinth.minotaur.ModrinthExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project

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
            dependencies {
                common(project(":common"))
            }
            tasks.named<ReobfuscatedJar>("reobfJar") {
                dependsOn(tasks.named(SHADOW_JAR_TASK_NAME))
                inputJar.set(tasks.named<ShadowJar>("shadowJar").get().archiveFile)
            }
            tasks.named<Jar>("jar") {
                finalizedBy(tasks.named(SHADOW_JAR_TASK_NAME))
            }
            tasks.named<ShadowJar>(SHADOW_JAR_TASK_NAME) {
                archiveClassifier.set("")
                configurations.set(listOf(shadowBundle.get()))
                from(project(":common").tasks.named<Jar>("jar").map {
                    zipTree(it.archiveFile.get().asFile)
                })
                finalizedBy(tasks.named("reobfJar"))
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
                uploadFile.set(tasks.named("reobfJar"))
                versionNumber.set(version.toString())
                versionType.set("release")
            }
        }
    }
}
