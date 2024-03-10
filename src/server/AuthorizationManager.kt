package server

import lib.json.JsonMapper
import lib.json.fromJson
import lib.json.toJson
import org.apache.logging.log4j.kotlin.Logging
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

class AuthorizationManager(
    private val usersInfoDirectory: Path,
    private val jsonMapper: JsonMapper = JsonMapper()
) : Logging {
    private val usersInfo: HashSet<AuthorizationHeader> = HashSet()

    init {
        val usersInfoFile = usersInfoDirectory.toFile()

        if (!usersInfoFile.exists()) {
            usersInfoFile.mkdirs()
        }

        require(usersInfoFile.isDirectory)

        usersInfoFile.walk().forEach {
            if (it.isFile) {
                val authorizationHeader: AuthorizationHeader = jsonMapper.fromJson(it)
                usersInfo.add(authorizationHeader)
            }
        }

        logger.info("Users info loaded")
    }

    fun isUserAuthorized(authorizationHeader: AuthorizationHeader): Boolean {
        return usersInfo.contains(authorizationHeader)
    }

    fun addUser(authorizationHeader: AuthorizationHeader) {
        usersInfo.add(authorizationHeader)
        getAuthorizationFilePath(authorizationHeader).writeText(jsonMapper.toJson(authorizationHeader))
    }

    fun removeUser(authorizationHeader: AuthorizationHeader) {
        getAuthorizationFilePath(authorizationHeader).deleteIfExists()
    }

    private fun getAuthorizationFilePath(authorizationHeader: AuthorizationHeader): Path {
        return usersInfoDirectory.resolve("${authorizationHeader.login}.json")
    }
}