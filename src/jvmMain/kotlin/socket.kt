import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ObjectInputStream
import java.net.Socket
import java.security.PrivateKey
import java.security.PublicKey
import kotlin.text.StringBuilder

class UserSocket(private val scope: CoroutineScope, private val rsa: RSAEncryptor) {

    private lateinit var socket: Socket
    private val _state = MutableStateFlow(Message())
    
    var publicKey: PublicKey? = null
    var privateKey: PrivateKey? = null
    val state = _state.asStateFlow()
    
    

    init {
        Thread {
            try{
                socket = Socket("127.0.0.1", 1112)
                receiveKeys()
                receiveMessage()
            }catch (e: Exception) {
                println("No host found")
            }
        }.start()
    }

    private fun receiveKeys() {
        val objectInputStream = ObjectInputStream(socket.getInputStream())
        publicKey = objectInputStream.readObject() as PublicKey
        privateKey = objectInputStream.readObject() as PrivateKey
    }

    private fun receiveMessage() {
        while (true) {
            val strBuilder = StringBuilder()
            val length = socket.getInputStream().read()
            for (i in 1..length) {
                strBuilder.append(socket.getInputStream().read().toChar())
            }
            val decrypted = rsa.decryptMessage(strBuilder.toString(), privateKey!!)
            scope.launch {
                _state.emit(
                    Message(
                        message = decrypted,
                        messageType = MessageType.SERVER
                    )
                )
            }
        }
    }

    fun sendMessage(text: String) {
        socket.getOutputStream().write(text.length)
        socket.getOutputStream().write(text.toByteArray())
    }
}