package com.berkayozdag.sausocial.data

sealed class NetworkResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : NetworkResponse<T>()
    data class Error(val errorMessage: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()

}
