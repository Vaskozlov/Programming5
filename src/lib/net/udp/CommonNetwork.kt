package lib.net.udp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lib.json.JsonMapper
import java.net.DatagramPacket
import java.net.DatagramSocket

open class CommonNetwork(private val socket: DatagramSocket, val jsonMapper: JsonMapper = JsonMapper()) {
    constructor(port: Int, jsonMapper: JsonMapper = JsonMapper()) : this(DatagramSocket(port), jsonMapper)

    fun setTimeout(timeout: Int) {
        socket.soTimeout = timeout
    }

    suspend fun send(packet: DatagramPacket, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(dispatcher) {
            socket.send(packet)
        }
    }

    suspend fun receive(packet: DatagramPacket, dispatcher: CoroutineDispatcher = Dispatchers.IO): DatagramPacket {
        withContext(dispatcher) {
            socket.receive(packet)
        }

        return packet
    }

    suspend fun receive(dispatcher: CoroutineDispatcher = Dispatchers.IO): DatagramPacket {
        val buffer = ByteArray(bufferSize)
        val packet = DatagramPacket(buffer, buffer.size)
        return receive(packet, dispatcher)
    }

    suspend fun receiveJson(dispatcher: CoroutineDispatcher = Dispatchers.IO): JsonHolder {
        val packet = receive(dispatcher)
        return packet.constructJsonHolder(jsonMapper.objectMapper)
    }

    fun close() {
        socket.close()
    }
}

