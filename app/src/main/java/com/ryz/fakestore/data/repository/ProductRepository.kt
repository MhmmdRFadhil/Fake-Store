package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllCategory(): Flow<Resource<List<String>>>
    suspend fun getProductByCategory(category: String): Flow<Resource<List<ProductResponse>>>
    suspend fun getProduct(): Flow<Resource<List<ProductResponse>>>
    suspend fun getProductDetail(productId: Int): Flow<Resource<ProductResponse>>
}