package dev.ms0503.mcserverautocloser.neoforge

import dev.ms0503.mcserverautocloser.MCSACInfo
import dev.ms0503.mcserverautocloser.MCSACMod
import dev.ms0503.mcserverautocloser.MCSACMod.LOG
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent

@Mod(MCSACInfo.MOD_ID)
class MCSACModNeoForge {
    init {
        MCSACMod.init()
        NeoForge.EVENT_BUS.addListener(::onServerStarted)
        NeoForge.EVENT_BUS.addListener(::onPlayerJoined)
        NeoForge.EVENT_BUS.addListener(::onPlayerLeaved)
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
