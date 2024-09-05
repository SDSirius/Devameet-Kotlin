package br.com.devaria.Devameet.SDSirius.Devameet_Kotlin.utils

val EMAIL_REGEX = "^[\\w+.]+@\\w+\\.\\w{2,}(?:\\.\\w{2})?\$";
var PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

fun isEmailvalid ( email : String ):Boolean{
    return EMAIL_REGEX.toRegex().matches(email)
}
fun isPasswordvalid ( password : String ):Boolean{
    return PASSWORD_REGEX.toRegex().matches(password)
}