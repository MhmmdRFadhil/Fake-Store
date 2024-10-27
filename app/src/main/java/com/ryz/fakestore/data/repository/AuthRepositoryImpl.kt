package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.local.LocalDataSource
import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.data.model.response.LoginResponse
import com.ryz.fakestore.data.model.response.UserResponse
import com.ryz.fakestore.data.remote.ApiService
import com.ryz.fakestore.data.remote.Resource
import com.ryz.fakestore.utils.executeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
) : AuthRepository {
    override suspend fun login(request: LoginRequest): Flow<Resource<LoginResponse>> =
        executeFlow(callApi = {
            apiService.login(request)
        }) { response, flow ->
            localDataSource.saveTokenAuth(response.token.orEmpty())
            localDataSource.saveUsername(request.username.orEmpty())
            flow.emit(Resource.Success(response))
        }

    override suspend fun getUser(): Flow<Resource<UserResponse>> = executeFlow(callApi = {
        apiService.getUser()
    }) { response, flow ->
        val userId = runBlocking {
            localDataSource.userId.firstOrNull()
        }
        if (userId != response.id) {
            flow.emit(Resource.Success(response))
        }
    }

    override suspend fun getUserById(userId: Int): Flow<Resource<UserResponse>> =
        executeFlow(callApi = {
            apiService.getUserById(userId)
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }
}