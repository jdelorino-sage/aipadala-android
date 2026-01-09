package com.aipadala.android.core.util

/**
 * A generic class that holds a value or an error status.
 * Used throughout the app for handling API responses and data states.
 */
sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> default
    }

    inline fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, code, throwable)
        is Loading -> Loading
    }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    inline fun onError(action: (String, Throwable?) -> Unit): Resource<T> {
        if (this is Error) {
            action(message, throwable)
        }
        return this
    }

    inline fun onLoading(action: () -> Unit): Resource<T> {
        if (this is Loading) {
            action()
        }
        return this
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String, code: Int? = null, throwable: Throwable? = null) =
            Error(message, code, throwable)
        fun loading() = Loading
    }
}
