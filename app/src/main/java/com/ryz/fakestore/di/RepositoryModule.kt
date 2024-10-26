package com.ryz.fakestore.di

import com.ryz.fakestore.data.local.LocalDataSource
import com.ryz.fakestore.data.remote.ApiService
import com.ryz.fakestore.data.repository.AuthRepository
import com.ryz.fakestore.data.repository.AuthRepositoryImpl
import com.ryz.fakestore.data.repository.ProductRepository
import com.ryz.fakestore.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        localDataSource: LocalDataSource
    ): AuthRepository = AuthRepositoryImpl(apiService, localDataSource)

    @Provides
    @Singleton
    fun provideProductRepository(apiService: ApiService): ProductRepository =
        ProductRepositoryImpl(apiService)
}