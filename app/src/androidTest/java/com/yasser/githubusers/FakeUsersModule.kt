package com.yasser.githubusers

import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyLocaleDataSource
import com.yasser.githubusers.data.user.FakeUserRepository
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.data.user.data_source.local.FakeUserLocalDataSource
import com.yasser.githubusers.data.user.data_source.local.UserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.FakeUserRemoteDataSource
import com.yasser.githubusers.data.user.data_source.remote.UserRemoteDataSource
import com.yasser.githubusers.data.user_remote_key.local.FakeUserRemoteKeyLocalDataSource
import com.yasser.githubusers.di.UsersModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [UsersModule::class])

abstract class FakeUsersModule {

    @Binds
    abstract fun bindUserRepository(fakeUserRepository: FakeUserRepository): UserRepository

    @Binds
    abstract fun bindUserLocalSource(fakeUserLocalDataSource: FakeUserLocalDataSource): UserLocalDataSource

    @Binds
    abstract fun bindUserRemoteDataSource(fakeUserRemoteDataSource: FakeUserRemoteDataSource): UserRemoteDataSource

    @Binds
    abstract fun bindRemoteKeyLocaleDataSource(fakeUserRemoteKeyLocalDataSource: FakeUserRemoteKeyLocalDataSource): UserRemoteKeyLocaleDataSource

}