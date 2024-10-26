package com.ryz.fakestore.data.remote

sealed class Resource<out R> private constructor() {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: String) : Resource<Nothing>()
    data class Loading(val isLoading: Boolean): Resource<Nothing>()
}