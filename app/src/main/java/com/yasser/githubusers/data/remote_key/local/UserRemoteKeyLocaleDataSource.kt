package com.yasser.githubusers.data.remote_key.local

import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import javax.inject.Inject

class DefaultUserRemoteKeyLocaleDataSource @Inject constructor(private val userRemoteKeyDao: UserRemoteKeyDao) :
    UserRemoteKeyLocaleDataSource {

    override suspend fun insertOrReplaceUserRemoteKey(remoteKey: UserRemoteKeyEntity)=
        userRemoteKeyDao.insertOrReplaceUserRemoteKey(remoteKey)

    override suspend fun getRemoteKeyByUserName(userName: String): UserRemoteKeyEntity?=
        userRemoteKeyDao.findUserRemoteKeyByUserName(userName)

    override suspend fun clearUserRemoteKey()=userRemoteKeyDao.clearUserRemoteKey()

}

interface UserRemoteKeyLocaleDataSource {
    suspend fun insertOrReplaceUserRemoteKey(remoteKey: UserRemoteKeyEntity)
    suspend fun getRemoteKeyByUserName(userName: String): UserRemoteKeyEntity?
    suspend fun clearUserRemoteKey()
}