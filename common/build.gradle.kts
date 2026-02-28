import dev.ms0503.mcserverautocloser.Constants.ENABLED_PLATFORMS
import dev.ms0503.mcserverautocloser.Constants.MOD_ID
import dev.ms0503.mcserverautocloser.Constants.MOD_NAME
import dev.ms0503.mcserverautocloser.Constants.VERSION
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `common-parts`
}

version = "${VERSION}+${libs.versions.minecraft.get()}"

architectury {
    common(ENABLED_PLATFORMS.split(","))
}

loom {
}

repositories {
    mavenCentral()
}

dependencies {
    mappings(loom.officialMojangMappings())
    minecraft(libs.minecraft)
    modImplementation(libs.architectury)
    modImplementation(libs.fabric.loader)
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutines.core)
}

tasks.register<Copy>("expandInfo") {
    val replaceProperties = mapOf(
        "mod_id" to MOD_ID,
        "mod_name" to MOD_NAME
    )
    doFirst {
        delete("src/main/kotlin/dev/ms0503/mcserverautocloser/MCSACInfo.kt")
    }
    expand(replaceProperties)
    from("MCSACInfo.kt")
    into("src/main/kotlin/dev/ms0503/mcserverautocloser")
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn("expandInfo")
}

tasks.withType<KotlinCompile>().configureEach {
    dependsOn("expandInfo")
}

tasks.withType<SourceTask>().configureEach {
    dependsOn("expandInfo")
}

tasks.named("sourcesJar") {
    dependsOn("expandInfo")
}
