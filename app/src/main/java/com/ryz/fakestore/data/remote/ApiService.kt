package com.ryz.fakestore.data.remote

import com.ryz.fakestore.data.model.request.LoginRequest
import com.ryz.fakestore.data.model.response.LoginResponse
import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.data.model.response.UserResponse
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

    @GET("/products/category/{category}")
    suspend fun getProductByCategory(
        @Path("category") category: String
    ): Response<List<ProductResponse>>

    @GET("/products")
    suspend fun getProduct(): Response<List<ProductResponse>>

    @GET("/products")
    suspend fun getSortProduct(
        @Query("sort") sort: String
    ): Response<List<ProductResponse>>

    @GET("/products/{product_id}")
    suspend fun getDetailProduct(
        @Path("product_id") productId: Int
    ): Response<ProductResponse>

    @GET("/users")
    suspend fun getUser(): Response<List<UserResponse>>

    @GET("users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int
    ): Response<UserResponse>
}