package com.ryz.fakestore.utils

import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

inline fun <Res, Return> executeFlow(
    crossinline callApi: suspend () -> Response<Res>,
    crossinline onSuccess: suspend (Res, FlowCollector<Resource<Return>>) -> Unit
) =
    flow {
        emit(Resource.Loading(true))
        val response = callApi()
        emit(Resource.Loading(false))
       if (response.isSuccessful) {
           response.body()?.let { data -> onSuccess(data, this) }
       } else {
           val errorBody = response.errorBody()?.charStream()?.readText()
           emit(Resource.Error(errorBody.orEmpty()))
       }
    }.catch { ex ->
        emit(Resource.Loading(false))
        emit(Resource.Error(ex.message.orEmpty()))
    }.flowOn(Dispatchers.IO)