import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.Socket
import kotlin.jvm.Throws
import kotlin.text.StringBuilder

@Throws(IOException::class)
internal fun startSocket() {
    Thread {
        val socket = Socket("127.0.0.1", 1112)
    }.start()
}

class UserSocket(private val scope: CoroutineScope) {

    private lateinit var socket: Socket
    private val _state = MutableStateFlow(Message())
    val state = _state.asStateFlow()

    init {
        Thread {
            socket = Socket("127.0.0.1", 1112)
            receiveMessage()
        }.start()
    }

    private fun receiveMessage() {
        while (true) {
            val strBuilder = StringBuilder()
            val length = socket.getInputStream().read()
            for (i in 1..length) {
                strBuilder.append(socket.getInputStream().read().toChar())
            }
            scope.launch {
                _state.emit(
                    Message(
                        message = strBuilder.toString(),
                        messageType = MessageType.SERVER
                    )
                )
                println("received")
            }
        }
    }

    fun sendMessage(text: String) {
        socket.getOutputStream().write(text.length)
        socket.getOutputStream().write(text.toByteArray())
    }
}