package com.ryz.fakestore.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

sealed class GenericUiState<T> {
    class Success<T>(val data: T) : GenericUiState<T>()
    class Loading<T>(val isLoading: Boolean) : GenericUiState<T>()
    class Error<T>(val message: String) : GenericUiState<T>()
}