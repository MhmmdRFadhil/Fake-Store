package com.ryz.fakestore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_product")
data class CartProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String,
    val productId: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rate: Double,
    val count: Int,
    val quantity: Int
)
