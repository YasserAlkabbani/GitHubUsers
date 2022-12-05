package com.yasser.githubusers.data.user

import androidx.paging.PagingData
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.data.user.model.useres.UserRemote
import com.yasser.githubusers.manager.RequestResult
import com.yasser.githubusers.utils.extention.asTimeZoneDate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FakeUserRepository @Inject constructor():UserRepository {

    private val userRemoteDataSource:List<UserRemote> = buildList {
        repeat(100){
            val userRemote: UserRemote = UserRemote(
                id=it.toLong(), login = "USER_$it", nodeId = "NODE_$it",avatarUrl ="AVATAR_$it",
                gravatarId = "GRAVATAR_$it", name = "NAME_$it", email = "EMAIL@EMAIL.COM_$it",
                type = "TYPE_$it", bio = "BIO_$it", blog = "BLOG_$it", company = "COMPANY_$it",
                eventsUrl = "", followers = it, following = it,
                followersUrl = "", followingUrl = "", gistsUrl = "",
                hireable = listOf(true,false).random(), htmlUrl = "", location = "LOCATION_$it",
                organizationsUrl = "", publicGists = it, publicRepos = it,
                reposUrl = "", siteAdmin = listOf(true,false).random(), starredUrl = "",
                url = "", receivedEventsUrl = "", subscriptionsUrl = "",
                twitterUsername ="TWITTER_USER_NAME_$it",
                updatedAt = (it*100000).toLong().asTimeZoneDate(), createdAt = (it*100000).toLong().asTimeZoneDate()
            )
            add(userRemote)
        }
    }
    private val userLocalDataSource:MutableStateFlow<List<UserEntity>> = MutableStateFlow(listOf())
    private val followUserCrossRefLocalDataSource:MutableList<FollowUserCrossRef> = mutableListOf()
    private val userRemoteKeyLocaleDataSource:MutableList<UserRemoteKeyEntity> = mutableListOf()

    override suspend fun refreshUserByUserName(userName: String): RequestResult<Unit?> {
        userRemoteDataSource.firstOrNull{it.login==userName}?.asEntity()?.let { user->
            userLocalDataSource.update { it.toMutableList().apply { add(user) } }
        }
        return RequestResult.Success(Unit)
    }

    override fun findUserByUserNameAsFlow(userName: String): Flow<UserDomain?> {
        return userLocalDataSource.map { it.firstOrNull { it.userName==userName }?.asDomain() }
    }

    override fun getFollowingAsPagingSource(userName: String): Flow<PagingData<UserDomain>> {
        return flow { PagingData.from(userLocalDataSource.value) }
    }

    override fun getFollowersAsPagingSource(userName: String): Flow<PagingData<UserDomain>> {
        return flow { PagingData.from(userLocalDataSource.value) }
    }

}


/*
override suspend fun insertFollowersUsersWithFollowingUserWithRemoteKey(
    userName: String, followingUsers: List<UserEntity>?, followersUsers: List<UserEntity>?,
    userRemoteKey: UserRemoteKeyEntity?
) {
    suspend fun insertOrReplaceFollowingCrossRef(userName:String, following:List<UserEntity>){
        val followUserCrossRef=
            following.map { FollowUserCrossRef(userName = userName, followingUserName = it.userName) }
        followUserCrossRefLocalDataSource.addAll(followUserCrossRef)
        userLocalDataSource.update { it.toMutableList().apply {  } }
    }
    suspend fun insertOrReplaceFollowersCrossRef(userName: String, followers:List<UserEntity>){
        val followUserCrossRef=
            followers.map { FollowUserCrossRef(userName =it.userName, followingUserName = userName) }
        followUserCrossRefLocalDataSource.addAll(followUserCrossRef)
    }
    suspend fun insertOrReplaceUser(users:List<UserEntity>){
        userLocalDataSource.update { it.toMutableList().apply { addAll(users) } }
    }
    suspend fun insertOrReplaceRemoteKey(users: List<UserEntity>, userRemoteKey: UserRemoteKeyEntity?){
        users.lastOrNull()?.userName?.let {
            userRemoteKeyLocaleDataSource.add(UserRemoteKeyEntity(it,userRemoteKey?.nextKey))
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
}*/
