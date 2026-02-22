package dev.ms0503.mcserverautocloser

import dev.architectury.injectables.annotations.ExpectPlatform
import dev.ms0503.mcserverautocloser.MCSACInfo.MOD_ID
import java.io.File
import java.nio.file.Path

object MCSACConfigFile {
    fun get(): File = getConfigDir().resolve("${MOD_ID}.json").toFile()

    @ExpectPlatform
    @JvmStatic
    fun getConfigDir(): Path = throw AssertionError()
}
