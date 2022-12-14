package com.yasser.githubusers.data.user

import androidx.paging.*
import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyLocaleDataSource
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.data_source.local.UserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.UserRemoteDataSource
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.manager.RequestResult
import com.yasser.githubusers.manager.requestWithResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteKeyLocaleDataSource:UserRemoteKeyLocaleDataSource
) : UserRepository {

    override suspend fun refreshUserByUserName(userName: String)= requestWithResult {
        userRemoteDataSource.findUserByUserName(userName)?.asEntity()?.let {user->
            userLocalDataSource.insertOrReplaceUsers(user)
        }
    }
    override fun findUserByUserNameAsFlow(userName:String)= userLocalDataSource.findUserByUserNameAsFlow(userName)
            .map { it?.asDomain() }.flowOn(Dispatchers.Default)

    suspend fun insertFollowersUsersWithFollowingUserWithRemoteKey(
        userName:String, followingUsers:List<UserEntity>?, followersUsers:List<UserEntity>?, userRemoteKey: UserRemoteKeyEntity?
    ){
        suspend fun insertOrReplaceFollowingCrossRef(userName:String, following:List<UserEntity>){
            val followUserCrossRef=
                following.map { FollowUserCrossRef(userName = userName, followingUserName = it.userName) }
            userLocalDataSource.insertFollowers(*followUserCrossRef.toTypedArray())
        }
        suspend fun insertOrReplaceFollowersCrossRef(userName: String, followers:List<UserEntity>){
            val followUserCrossRef=
                followers.map { FollowUserCrossRef(userName =it.userName, followingUserName = userName) }
            userLocalDataSource.insertFollowers(*followUserCrossRef.toTypedArray())
        }
        suspend fun insertOrReplaceUser(users:List<UserEntity>){
            userLocalDataSource.insertOrReplaceUsers(*users.toTypedArray())
        }
        suspend fun insertOrReplaceRemoteKey(users: List<UserEntity>, userRemoteKey: UserRemoteKeyEntity?){
            users.lastOrNull()?.userName?.let {
                userRemoteKeyLocaleDataSource.insertOrReplaceUserRemoteKey(UserRemoteKeyEntity(it,userRemoteKey?.nextKey))
            }
        }

        followingUsers?.let {
            insertOrReplaceFollowingCrossRef(userName,it)
            insertOrReplaceUser(it)
            insertOrReplaceRemoteKey(it,userRemoteKey)
        }
        followersUsers?.let {
            insertOrReplaceFollowersCrossRef(userName,it)
            insertOrReplaceUser(it)
            insertOrReplaceRemoteKey(it,userRemoteKey)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFollowingAsPagingSource(userName: String)=Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = userRemoteDataSource::getFollowing,
            insertUsers = {following,userRemoteKey->
                insertFollowersUsersWithFollowingUserWithRemoteKey(userName,following,null,userRemoteKey)
            },
            getUserRemoteKeyByUserName = userRemoteKeyLocaleDataSource::getRemoteKeyByUserName
        ),
        pagingSourceFactory = {userLocalDataSource.getUserFollowingAsPagingSource(userName)},
    ).flow.map { it.map { it.asDomain() } }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFollowersAsPagingSource(userName: String)=Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = userRemoteDataSource::getFollowers,
            insertUsers = {followers,userRemoteKey->
                insertFollowersUsersWithFollowingUserWithRemoteKey(userName,null,followers,userRemoteKey)
            },
            getUserRemoteKeyByUserName = userRemoteKeyLocaleDataSource::getRemoteKeyByUserName
        ),
        pagingSourceFactory = {userLocalDataSource.getUserFollowersAsPagingSource(userName)},
    ).flow.map { it.map { it.asDomain() } }

}

interface UserRepository {

    suspend fun refreshUserByUserName(userName: String): RequestResult<Unit?>

    fun findUserByUserNameAsFlow(userName: String): Flow<UserDomain?>

    fun getFollowingAsPagingSource(userName: String): Flow<PagingData<UserDomain>>

    fun getFollowersAsPagingSource(userName: String): Flow<PagingData<UserDomain>>

}