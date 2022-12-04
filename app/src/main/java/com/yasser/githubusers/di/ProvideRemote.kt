package com.yasser.githubusers.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yasser.githubusers.data.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL="https://api.github.com/"

@Module @InstallIn(SingletonComponent::class)
object ProvideRemote {


    @Singleton @Provides
    fun provideMoshi():Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Singleton @Provides
    fun provideOkHttp()=OkHttpClient.Builder()
        .callTimeout(60,TimeUnit.SECONDS)
        .connectTimeout(60,TimeUnit.SECONDS)
        .readTimeout(60,TimeUnit.SECONDS)
        .writeTimeout(60,TimeUnit.SECONDS)
        .build()

    @Singleton @Provides
    fun provideRetrofit(moshi: Moshi,okHttpClient: OkHttpClient):Retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Singleton @Provides
    fun provideGitHubApiService(retrofit: Retrofit):GitHubApiService=retrofit.create(GitHubApiService::class.java)

}