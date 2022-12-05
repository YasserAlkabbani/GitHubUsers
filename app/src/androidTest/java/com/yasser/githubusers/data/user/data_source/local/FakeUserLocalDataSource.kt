package com.yasser.githubusers.data.user.data_source.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FakeUserLocalDataSource @Inject constructor() :UserLocalDataSource {

    private val userList:MutableStateFlow<List<UserEntity>> = MutableStateFlow(listOf())
    private val followUserCrossRefList:MutableStateFlow<List<FollowUserCrossRef>> = MutableStateFlow(listOf())

    override suspend fun insertOrReplaceUsers(vararg users: UserEntity) {
        userList.update { it.toMutableList().apply { addAll(users.toList()) }
            .distinctBy { it.id }.distinctBy { it.userName } }
    }

    override fun findUserByUserNameAsFlow(userNameSearchKey: String): Flow<UserEntity?> =userList.map {
        it.firstOrNull { it.userName==userNameSearchKey }
    }

    override suspend fun insertFollowers(vararg followUserCrossRef: FollowUserCrossRef) {
        followUserCrossRefList.update { it.toMutableList().apply { addAll(followUserCrossRef.toList()) } }
    }

    override fun getUserFollowingAsPagingSource(userName: String): PagingSource<Int, UserEntity> =
        UserPagingSource {
            val myFollowingUserName=followUserCrossRefList.value.filter { it.userName==userName }.map { it.followingUserName }
            val myFollowing=userList.value.filter { myFollowingUserName.contains(it.userName) }
            myFollowing
        }

    override fun getUserFollowersAsPagingSource(userName: String): PagingSource<Int, UserEntity> =
        UserPagingSource {
            val myFollowerUserName=followUserCrossRefList.value.filter { it.followingUserName==userName }.map { it.userName }
            val myFollower=userList.value.filter { myFollowerUserName.contains(it.userName) }
            myFollower
        }

}

class UserPagingSource(val gerUsers:suspend (Int?)->List<UserEntity>):PagingSource<Int,UserEntity>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        return LoadResult.Page(
            data = gerUsers(params.key),
            prevKey = null, // Only paging forward.
            nextKey = (params.key?:0)+1
        )
    }
    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? =null
}