package dev.ms0503.mcserverautocloser.neoforge

import java.nio.file.Path
import net.neoforged.fml.loading.FMLPaths

object MCSACConfigFileImpl {
    @JvmStatic
    fun getConfigDir(): Path = FMLPaths.CONFIGDIR.get()
}
