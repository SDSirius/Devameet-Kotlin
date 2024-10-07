package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

private val algorithm = "AES/CBC/PKCS5Padding"
private val iv = IvParameterSpec(ByteArray(16))

fun encrypt(inputText: String, secretKey: String) : String {
    val key = SecretKeySpec(secretKey.toByteArray(), "AES")
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val cipherText = cipher.doFinal(inputText.toByteArray())

    return Base64.getEncoder().encodeToString(cipherText)
}

fun decrypt(cipherText: String, secretKey: String): String {
    val key = SecretKeySpec(secretKey.toByteArray(), "AES")
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))

    return String(plainText)
}
