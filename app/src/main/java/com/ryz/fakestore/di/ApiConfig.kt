package com.ryz.fakestore.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.ryz.fakestore.BuildConfig
import com.ryz.fakestore.data.remote.ApiService
import com.ryz.fakestore.data.remote.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {

        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return okhttpClient.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideEndpoint(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}