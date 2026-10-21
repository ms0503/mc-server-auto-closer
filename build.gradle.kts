import dev.ms0503.mcserverautocloser.Constants.MAVEN_GROUP
import dev.ms0503.mcserverautocloser.Constants.VERSION
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-library`
    alias(libs.plugins.dotenv)
    base
    id(libs.plugins.forge.gradle.get().pluginId) apply false // common-partsがclasspathに追加しているためID参照のみ
    idea
    java
    kotlin("jvm")
}

group = MAVEN_GROUP
version = "${VERSION}+${libs.versions.minecraft.get()}"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
    project {
        languageLevel = IdeaLanguageLevel(8)
    }
}

tasks.register("modrinth") {
    dependsOn(project(":forge").tasks["modrinth"])
}
