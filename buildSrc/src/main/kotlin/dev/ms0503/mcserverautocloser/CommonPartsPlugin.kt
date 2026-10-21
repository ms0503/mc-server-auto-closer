package dev.ms0503.mcserverautocloser

import dev.ms0503.mcserverautocloser.Constants.ARCHIVES_NAME
import dev.ms0503.mcserverautocloser.Constants.MAVEN_GROUP
import dev.ms0503.mcserverautocloser.Constants.VERSION
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.repositories
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
                apply("net.minecraftforge.gradle")
                apply("org.jetbrains.kotlin.jvm")
            }
            group = MAVEN_GROUP
            version = "${VERSION}+${libs.findVersion("minecraft").get()}"
            val minecraft = configurations.named("minecraft")
            repositories {
                maven {
                    name = "Forge"
                    url = uri("https://maven.minecraftforge.net")
                }
            }
            dependencies {
                minecraft(libs.findLibrary("forge").get())
            }
            configure<BasePluginExtension> {
                archivesName.set("${ARCHIVES_NAME}-${name}")
            }
            configure<JavaPluginExtension> {
                toolchain.languageVersion.set(JavaLanguageVersion.of(8))
                withJavadocJar()
                withSourcesJar()
            }
            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
                sourceCompatibility = "1.8"
                targetCompatibility = "1.8"
            }
            tasks.withType<Javadoc>().configureEach {
                options.encoding = "UTF-8"
            }
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
            }
            tasks.named<Jar>("jar") {
                archiveClassifier.set("dev")
            }
        }
    }
}
