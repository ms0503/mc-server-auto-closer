package dev.ms0503.mcserverautocloser.forge

import dev.architectury.platform.forge.EventBuses
import dev.ms0503.mcserverautocloser.MCSACInfo
import dev.ms0503.mcserverautocloser.MCSACMod
import dev.ms0503.mcserverautocloser.MCSACMod.LOG
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod(MCSACInfo.MOD_ID)
class MCSACModForge {
    init {
        EventBuses.registerModEventBus(MCSACInfo.MOD_ID, MOD_CONTEXT.getKEventBus())
        MCSACMod.init()
        MinecraftForge.EVENT_BUS.addListener(::onServerStarted)
        MinecraftForge.EVENT_BUS.addListener(::onPlayerJoined)
        MinecraftForge.EVENT_BUS.addListener(::onPlayerLeaved)
        LOG.info("${MCSACInfo.MOD_NAME} initialized!")
    }

    fun onServerStarted(event: ServerStartedEvent) {
        MCSACMod.onServerStarted(event.server)
    }

    fun onPlayerJoined(event: PlayerEvent.PlayerLoggedInEvent) {
        MCSACMod.onPlayerJoined(event.entity.server!!)
    }

    fun onPlayerLeaved(event: PlayerEvent.PlayerLoggedOutEvent) {
        MCSACMod.onPlayerLeaved(event.entity.server!!)
    }
}
