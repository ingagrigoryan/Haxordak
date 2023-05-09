import java.security.*
import java.util.*
import javax.crypto.Cipher

class RSAEncryptor {

    @Throws(Exception::class)
    fun encryptMessage(plainText: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.getEncoder()
            .encodeToString(
                cipher.doFinal(plainText.toByteArray())
            )
    }

    @Throws(Exception::class)
    fun decryptMessage(encryptedText: String?, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)))
    }
}