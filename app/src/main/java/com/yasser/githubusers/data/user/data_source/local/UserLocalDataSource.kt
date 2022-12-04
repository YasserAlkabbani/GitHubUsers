package com.yasser.githubusers.data.user.data_source.local

import androidx.paging.PagingSource
import com.yasser.githubusers.data.GithubDatabase
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserLocalDataSource @Inject constructor(
    private val githubDatabase: GithubDatabase, private val userDao: UserDao
) {

    suspend fun insertOrReplaceUsers(vararg users: UserEntity) = userDao.insertOrReplaceUsers(*users)

    fun findUserByUserNameAsFlow(userNameSearchKey:String): Flow<UserEntity?> =
        userDao.findUserByUserNameAsFlow(userNameSearchKey)

    fun getUsersAsPagingSource(): PagingSource<Int, UserEntity> =
        userDao.getUsersAsPagingSource()

    suspend fun insertFollowers(vararg followUserCrossRef: FollowUserCrossRef) =
        userDao.insertFollowers(*followUserCrossRef)

    fun getUserFollowersAsPagingSource(userName:String):PagingSource<Int,UserEntity> =
        userDao.getUserFollowersAsPagingSource(userName)

    fun getUserFollowingAsPagingSource(userName:String):PagingSource<Int,UserEntity> =
        userDao.getUserFollowingAsPagingSource(userName)
}