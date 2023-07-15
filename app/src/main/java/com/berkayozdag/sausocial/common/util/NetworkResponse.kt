package com.berkayozdag.sausocial.common.util

sealed class NetworkResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : NetworkResponse<T>()
    data class Error(val errorMessage: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()

}
