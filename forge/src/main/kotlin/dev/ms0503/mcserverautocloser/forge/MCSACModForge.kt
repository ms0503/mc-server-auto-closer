package dev.ms0503.mcserverautocloser.forge

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartedEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent
import dev.ms0503.mcserverautocloser.MCSACInfo
import dev.ms0503.mcserverautocloser.MCSACMod
import dev.ms0503.mcserverautocloser.MCSACMod.log
import net.minecraft.server.MinecraftServer
import net.minecraftforge.common.MinecraftForge

@Mod(modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter", modid = MCSACInfo.MOD_ID, useMetadata = true)
object MCSACModForge {
    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log = event.modLog
        MCSACMod.init()
        MinecraftForge.EVENT_BUS.register(this)
        log!!.info("${MCSACInfo.MOD_NAME} initialized!")
    }

    @EventHandler
    fun onServerStarted(@Suppress("unused") event: FMLServerStartedEvent) {
        MCSACMod.onServerStarted(FMLCommonHandler.instance().minecraftServerInstance)
    }

    @SubscribeEvent
    fun onPlayerJoined(@Suppress("unused") event: PlayerLoggedInEvent) {
        MCSACMod.onPlayerJoined(MinecraftServer.getServer())
    }

    @SubscribeEvent
    fun onPlayerLeaved(@Suppress("unused") event: PlayerLoggedOutEvent) {
        MCSACMod.onPlayerLeaved(MinecraftServer.getServer())
    }
}
