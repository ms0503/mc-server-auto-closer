package dev.ms0503.mcserverautocloser

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class MCSACConfig {
    companion object {
        private val gson: Gson = GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

        val config: MCSACConfig by lazy { loadFromFile(MCSACConfigFile.get()) }

        fun loadFromFile(file: File): MCSACConfig {
            val config = if(file.exists()) {
                BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)).use { reader ->
                    gson.fromJson(reader, MCSACConfig::class.java)
                }
            } else {
                MCSACConfig()
            }
            saveToFile(file, config)
            return config
        }

        fun saveToFile(file: File, config: MCSACConfig) {
            OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8).use { writer ->
                gson.toJson(config, writer)
            }
        }
    }

    val waitTime: Long = 1000L * 60L * 15L // 15min
}
