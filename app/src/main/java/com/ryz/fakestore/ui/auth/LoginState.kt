package com.ryz.fakestore.ui.auth

import com.ryz.fakestore.data.model.response.UserResponse

data class LoginState (
    val userId: Int? = null,
    val username: String? = null,
    val userResponse: UserResponse? = null,
    val isLoading: Boolean = false,
    val snackBarErrorMessage: String? = null
)