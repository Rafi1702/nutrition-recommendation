package com.example.nutritiontracker

import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.ui.utils.network.HeaderInterceptor
import com.example.nutritiontracker.ui.utils.network.ResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun httpClient(localTokenDataSource: LocalTokenDataSource): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).addNetworkInterceptor(
            ResponseInterceptor(localTokenDataSource)
        ).build()
    }

    @Singleton
    @Provides
    fun provideNetworkClient(localTokenDataSource: LocalTokenDataSource): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL).client(httpClient(localTokenDataSource))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
