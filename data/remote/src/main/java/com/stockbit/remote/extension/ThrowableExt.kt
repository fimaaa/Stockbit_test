package com.stockbit.remote.extension

import com.google.gson.JsonParser
import com.stockbit.model.common.UIText
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

fun Throwable.toThrowableMessage(content: String = "Data"): UIText = when (this) {
    is HttpException ->
        try {
            val errorJsonString = response()?.errorBody()?.string()
            val messageString = JsonParser.parseString(errorJsonString)
                .asJsonObject["message"]
                .asString
            UIText.DynamicString(messageString)
//            UIText.DynamicString()
        } catch (e: Exception) {
            when (code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> UIText.DynamicString("Tidak dapat mengakses data")
                HttpsURLConnection.HTTP_NOT_FOUND, HttpsURLConnection.HTTP_NO_CONTENT -> UIText.DynamicString(content)
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> UIText.DynamicString("Terjadi gangguan pada server")
                HttpsURLConnection.HTTP_BAD_REQUEST -> UIText.DynamicString("Data tidak sesuai")
                HttpsURLConnection.HTTP_FORBIDDEN -> UIText.DynamicString("Sesi telah berakhir")
                429 -> UIText.DynamicString("Terlalu Banyak Request")
                else -> UIText.DynamicString("Oops, Terjadi gangguan, coba lagi beberapa saat")
            }
        }
    is UnknownHostException -> UIText.DynamicString("Tidak ada koneksi internet")
    is ConnectException, is SocketTimeoutException, is Errors.OfflineException -> UIText.DynamicString("No internet connected")
    is Errors.FetchException -> UIText.DynamicString("Fetch exception")
    else -> UIText.DynamicString("Terjadi kesalahan")
}

fun Throwable.toThrowableCode(): Int = when (this) {
    is HttpException ->
        try {
            val errorJsonString = response()?.errorBody()?.string()
            JsonParser.parseString(errorJsonString)
                .asJsonObject["statusCode"]
                .asInt
        } catch (e: Exception) {
            when (code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> 401
                HttpsURLConnection.HTTP_NOT_FOUND -> 404
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> 500
                HttpsURLConnection.HTTP_BAD_REQUEST -> 400
                HttpsURLConnection.HTTP_FORBIDDEN -> 403
                HttpsURLConnection.HTTP_CONFLICT -> 409
                else -> code()
            }
        }
    else -> 500
}

fun Throwable.errorResponseUrl(): String {
    val httpError = this as? HttpException ?: return "unable to parse"
    return httpError.response()?.raw()?.request()?.url()?.url().toString() ?: "null url"
}

sealed class Errors
    (msg: String) : Exception(msg) {
    class OfflineException(msg: String = "Not Connected to Internet") : Errors(msg)
    class FetchException(msg: String) : Errors(msg)
}
