import dev.ms0503.mcserverautocloser.Constants.MAVEN_GROUP
import dev.ms0503.mcserverautocloser.Constants.VERSION
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-library`
    alias(libs.plugins.architectury)
    alias(libs.plugins.dotenv)
    base
    id(libs.plugins.architectury.loom.get().pluginId) apply false // common-partsがclasspathに追加しているためID参照のみ
    idea
    java
    kotlin("jvm")
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

tasks.register("modrinth") {
    dependsOn(
        project(":fabric").tasks["modrinth"],
        project(":forge").tasks["modrinth"],
        project(":neoforge").tasks["modrinth"]
    )
}
