import dev.ms0503.mcserverautocloser.Constants.ARCHIVES_NAME
import dev.ms0503.mcserverautocloser.Constants.MAVEN_GROUP
import dev.ms0503.mcserverautocloser.Constants.MOD_ID
import dev.ms0503.mcserverautocloser.Constants.VERSION
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

buildscript {
    dependencies {
        classpath(libs.aggregate.javadocs)
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    `java-library`
    alias(libs.plugins.architectury)
    base
    id(libs.plugins.architectury.loom.get().pluginId) apply false // common-partsがclasspathに追加しているためID参照のみ
    idea
    java
    kotlin("jvm")
}

apply(plugin = "nebula-aggregate-javadocs")

base {
    archivesName = ARCHIVES_NAME
}

group = MAVEN_GROUP
version = "${VERSION}+${libs.versions.minecraft.get()}"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
}

architectury {
    minecraft = libs.versions.minecraft.get()
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

tasks.jar {
    val commonJar = project(":common").tasks["remapJar"] as RemapJarTask
    val fabricJar = project(":fabric").tasks["remapJar"] as RemapJarTask
    val forgeJar = project(":forge").tasks["remapJar"] as RemapJarTask
    dependsOn(commonJar, fabricJar, forgeJar)
    from(commonJar.archiveFile.map {
        zipTree(it)
    })
    from(fabricJar.archiveFile.map {
        zipTree(it)
    })
    from(forgeJar.archiveFile.map {
        zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Timestamp"] =
            OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Vendor"] = "Sora Tonami"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Specification-Title"] = MOD_ID
        attributes["Specification-Vendor"] = "Sora Tonami"
        attributes["Specification-Version"] = "1"
    }
}

tasks.named<Jar>("sourcesJar") {
    archiveClassifier = "sources"
    dependsOn(project(":common").tasks["expandInfo"])
    from(project(":common").sourceSets.main.get().allSource)
    from(project(":fabric").sourceSets.main.get().allSource)
    from(project(":forge").sourceSets.main.get().allSource)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Jar>("javadocJar") {
    dependsOn(tasks["aggregateJavadocs"])
}
