package com.yasser.githubusers.data.user.data_source.remote

import com.yasser.githubusers.data.GitHubApiService
import com.yasser.githubusers.data.user.model.useres.UserRemote
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class DefaultUserRemoteDataSource @Inject constructor(private val gitHubApiService: GitHubApiService) : UserRemoteDataSource {

    override suspend fun findUserByUserName(userName:String): UserRemote? {
        delay(1000) ///// DELAY BEFORE REQUEST FOR AVOID TOO MANY REQUEST
        return gitHubApiService.findUserByUserName(userName=userName)
    }

    override suspend fun getFollowing(userName:String, page:Int?, perPage:Int):List<UserRemote> =
        gitHubApiService.getFollowing(userName=userName, page = page,perPage=perPage).orEmpty()

    override suspend fun getFollowers(userName:String, page:Int?, perPage:Int):List<UserRemote> =
        gitHubApiService.getFollowers(userName=userName, page = page,perPage=perPage).orEmpty()

}

interface UserRemoteDataSource {

    suspend fun findUserByUserName(userName: String): UserRemote?
    suspend fun getFollowing(userName: String, page: Int?, perPage: Int): List<UserRemote>
    suspend fun getFollowers(userName: String, page: Int?, perPage: Int): List<UserRemote>

}