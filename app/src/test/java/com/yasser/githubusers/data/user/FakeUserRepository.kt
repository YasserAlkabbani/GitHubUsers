package com.yasser.githubusers.data.user

import androidx.paging.*
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.data.user.model.useres.UserRemote
import com.yasser.githubusers.manager.RequestResult
import com.yasser.githubusers.utils.extention.asTimeZoneDate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FakeUserRepository :UserRepository {

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

    fun insertFollowersUsersWithFollowingUserWithRemoteKey(
        userName:String, followingUsers:List<UserEntity>?, followersUsers:List<UserEntity>?, userRemoteKey: UserRemoteKeyEntity?
    ){
        fun insertOrReplaceFollowingCrossRef(userName:String, following:List<UserEntity>){
            val followUserCrossRef=
                following.map { FollowUserCrossRef(userName = userName, followingUserName = it.userName) }
            followUserCrossRefLocalDataSource.addAll(followUserCrossRef)
        }
        fun insertOrReplaceFollowersCrossRef(userName: String, followers:List<UserEntity>){
            val followUserCrossRef=
                followers.map { FollowUserCrossRef(userName =it.userName, followingUserName = userName) }
            followUserCrossRefLocalDataSource.addAll(followUserCrossRef)
        }
        fun insertOrReplaceUser(users:List<UserEntity>){
            userLocalDataSource.update { it.toMutableList().apply { addAll(users) } }
        }
        fun insertOrReplaceRemoteKey(users: List<UserEntity>, userRemoteKey: UserRemoteKeyEntity?){
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
    }

    override fun getFollowingAsPagingSource(userName: String)= Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = {user,page,perPage->
                userRemoteDataSource.filter { (it.id?:0) in 70 .. 90 }
            },
            insertUsers = {following,userRemoteKey->
                insertFollowersUsersWithFollowingUserWithRemoteKey(userName,following,null,userRemoteKey)
            },
            getUserRemoteKeyByUserName = {user->userRemoteKeyLocaleDataSource.firstOrNull { it.userName==user }}
        ),
        pagingSourceFactory = {
            UserPagingSource {
                val myFollowingUserName=followUserCrossRefLocalDataSource.filter { it.userName==userName }.map { it.followingUserName }
                val myFollowing=userLocalDataSource.value.filter { myFollowingUserName.contains(it.userName) }
                myFollowing
            }
        },
    ).flow.map { it.map { it.asDomain() } }

    override fun getFollowersAsPagingSource(userName: String)=Pager(
        config = PagingConfig(10),
        remoteMediator = UserRemoteMediator(
            currentUserName = userName,
            getUsers = {user,page,perPage->
                userRemoteDataSource.filter { (it.id?:0) in 40 .. 60 }
            },
            insertUsers = {followers,userRemoteKey->
                insertFollowersUsersWithFollowingUserWithRemoteKey(userName,null,followers,userRemoteKey)
            },
            getUserRemoteKeyByUserName = {user->userRemoteKeyLocaleDataSource.firstOrNull { it.userName==user }}
        ),
        pagingSourceFactory = {
            UserPagingSource {
                val myFollowerUserName=followUserCrossRefLocalDataSource.filter { it.followingUserName==userName }.map { it.userName }
                val myFollower=userLocalDataSource.value.filter { myFollowerUserName.contains(it.userName) }
                myFollower
            }
        },
    ).flow.map { it.map { it.asDomain() } }

}

class UserPagingSource(val gerUsers:suspend (Int?)->List<UserEntity>):
    PagingSource<Int, UserEntity>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        return LoadResult.Page(
            data = gerUsers(params.key),
            prevKey = null, // Only paging forward.
            nextKey = (params.key?:0)+1
        )
    }
    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? =null
}
