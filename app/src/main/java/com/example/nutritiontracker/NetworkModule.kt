package com.example.nutritiontracker

import com.example.nutritiontracker.ui.utils.network.AuthorizationInterceptor
import com.example.nutritiontracker.ui.utils.network.ContentTypeInterceptor
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
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor()).addInterceptor(
            ContentTypeInterceptor()
        ).build()
    }

    @Singleton
    @Provides
    fun provideNetworkClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL).client(httpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}