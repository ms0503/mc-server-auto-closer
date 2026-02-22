package dev.ms0503.mcserverautocloser.forge

import java.nio.file.Path
import net.minecraftforge.fml.loading.FMLPaths

object MCSACConfigFileImpl {
    @JvmStatic
    fun getConfigDir(): Path = FMLPaths.CONFIGDIR.get()
}
