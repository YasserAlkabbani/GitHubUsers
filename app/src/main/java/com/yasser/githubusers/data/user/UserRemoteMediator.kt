package com.yasser.githubusers.data.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.data.user.model.useres.UserRemote
import com.yasser.githubusers.manager.RequestResult
import com.yasser.githubusers.manager.requestWithResult
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val currentUserName:String,
    private val getUsers:suspend (userName:String, page:Int?, perPage:Int)->List<UserRemote>,
    private val insertUsers:suspend (List<UserEntity>,UserRemoteKeyEntity?)->Unit,
    private val getUserRemoteKeyByUserName:suspend (String)->UserRemoteKeyEntity?
): RemoteMediator<Int, UserEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserEntity>): MediatorResult {

        val lastUserName= when(loadType){
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> state.lastItemOrNull()?.userName
        }

        val currentRemoteKey=lastUserName?.let { getUserRemoteKeyByUserName(it) }?.nextKey?:1

        val userResult= requestWithResult { getUsers(currentUserName,currentRemoteKey,state.config.pageSize) }

        return when(userResult){
            is RequestResult.Success -> {
                val users=userResult.result.mapNotNull { it.asEntity() }
                insertUsers(users,users.lastOrNull()?.let {UserRemoteKeyEntity(it.userName,currentRemoteKey+1)})
                MediatorResult.Success(users.isEmpty())
            }
            is RequestResult.Failure -> {
                MediatorResult.Error(userResult.throwable)
            }
        }

    }

//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.SKIP_INITIAL_REFRESH
//    }
}