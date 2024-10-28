package com.ryz.fakestore.data.repository

import com.ryz.fakestore.data.local.dao.CartProductDao
import com.ryz.fakestore.data.local.model.CartProductEntity
import com.ryz.fakestore.data.remote.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(private val cartProductDao: CartProductDao) :
    CartRepository {
    override fun getCartProducts(): Flow<Resource<List<CartProductEntity>>> = flow {
        emit(Resource.Loading(false))
        try {
            cartProductDao.getAllCartProducts().collect {
                emit(Resource.Success(it))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }


    override suspend fun addOrUpdateCart(item: CartProductEntity): Resource<Unit> {
        return try {
            val existingProduct = cartProductDao.getProductById(item.productId)
            if (existingProduct != null) {
                val updateProduct = existingProduct.copy(quantity = existingProduct.quantity + 1)
                cartProductDao.update(updateProduct)
            } else {
                cartProductDao.insertCartProduct(item)
            }

            Resource.Success(Unit)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun update(item: CartProductEntity): Resource<Unit> {
        return try {
            cartProductDao.update(item)
            Resource.Success(Unit)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun delete(id: Int): Resource<Unit> {
        return try {
            cartProductDao.delete(id)
            Resource.Success(Unit)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun deleteAll(): Resource<Unit> {
        return try {
            cartProductDao.deleteAll()
            Resource.Success(Unit)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }
}