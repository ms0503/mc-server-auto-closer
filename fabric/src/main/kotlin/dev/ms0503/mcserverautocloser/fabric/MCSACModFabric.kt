package dev.ms0503.mcserverautocloser.fabric

import dev.ms0503.mcserverautocloser.MCSACConfig
import dev.ms0503.mcserverautocloser.MCSACInfo
import dev.ms0503.mcserverautocloser.MCSACMod
import dev.ms0503.mcserverautocloser.MCSACMod.LOG
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.MinecraftServer

object MCSACModFabric : ModInitializer {
    override fun onInitialize() {
        MCSACMod.init()
        ServerPlayConnectionEvents.JOIN.register(::onPlayerJoined)
        ServerPlayConnectionEvents.DISCONNECT.register(::onPlayerLeaved)
        ServerLifecycleEvents.SERVER_STARTED.register(::onServerStarted)
        LOG.info("Loaded setting: Wait for joining player: ${MCSACConfig.config.waitTime} ms")
        LOG.info("${MCSACInfo.MOD_NAME} initialized!")
    }

    fun onServerStarted(server: MinecraftServer) {
        MCSACMod.onServerStarted(server)
    }

    fun onPlayerJoined(handler: ServerGamePacketListener, sender: PacketSender, server: MinecraftServer) {
        MCSACMod.onPlayerJoined(server)
    }

    fun onPlayerLeaved(handler: ServerGamePacketListener, server: MinecraftServer) {
        MCSACMod.onPlayerLeaved(server)
    }
}
