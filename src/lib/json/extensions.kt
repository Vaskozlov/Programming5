package lib.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue

inline fun <reified T> JsonMapper.fromJson(json: String): T = objectMapper.readValue(json)
inline fun <reified T> JsonMapper.fromJson(json: JsonNode): T = fromJson(json.toString())
fun JsonMapper.toJson(obj: Any?): String = objectMapper.writeValueAsString(obj)
