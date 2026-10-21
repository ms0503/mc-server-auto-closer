import dev.ms0503.mcserverautocloser.Constants.MOD_ID
import dev.ms0503.mcserverautocloser.Constants.MOD_NAME
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `common-parts`
}

minecraft {
    mcVersion.set(libs.versions.minecraft.get())
    mcpMappingChannel.set("stable")
    mcpMappingVersion.set("12")
}

repositories {
    mavenCentral()
}

dependencies {
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

tasks.named("compileMcLauncherKotlin") {
    dependsOn("createMcLauncherFiles")
}

tasks.named("compilePatchedMcKotlin") {
    dependsOn("decompressDecompiledSources")
}

tasks.named("compileInjectedTagsKotlin") {
    dependsOn("injectTags")
}
