package com.berkayozdag.sausocial.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.berkayozdag.sausocial.BuildConfig
import com.berkayozdag.sausocial.common.Constants
import com.berkayozdag.sausocial.data.api.ApiService
import com.berkayozdag.sausocial.data.repository.AuthRepository
import com.berkayozdag.sausocial.data.repository.SocialAppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://sausocialmedia.com.tr/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSocialAppRepository(api: ApiService): SocialAppRepository {
        return SocialAppRepository(api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService): AuthRepository {
        return AuthRepository(api)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(application: Application): SharedPreferences {
        return application.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

}