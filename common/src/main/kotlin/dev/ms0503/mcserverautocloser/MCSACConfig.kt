package dev.ms0503.mcserverautocloser

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MCSACConfig {
    companion object {
        private val gson: Gson = GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
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

    val enableTimeStart: LocalTime = LocalTime.parse("02:00:00")
    val enableTimeEnd: LocalTime = LocalTime.parse("08:00:00")
    val waitTime: Long = 1000L * 60L * 15L // 15min

    private class LocalTimeAdapter : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
        private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime =
            formatter.parse(json.asString, LocalTime::from)

        override fun serialize(src: LocalTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
            JsonPrimitive(formatter.format(src))
    }
}
