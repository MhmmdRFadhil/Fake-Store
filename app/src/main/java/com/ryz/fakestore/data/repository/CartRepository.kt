package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartProducts(): Flow<Resource<List<CartProductEntity>>>
    suspend fun addOrUpdateCart(item: CartProductEntity): Resource<Unit>
    suspend fun update(item: CartProductEntity): Resource<Unit>
    suspend fun delete(id: Int): Resource<Unit>
    suspend fun deleteAll(): Resource<Unit>
}