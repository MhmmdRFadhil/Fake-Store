package com.ryz.fakestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ryz.fakestore.data.local.model.CartProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProduct: CartProductEntity)

    @Query("SELECT * FROM cart_product WHERE productId = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): CartProductEntity?

    @Query("DELETE FROM cart_product")
    suspend fun deleteAll()

    @Update
    suspend fun update(cartItem: CartProductEntity)

    @Query("DELETE FROM cart_product WHERE id =:id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM cart_product")
    fun getAllCartProducts(): Flow<List<CartProductEntity>>
}