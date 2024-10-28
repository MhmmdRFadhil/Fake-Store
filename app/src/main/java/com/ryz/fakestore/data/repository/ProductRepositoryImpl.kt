package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.data.remote.ApiService
import com.ryz.fakestore.data.remote.Resource
import com.ryz.fakestore.utils.executeFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    ProductRepository {
    override suspend fun getAllCategory(): Flow<Resource<List<String>>> =
        executeFlow(callApi = {
            apiService.getAllCategory()
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }

    override suspend fun getProductByCategory(category: String): Flow<Resource<List<ProductResponse>>> =
        executeFlow(callApi = {
            apiService.getProductByCategory(category)
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }

    override suspend fun getProduct(): Flow<Resource<List<ProductResponse>>> =
        executeFlow(callApi = {
            apiService.getProduct()
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }

    override suspend fun getProductDetail(productId: Int): Flow<Resource<ProductResponse>> =
        executeFlow(callApi = {
            apiService.getDetailProduct(productId)
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }
}