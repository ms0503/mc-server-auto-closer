package dev.ms0503.mcserverautocloser

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MCSACMod {
    val LOG: Logger = LoggerFactory.getLogger(MCSACInfo.MOD_ID)
    private var thread: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun id(name: String) = ResourceLocation(MCSACInfo.MOD_ID, name)

    fun init() {
    }

    suspend fun stop(server: MinecraftServer) {
        delay(MCSACConfig.config.waitTime)
        server.commands.performPrefixedCommand(server.createCommandSourceStack(), "stop")
    }

    fun onServerStarted(server: MinecraftServer) {
        this.thread = scope.launch {
            try {
                stop(server)
            } catch(e: CancellationException) {
                // no-op
            }
        }
        LOG.info("Waiting for players...")
    }

    fun onPlayerJoined(server: MinecraftServer) {
        this.thread?.cancel()
        if(this.thread != null) {
            LOG.info("Canceled waiting for players.")
        }
        this.thread = null
    }

    fun onPlayerLeaved(server: MinecraftServer) {
        if(server.playerCount == 1) {
            this.thread = scope.launch {
                try {
                    stop(server)
                } catch(e: CancellationException) {
                    // no-op
                }
            }
            LOG.info("There is no player! Waiting for players...")
        }
    }
}
