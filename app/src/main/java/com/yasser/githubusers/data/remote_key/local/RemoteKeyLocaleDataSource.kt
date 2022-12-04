package com.yasser.githubusers.data.remote_key.local

import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import javax.inject.Inject

class RemoteKeyLocaleDataSource @Inject constructor(private val userRemoteKeyDao: UserRemoteKeyDao) {

    suspend fun insertOrReplaceUserRemoteKey(remoteKey: UserRemoteKeyEntity)=userRemoteKeyDao.insertOrReplaceUserRemoteKey(remoteKey)

    suspend fun getRemoteKeyByUserName(userName: String): UserRemoteKeyEntity?=userRemoteKeyDao.getUserRemoteKeyByUserName(userName)

    suspend fun clearUserRemoteKey()=userRemoteKeyDao.clearUserRemoteKey()
}