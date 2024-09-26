package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

private val algorithm = "AES/CBC/PKCS5Padding"

fun generateRandomIV(): ByteArray {
    val iv = ByteArray(16)
    Random.nextBytes(iv)
    return iv
}

fun encrypt(inputText: String, secretKey: String): String {
    val key = SecretKeySpec(secretKey.toByteArray().copyOf(16), "AES")
    val cipher = Cipher.getInstance(algorithm)
    val iv = generateRandomIV()
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
    val cipherText = cipher.doFinal(inputText.toByteArray())

    return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(cipherText)
}

fun decrypt(cipherText: String, secretKey: String): String {
    val key = SecretKeySpec(secretKey.toByteArray().copyOf(16), "AES")
    val cipher = Cipher.getInstance(algorithm)
    val parts = cipherText.split(":")
    val iv = Base64.getDecoder().decode(parts[0])
    val cipherTextBytes = Base64.getDecoder().decode(parts[1])
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
    val plainText = cipher.doFinal(cipherTextBytes)

    return String(plainText)
}
