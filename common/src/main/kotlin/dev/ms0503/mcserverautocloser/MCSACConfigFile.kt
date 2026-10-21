package dev.ms0503.mcserverautocloser

import dev.ms0503.mcserverautocloser.MCSACInfo.MOD_ID
import java.io.File
import java.nio.file.Path
// Platform-dependent code to improve compatibility with new version
import net.minecraftforge.fml.common.Loader

object MCSACConfigFile {
    fun get(): File = getConfigDir().resolve("${MOD_ID}.json").toFile()

    // Platform-dependent code to improve compatibility with new version
    @JvmStatic
    fun getConfigDir(): Path = Loader.instance().configDir.toPath()
}
