import kotlinx.coroutines.runBlocking
import server.DatabaseCommandsReceiver
import kotlin.io.path.Path

object Server {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val receiver = DatabaseCommandsReceiver(
                8080,
                Path("/Volumes/ramdisk/tmp/clients"),
                Path("/Volumes/ramdisk/tmp/database")
            )
            receiver.run()
        }
    }
}