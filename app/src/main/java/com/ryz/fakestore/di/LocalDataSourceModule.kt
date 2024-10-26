package com.ryz.fakestore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.ryz.fakestore.data.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    private val Context.dataStore by preferencesDataStore("app_preferences")

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): LocalDataSource =
        LocalDataSource(context.dataStore)
}