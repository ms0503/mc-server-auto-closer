package dev.ms0503.mcserverautocloser

import dev.ms0503.mcserverautocloser.Constants.ARCHIVES_NAME
import dev.ms0503.mcserverautocloser.Constants.MAVEN_GROUP
import dev.ms0503.mcserverautocloser.Constants.VERSION
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapTaskConfiguration.REMAP_JAR_TASK_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonPartsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs: VersionCatalog = project
            .extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")
        with(project) {
            plugins.apply {
                apply("base")
                apply("idea")
                apply("java")
                apply("org.jetbrains.kotlin.jvm")
                apply("architectury-plugin")
                apply("dev.architectury.loom")
            }
            group = MAVEN_GROUP
            version = "${VERSION}+${libs.findVersion("minecraft").get()}"
            configure<BasePluginExtension> {
                archivesName.set("${ARCHIVES_NAME}-${name}")
            }
            configure<JavaPluginExtension> {
                withJavadocJar()
                withSourcesJar()
            }
            configure<LoomGradleExtensionAPI> {
                silentMojangMappingsLicense()
            }
            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }
            tasks.withType<Javadoc>().configureEach {
                options.encoding = "UTF-8"
            }
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
            tasks.named<Jar>("jar") {
                archiveClassifier.set("dev")
            }
            tasks.named<RemapJarTask>(REMAP_JAR_TASK_NAME) {
                archiveClassifier.set(null as String?)
            }
            artifacts {
                add("archives", tasks.named(REMAP_JAR_TASK_NAME))
            }
        }
    }
}
