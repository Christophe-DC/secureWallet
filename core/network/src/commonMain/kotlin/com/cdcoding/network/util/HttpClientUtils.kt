package com.cdcoding.network.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException


suspend inline fun <reified T> HttpResponse.getResult(): Result<T, NetworkError> {
    val response: HttpResponse = try {
        this
    } catch (e: UnresolvedAddressException) {
        e.stackTraceToString()
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    }


    return when (response.status.value) {
        in 200..299 -> {
            try {
                val responseBody = response.body<T>()
                Result.Success(responseBody)
            } catch (e: SerializationException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }

        401 -> Result.Error(NetworkError.UNAUTHORIZED)
        409 -> Result.Error(NetworkError.CONFLICT)
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}