package dev.ms0503.mcserverautocloser.forge

import dev.ms0503.mcserverautocloser.MCSACInfo
import dev.ms0503.mcserverautocloser.MCSACMod
import dev.ms0503.mcserverautocloser.MCSACMod.log
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent
import net.minecraftforge.fml.relauncher.Side

@EventBusSubscriber(Side.SERVER, modid = MCSACInfo.MOD_ID)
@Mod(modLanguageAdapter = "io.github.chaosunity.forgelin.KotlinAdapter", modid = MCSACInfo.MOD_ID, useMetadata = true)
object MCSACModForge {
    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log = event.modLog
        MCSACMod.init()
        log!!.info("${MCSACInfo.MOD_NAME} initialized!")
    }

    @EventHandler
    fun onServerStarted(@Suppress("unused") event: FMLServerStartedEvent) {
        MCSACMod.onServerStarted(FMLCommonHandler.instance().minecraftServerInstance)
    }

    @SubscribeEvent
    fun onPlayerJoined(event: PlayerLoggedInEvent) {
        MCSACMod.onPlayerJoined(event.player.server!!)
    }

    @SubscribeEvent
    fun onPlayerLeaved(event: PlayerLoggedOutEvent) {
        MCSACMod.onPlayerLeaved(event.player.server!!)
    }
}
