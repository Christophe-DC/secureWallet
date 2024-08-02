package com.cdcoding.network.util

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : com.cdcoding.network.util.Error>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> Result.Error(error)
    }
}


inline fun <T, E : Error> Result<T, E>.getOrNull(): T? {
    return if (this is Result.Success) {
        data
    }
    else {
        null
    }
}


inline fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<Unit, E> = map { }

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> {
            action(data)
            this
        }

        is Result.Error -> this
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}

inline fun <R, T, E : Error> Result<T, E>.fold(
    onSuccess: (value: T) -> R,
    onError: (E) -> R
): R {

    return when (this) {
        is Result.Success -> {
            onSuccess(data)
        }

        is Result.Error -> onError(error)
    }

}

typealias EmptyResult<T, E> = Result<T, E>