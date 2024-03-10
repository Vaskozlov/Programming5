package lib.net.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

open class Client(private val address: InetAddress, private val port: Int) {
    private val socket = DatagramSocket()

    constructor(address: String, port: Int) : this(InetAddress.getByName(address), port)

    fun receive(): String {
        val buffer = ByteArray(ClientConstants.bufferSize)
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)

        return String(buffer, 0, packet.length)
    }

    fun send(data: ByteArray) {
        val packet = DatagramPacket(data, data.size, address, port)
        socket.send(packet)
    }

    companion object ClientConstants {
        const val bufferSize = 2048
    }
}
