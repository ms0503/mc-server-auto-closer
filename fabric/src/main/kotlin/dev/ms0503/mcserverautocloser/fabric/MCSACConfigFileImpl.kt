package dev.ms0503.mcserverautocloser.fabric

import java.nio.file.Path
import net.fabricmc.loader.api.FabricLoader

object MCSACConfigFileImpl {
    @JvmStatic
    fun getConfigDir(): Path = FabricLoader.getInstance().configDir
}
