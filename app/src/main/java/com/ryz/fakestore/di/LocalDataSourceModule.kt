package com.ryz.fakestore.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ryz.fakestore.data.local.LocalDataSource
import com.ryz.fakestore.data.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): LocalDataSource {
        return LocalDataSourceImpl(sharedPreferences)
    }
}