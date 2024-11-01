package com.ryz.fakestore.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.fakestore.data.local.LocalDataSource
import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.data.model.response.LoginResponse
import com.ryz.fakestore.data.model.response.UserResponse
import com.ryz.fakestore.data.remote.Resource
import com.ryz.fakestore.data.repository.AuthRepository
import com.ryz.fakestore.utils.GenericUiState
import com.ryz.fakestore.utils.updateFromResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _loginState =
        MutableStateFlow<GenericUiState<LoginResponse>>(GenericUiState.Loading(false))
    val loginState = _loginState.asStateFlow()

    private val _userData =
        MutableStateFlow<GenericUiState<UserResponse>>(GenericUiState.Loading(false))
    val userData = _userData.asStateFlow()

    fun login(request: LoginRequest) = viewModelScope.launch {
        repository.login(request).collect { resource ->
            _loginState.updateFromResource(resource)
        }
    }

    fun getUser() = viewModelScope.launch {
        repository.getUser().collect { resource ->
            if (resource is Resource.Success) {
                val userId = resource.data.find { it.username == localDataSource.getUsername() }
                userId?.id?.let { id ->
                    localDataSource.saveUserId(id)
                }
            }
        }
    }

    fun getUserById(userId: Int) = viewModelScope.launch {
        repository.getUserById(userId).collect { resource ->
            _userData.updateFromResource(resource)
        }
    }
}