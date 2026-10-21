package dev.ms0503.mcserverautocloser

import java.time.Duration
import java.time.LocalTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toKotlinDuration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.Logger

object MCSACMod {
    var log: Logger? = null
    private const val DAY_MS = 1000L * 60L * 60L * 24L
    private var thread: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun init() {
        log!!.info("Loaded setting: Enable time: ${MCSACConfig.config.enableTimeStart} - ${MCSACConfig.config.enableTimeEnd}")
        log!!.info("Loaded setting: Wait for joining player: ${MCSACConfig.config.waitTime} ms")
    }

    suspend fun stop(server: MinecraftServer) {
        delay(MCSACConfig.config.waitTime.milliseconds)
        server.commandManager.executeCommand(server, "stop")
    }

    fun onServerStarted(server: MinecraftServer) {
        val now = LocalTime.now()
        if(isBetween(now, MCSACConfig.config.enableTimeStart, MCSACConfig.config.enableTimeEnd)) {
            this.thread =
                scope.launch {
                    try {
                        stop(server)
                    } catch(_: CancellationException) {
                        // no-op
                    }
                }
            log!!.info("Waiting for players...")
        } else {
            val waitForEnable = getWaitTimeForEnable(now)
            this.thread =
                scope.launch {
                    try {
                        delay(waitForEnable)
                        stop(server)
                    } catch(_: CancellationException) {
                        // no-op
                    }
                }
        }
    }

    fun onPlayerJoined(@Suppress("unused") server: MinecraftServer) {
        this.thread?.cancel()
        if(this.thread != null) {
            log!!.info("Canceled waiting for players.")
        }
        this.thread = null
    }

    fun onPlayerLeaved(server: MinecraftServer) {
        val now = LocalTime.now()
        val waitForDisable = getWaitTimeForDisable(now)
        if(server.playerList.currentPlayerCount == 1) {
            if(MCSACConfig.config.waitTime.milliseconds <= waitForDisable) {
                this.thread =
                    scope.launch {
                        try {
                            stop(server)
                        } catch(_: CancellationException) {
                            // no-op
                        }
                    }
                log!!.info("There is no player! Waiting for players...")
            } else {
                val waitForEnable = getWaitTimeForEnable(now)
                this.thread =
                    scope.launch {
                        try {
                            delay(waitForEnable)
                            stop(server)
                        } catch(_: CancellationException) {
                            // no-op
                        }
                    }
                log!!.info("There is no player! But disabling time will come soon.")
            }
        }
    }

    private fun getWaitTimeForDisable(now: LocalTime) =
        Duration.between(now, MCSACConfig.config.enableTimeEnd).toKotlinDuration().let {
            if(it.isNegative()) DAY_MS.milliseconds + it else it
        }

    private fun getWaitTimeForEnable(now: LocalTime) =
        Duration.between(now, MCSACConfig.config.enableTimeStart).toKotlinDuration().let {
            if(it.isNegative()) DAY_MS.milliseconds + it else it
        }

    private fun isBetween(time: LocalTime, start: LocalTime, end: LocalTime) =
        if(start < end) {
            time in start..<end
        } else {
            time !in end..<start
        }
}
