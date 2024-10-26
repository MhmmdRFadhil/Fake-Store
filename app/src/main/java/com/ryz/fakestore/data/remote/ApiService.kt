package com.ryz.fakestore.data.remote

import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.data.model.response.LoginResponse
import com.ryz.fakestore.data.model.response.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/products/categories")
    suspend fun getAllCategory(): Response<List<String>>

    @GET("/products/category/jewelery")
    suspend fun getProductByCategory(): Response<ProductResponse>

    @GET("/products")
    suspend fun getProduct(): Response<ProductResponse>

    @GET("/products")
    suspend fun getSortProduct(
        @Query("sort") sort: String
    ): Response<ProductResponse>

    @GET("/products/{product_id}")
    suspend fun getDetailProduct(
        @Path("product_id") productId: Int
    ): Response<ProductResponse>
}