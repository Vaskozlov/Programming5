import kotlinx.coroutines.Dispatchers
import server.DatabaseCommandsReceiver
import kotlin.io.path.Path

object Server {
    @JvmStatic
    fun main(args: Array<String>) {
        val port = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 8080

        val receiver = DatabaseCommandsReceiver(
            port,
            Dispatchers.Default,
            Path("/tmp/ramdisk/tmp/clients"),
            Path("/tmp/ramdisk/tmp/database")
        )
        receiver.run()

    }
}