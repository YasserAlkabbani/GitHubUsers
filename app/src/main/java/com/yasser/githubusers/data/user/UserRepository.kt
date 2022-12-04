package com.yasser.githubusers.data.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.yasser.githubusers.data.remote_key.local.RemoteKeyLocaleDataSource
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.data_source.local.UserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.UserRemoteDataSource
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.manager.requestWithResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val remoteKeyLocaleDataSource:RemoteKeyLocaleDataSource
) {

    suspend fun refreshUserByUserName(userName: String)= requestWithResult {
        userRemoteDataSource.findUserByUserName(userName)?.asEntity()?.let {user->
            userLocalDataSource.insertOrReplaceUsers(user)
        }
    }
    fun findUserByUserNameAsFlow(userName:String)= userLocalDataSource.findUserByUserNameAsFlow(userName)
            .map { it?.asDomain() }.flowOn(Dispatchers.Default)

    private suspend fun insertOrReplaceUser(following:List<UserEntity>){
        userLocalDataSource.insertOrReplaceUsers(*following.toTypedArray())
    }
    private suspend fun insertOrReplaceFollowing(userName:String,following:List<UserEntity>){
        val followUserCrossRef=
            following.map { FollowUserCrossRef(userName = userName, followingUserName = it.userName) }
        userLocalDataSource.insertFollowers(*followUserCrossRef.toTypedArray())
    }
    private suspend fun insertOrReplaceFollowers(userName: String,followers:List<UserEntity>){
        val followUserCrossRef=
            followers.map { FollowUserCrossRef(userName =it.userName, followingUserName = userName) }
        userLocalDataSource.insertFollowers(*followUserCrossRef.toTypedArray())
    }
    private suspend fun insertOrReplaceRemoteKet(users:List<UserEntity>,userRemoteKey:UserRemoteKeyEntity?){
        users.lastOrNull()?.userName?.let {
            remoteKeyLocaleDataSource.insertOrReplaceUserRemoteKey(UserRemoteKeyEntity(it,userRemoteKey?.nextKey))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getFollowingAsPagingSource(userName: String)=Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = userRemoteDataSource::getFollowing,
            insertUsers = {following,userRemoteKey->
                insertOrReplaceUser(following)
                insertOrReplaceFollowing(userName,following)
                insertOrReplaceRemoteKet(following,userRemoteKey)
            },
            getUserRemoteKeyByUserName = remoteKeyLocaleDataSource::getRemoteKeyByUserName
        ),
        pagingSourceFactory = {userLocalDataSource.getUserFollowingAsPagingSource(userName)},
    ).flow.map { it.map { it.asDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    fun getFollowersAsPagingSource(userName: String)=Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = userRemoteDataSource::getFollowers,
            insertUsers = {followers,userRemoteKey->
                insertOrReplaceUser(followers)
                insertOrReplaceFollowers(userName,followers)
                insertOrReplaceRemoteKet(followers,userRemoteKey)
            },
            getUserRemoteKeyByUserName = remoteKeyLocaleDataSource::getRemoteKeyByUserName
        ),
        pagingSourceFactory = {userLocalDataSource.getUserFollowersAsPagingSource(userName)},
    ).flow.map { it.map { it.asDomain() } }

}