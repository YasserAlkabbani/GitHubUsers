package com.yasser.githubusers.data.user.data_source.local

import androidx.paging.PagingSource
import com.yasser.githubusers.data.GithubDatabase
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DefaultUserLocalDataSource @Inject constructor(private val userDao: UserDao) : UserLocalDataSource {

    override suspend fun insertOrReplaceUsers(vararg users: UserEntity) = userDao.insertOrReplaceUsers(*users)

    override fun findUserByUserNameAsFlow(userNameSearchKey:String): Flow<UserEntity?> =
        userDao.findUserByUserNameAsFlow(userNameSearchKey)

    override suspend fun insertFollowers(vararg followUserCrossRef: FollowUserCrossRef) =
        userDao.insertFollowers(*followUserCrossRef)

    override fun getUserFollowersAsPagingSource(userName:String):PagingSource<Int,UserEntity> =
        userDao.getUserFollowersAsPagingSource(userName)

    override fun getUserFollowingAsPagingSource(userName:String):PagingSource<Int,UserEntity> =
        userDao.getUserFollowingAsPagingSource(userName)

}

interface UserLocalDataSource {

    suspend fun insertOrReplaceUsers(vararg users: UserEntity)
    fun findUserByUserNameAsFlow(userNameSearchKey: String): Flow<UserEntity?>

    suspend fun insertFollowers(vararg followUserCrossRef: FollowUserCrossRef)
    fun getUserFollowersAsPagingSource(userName: String): PagingSource<Int, UserEntity>
    fun getUserFollowingAsPagingSource(userName: String): PagingSource<Int, UserEntity>

}