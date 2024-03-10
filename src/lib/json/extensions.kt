package lib.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

inline fun <reified T> JsonMapper.fromJson(json: String): T = objectMapper.readValue(json)
inline fun <reified T> JsonMapper.fromJson(json: JsonNode): T = fromJson(json.toString())
inline fun <reified T> JsonMapper.fromJson(file: File): T = objectMapper.readValue(file)
inline fun <reified T> JsonMapper.toJson(obj: T): String = objectMapper.writeValueAsString(obj)
