package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

private const val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
private const val size = 11

fun generateLink() :String {
    var randomStr = ""
    for (i in 0 .. size){
        if (i == 3 || i == 8) {
            randomStr += "-"
        } else{
            val rNum = Math.floor(Math.random() * chars.length).toInt()
            randomStr += chars.substring(rNum, rNum+1)
        }
    }
    return randomStr
}
