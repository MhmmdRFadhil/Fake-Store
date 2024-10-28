package com.ryz.fakestore.utils

import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.data.model.response.ProductResponse
import com.ryz.fakestore.data.model.response.Rating

fun CartProductEntity.toProductResponse(): ProductResponse {
    return ProductResponse(
        image = image,
        price = price,
        Rating(
            count = count,
            rate = rate
        ),
        description = description,
        id = productId,
        title = title,
        category = category
    )
}

fun ProductResponse.toCartProductEntity(userId: Int): CartProductEntity {
    return CartProductEntity(
        title = title.orEmpty(),
        price = price ?: 0.0,
        image = image.orEmpty(),
        quantity = 1,
        userId = userId,
        date = getCurrentDate(pattern = "yyyy-MM-dd"),
        productId = id ?: 0,
        category = category.orEmpty(),
        description = description.orEmpty(),
        count = rating?.count ?: 0,
        rate = rating?.rate ?: 0.0
    )
}