package dev.ms0503.mcserverautocloser

import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit
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
    private const val DAY_MS = 1000L * 60L * 60L * 24L
    private var thread: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun id(name: String) = ResourceLocation(MCSACInfo.MOD_ID, name)

    fun init() {
        LOG.info("Loaded setting: Enable time: ${MCSACConfig.config.enableTimeStart} - ${MCSACConfig.config.enableTimeEnd}")
        LOG.info("Loaded setting: Wait for joining player: ${MCSACConfig.config.waitTime} ms")
    }

    suspend fun stop(server: MinecraftServer) {
        delay(MCSACConfig.config.waitTime)
        server.commands.performPrefixedCommand(server.createCommandSourceStack(), "stop")
    }

    fun onServerStarted(server: MinecraftServer) {
        val now = LocalTime.now()
        if(isBetween(now, MCSACConfig.config.enableTimeStart, MCSACConfig.config.enableTimeEnd)) {
            this.thread = scope.launch {
                try {
                    stop(server)
                } catch(e: CancellationException) {
                    // no-op
                }
            }
            LOG.info("Waiting for players...")
        } else {
            val waitForEnableMs = Duration.between(now, MCSACConfig.config.enableTimeStart)
                .toMillis()
                .let { if(it < 0) DAY_MS + it else it }
            this.thread = scope.launch {
                try {
                    delay(waitForEnableMs)
                    stop(server)
                } catch(e: CancellationException) {
                    // no-op
                }
            }
        }
    }

    fun onPlayerJoined(server: MinecraftServer) {
        this.thread?.cancel()
        if(this.thread != null) {
            LOG.info("Canceled waiting for players.")
        }
        this.thread = null
    }

    fun onPlayerLeaved(server: MinecraftServer) {
        val now = LocalTime.now()
        val waitForDisableMs = Duration.between(now, MCSACConfig.config.enableTimeEnd)
            .toMillis()
            .let { if(it < 0) DAY_MS + it else it }
        if(server.playerCount == 1) {
            if(MCSACConfig.config.waitTime <= waitForDisableMs) {
                this.thread = scope.launch {
                    try {
                        stop(server)
                    } catch(e: CancellationException) {
                        // no-op
                    }
                }
                LOG.info("There is no player! Waiting for players...")
            } else {
                val waitForEnableMs = Duration.between(now, MCSACConfig.config.enableTimeStart)
                    .toMillis()
                    .let { if(it < 0) DAY_MS + it else it }
                this.thread = scope.launch {
                    try {
                        delay(waitForEnableMs)
                        stop(server)
                    } catch(e: CancellationException) {
                        // no-op
                    }
                }
                LOG.info("There is no player! But disabling time will come soon.")
            }
        }
    }

    private fun isBetween(time: LocalTime, start: LocalTime, end: LocalTime) =
        (start.until(time, ChronoUnit.MILLIS) <= 0) xor
            (time.until(end, ChronoUnit.MILLIS) < 0) xor
            (start.until(end, ChronoUnit.MILLIS) < 0)
}
