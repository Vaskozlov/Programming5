import application.*
import client.RemoteDatabase
import kotlinx.coroutines.Dispatchers

object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        val serverIp = System.getenv("SERVER_IP") ?: "localhost"
        val serverPort = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 8080

        val application = Application(
            RemoteDatabase(
                serverIp,
                serverPort
            ), Dispatchers.Default
        )

        application.start()
    }
}