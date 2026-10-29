package dev.ms0503.mcserverautocloser

import co.uzzu.dotenv.gradle.DotEnvRoot
import dev.ms0503.mcserverautocloser.Constants.ARCHIVES_NAME
import dev.ms0503.mcserverautocloser.Constants.DESCRIPTION
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
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonPartsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val env: DotEnvRoot = project.extensions.getByType<DotEnvRoot>()
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
                apply("maven-publish")
                apply("signing")
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
                sourceCompatibility = "16"
                targetCompatibility = "16"
            }
            tasks.withType<Javadoc>().configureEach {
                options.encoding = "UTF-8"
            }
            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions.jvmTarget = "16"
            }
            tasks.named<Jar>("jar") {
                archiveClassifier.set("dev")
            }
            tasks.named<RemapJarTask>(REMAP_JAR_TASK_NAME) {
                archiveClassifier.set(null as String?)
            }
            tasks.named("assemble") {
                dependsOn(tasks.named(REMAP_JAR_TASK_NAME))
            }
            configure<PublishingExtension> {
                publications {
                    create("maven", MavenPublication::class.java) {
                        artifact(tasks.named("jar"))
                        artifactId = extensions.getByType<BasePluginExtension>().archivesName.get()
                        from(components.named("java").get())
                        pom {
                            description.set(DESCRIPTION)
                            developers {
                                developer {
                                    email.set("admin@ms0503.dev")
                                    id.set("ms0503")
                                    name.set("Sora Tonami")
                                }
                            }
                            licenses {
                                license {
                                    distribution.set("repo")
                                    name.set("MIT License")
                                    url.set("https://github.com/ms0503/mc-server-auto-closer/blob/HEAD/LICENSE.md")
                                }
                            }
                            name.set(extensions.getByType<BasePluginExtension>().archivesName.get())
                            scm {
                                connection.set("scm:git:https://github.com/ms0503/mc-server-auto-closer.git")
                                developerConnection.set("scm:git:https://github.com/ms0503/mc-server-auto-closer.git")
                                url.set("https://github.com/ms0503/mc-server-auto-closer")
                            }
                            url.set("https://github.com/ms0503/mc-server-auto-closer")
                        }
                    }
                }
                repositories {
                    maven {
                        name = "build"
                        url = rootProject.layout.buildDirectory.dir("repo").get().asFile.toURI()
                    }
                }
            }
            configure<SigningExtension> {
                sign(extensions.getByType<PublishingExtension>().publications.named<MavenPublication>("maven").get())
                useInMemoryPgpKeys(
                    env.fetchOrNull("GPG_KEY_ID"),
                    env.fetchOrNull("GPG_SECRET_KEY"),
                    env.fetchOrNull("GPG_PASSPHRASE")
                )
            }
        }
    }
}
