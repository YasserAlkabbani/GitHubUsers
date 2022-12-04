package com.yasser.githubusers.di

import android.app.Application
import androidx.room.Room
import com.yasser.githubusers.data.GithubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object ProvideDatabase {

    @Singleton @Provides
    fun provideGitHubUsersDatabase(application: Application)=Room.databaseBuilder(
        application,
        GithubDatabase::class.java, "database-github"
    ).build()

    @Provides
    fun provideUserDao(githubDatabase: GithubDatabase)=githubDatabase.userDao()

    @Provides
    fun provideUserRemoteKeyDao(githubDatabase: GithubDatabase)=githubDatabase.userRemoteKeyDao()

}