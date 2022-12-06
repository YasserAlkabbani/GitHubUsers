package com.yasser.githubusers.data.user_remote_key.local

import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyLocaleDataSource
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import javax.inject.Inject

class FakeUserRemoteKeyLocalDataSource @Inject constructor(): UserRemoteKeyLocaleDataSource {

    private val userRemoteKeysList:MutableList<UserRemoteKeyEntity> = mutableListOf()

    override suspend fun insertOrReplaceUserRemoteKey(remoteKey: UserRemoteKeyEntity) {userRemoteKeysList.add(remoteKey)}

    override suspend fun getRemoteKeyByUserName(userName: String): UserRemoteKeyEntity? =
        userRemoteKeysList.firstOrNull { it.userName==userName }

    override suspend fun clearUserRemoteKey() { userRemoteKeysList.clear() }
}