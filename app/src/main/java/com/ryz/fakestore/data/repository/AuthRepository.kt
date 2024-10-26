package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.data.model.response.LoginResponse
import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Flow<Resource<LoginResponse>>
}