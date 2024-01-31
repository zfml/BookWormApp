package com.zfml.bookworm.domain.model

sealed class Response<out T> {

    object Loading: Response<Nothing>()

    data class Success<T>(val data: T): Response<T>()

    data class Failure(val e: Exception?): Response<Nothing>()


}