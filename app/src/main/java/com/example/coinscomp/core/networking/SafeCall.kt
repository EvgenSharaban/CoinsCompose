package com.example.coinscomp.core.networking

import android.util.Log
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.data.network.entities.mappers.FromEntityToDomainMapper
import retrofit2.Response


suspend fun <T> safeApiCall(
    action: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = action()
        ApiErrorHandler.DefaultApiErrorHandler<T>().handleApiResponse(response)
    } catch (e: Throwable) {
        Log.e(TAG, "error during request: $e", e)
        Result.failure(e)
    }
}

fun <R, T> Result<T>.toDomain(mapper: FromEntityToDomainMapper<T, R>): Result<R> {
    return this.map { value ->
        mapper.mapToDomain(value)
    }
}

suspend fun <T> safeApiCallList(
    action: suspend () -> Response<List<T>>
): Result<List<T>> {
    return try {
        val response = action()
        ApiErrorHandler.DefaultApiErrorHandler<T>().handleApiList(response, response.body())
    } catch (e: Throwable) {
        Log.e(TAG, "error during request: $e", e)
        Result.failure(e)
    }
}

fun <R, T> Result<List<T>>.toDomainList(mapper: FromEntityToDomainMapper<T, R>): Result<List<R>> {
    return this.map { value ->
        mapper.mapToDomainList(value)
    }
}