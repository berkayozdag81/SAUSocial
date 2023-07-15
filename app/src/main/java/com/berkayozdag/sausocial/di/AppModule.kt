package com.berkayozdag.sausocial.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.berkayozdag.sausocial.BuildConfig
import com.berkayozdag.sausocial.common.util.Constants
import com.berkayozdag.sausocial.data.api.SocialAppApi
import com.berkayozdag.sausocial.data.repository.AuthRepositoryImpl
import com.berkayozdag.sausocial.data.repository.SocialAppRepositoryImpl
import com.berkayozdag.sausocial.domain.repository.AuthRepository
import com.berkayozdag.sausocial.domain.repository.SocialAppRepository
import com.berkayozdag.sausocial.domain.usecase.AuthUseCases
import com.berkayozdag.sausocial.domain.usecase.Follow
import com.berkayozdag.sausocial.domain.usecase.GetFollowingPosts
import com.berkayozdag.sausocial.domain.usecase.GetPostById
import com.berkayozdag.sausocial.domain.usecase.GetPosts
import com.berkayozdag.sausocial.domain.usecase.GetUserById
import com.berkayozdag.sausocial.domain.usecase.GetUsers
import com.berkayozdag.sausocial.domain.usecase.Login
import com.berkayozdag.sausocial.domain.usecase.PostDelete
import com.berkayozdag.sausocial.domain.usecase.PostDisLike
import com.berkayozdag.sausocial.domain.usecase.PostLike
import com.berkayozdag.sausocial.domain.usecase.SendComment
import com.berkayozdag.sausocial.domain.usecase.SendPost
import com.berkayozdag.sausocial.domain.usecase.SocialAppUseCases
import com.berkayozdag.sausocial.domain.usecase.UnFollow
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
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SocialAppApi {
        return retrofit.create(SocialAppApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(application: Application): SharedPreferences {
        return application.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: SocialAppApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            login = Login(repository),
        )
    }

    @Provides
    @Singleton
    fun provideSocialAppRepository(api: SocialAppApi): SocialAppRepository {
        return SocialAppRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSocialAppUseCases(repository: SocialAppRepository): SocialAppUseCases {
        return SocialAppUseCases(
            getPosts = GetPosts(repository),
            getPostById = GetPostById(repository),
            sendPost = SendPost(repository),
            sendComment = SendComment(repository),
            getUserById = GetUserById(repository),
            getUsers = GetUsers(repository),
            follow = Follow(repository),
            unFollow = UnFollow(repository),
            postLike = PostLike(repository),
            postDisLike = PostDisLike(repository),
            getFollowingPosts = GetFollowingPosts(repository),
            postDelete = PostDelete(repository),
        )
    }

}