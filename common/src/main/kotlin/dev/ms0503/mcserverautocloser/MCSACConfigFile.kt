package dev.ms0503.mcserverautocloser

// Platform-dependent code to improve compatibility with new version
import cpw.mods.fml.common.Loader
import dev.ms0503.mcserverautocloser.MCSACInfo.MOD_ID
import java.io.File
import java.nio.file.Path

object MCSACConfigFile {
    fun get(): File = getConfigDir().resolve("${MOD_ID}.json").toFile()

    // Platform-dependent code to improve compatibility with new version
    @JvmStatic
    fun getConfigDir(): Path = Loader.instance().configDir.toPath()
}
