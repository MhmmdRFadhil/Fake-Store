package com.ryz.fakestore.data.model.request

import com.google.gson.annotations.SerializedName

data class CartRequest(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null
)

data class ProductsItem(

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null
)
