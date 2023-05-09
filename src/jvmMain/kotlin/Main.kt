import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun App(socket: UserSocket, rsaEncryptor: RSAEncryptor) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val lazyListState by remember { mutableStateOf(LazyListState()) }
    val messages = mutableListOf<String>()
    val mess = remember {
        mutableStateListOf<Message>()
    }
    val receivedMessage = socket.state.collectAsState()
    if (receivedMessage.value.message.isNotBlank()) {
        mess.add(receivedMessage.value)
    }

    MaterialTheme {

        Column(modifier = Modifier.fillMaxSize()) {
            Text("User", modifier = Modifier.align(Alignment.CenterHorizontally))
            Button(onClick = {
                if (textFieldValue.text.isBlank()) {
                    return@Button
                }
                mess.add(Message(
                    message = textFieldValue.text,
                    messageType = MessageType.USER
                ))
                messages.add(textFieldValue.text)
                try {
                    socket.sendMessage(rsaEncryptor.encryptMessage(textFieldValue.text, socket.publicKey!!))
                }catch (e: Exception) {
                    println("Error ${e.message} ${e.cause}")
                }
                textFieldValue = TextFieldValue()

            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Send")
            }
            TextField(textFieldValue, {
                textFieldValue = it

            }, placeholder = { Text("Write message here...") }, modifier = Modifier.fillMaxWidth())

            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                state = lazyListState) {

                itemsIndexed(mess) {index, message ->
                    when (message.messageType) {
                        MessageType.SERVER -> getText(mess[index].message)
                        MessageType.USER -> getTextFrom(mess[index].message)
                    }
                }
            }
        }
    }
}


fun main() = application {
    val rsa = RSAEncryptor()
    val socket = UserSocket(rememberCoroutineScope(), rsa)
    Window(onCloseRequest = ::exitApplication, title = "Haxordak") {
        App(socket, rsa)
    }
}
