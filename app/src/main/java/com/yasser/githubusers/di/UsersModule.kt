package com.yasser.githubusers.di

import com.yasser.githubusers.data.remote_key.local.DefaultUserRemoteKeyLocaleDataSource
import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyLocaleDataSource
import com.yasser.githubusers.data.user.DefaultUserRepository
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.data.user.data_source.local.DefaultUserLocalDataSource
import com.yasser.githubusers.data.user.data_source.local.UserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.DefaultUserRemoteDataSource
import com.yasser.githubusers.data.user.data_source.remote.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersModule {

    @Binds
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository

    @Binds
    abstract fun bindUserLocalSource(defaultUserLocalDataSource: DefaultUserLocalDataSource):UserLocalDataSource

    @Binds
    abstract fun bindUserRemoteDataSource(defaultUserRemoteDataSource: DefaultUserRemoteDataSource):UserRemoteDataSource

    @Binds
    abstract fun bindRemoteKeyLocaleDataSource(defaultRemoteKeyLocaleDataSource:DefaultUserRemoteKeyLocaleDataSource):UserRemoteKeyLocaleDataSource

}