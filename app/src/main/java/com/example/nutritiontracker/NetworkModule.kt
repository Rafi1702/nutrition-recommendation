package com.example.nutritiontracker

import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.ui.utils.network.HeaderInterceptor
import com.example.nutritiontracker.ui.utils.network.ResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Named("RefreshRetrofit")
    @Singleton
    @Provides
    fun provideRefreshRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthService(
        @Named("RefreshRetrofit") retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun httpClient(
        localTokenDataSource: LocalTokenDataSource,
        authService: AuthService
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(localTokenDataSource))
            .addInterceptor(ResponseInterceptor(localTokenDataSource, authService))
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkClient(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
