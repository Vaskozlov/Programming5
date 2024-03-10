package lib.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class JsonMapper {
    var objectMapper: ObjectMapper = ObjectMapper()

    init {
        objectMapper.findAndRegisterModules()
        objectMapper.registerKotlinModule()
    }
}
