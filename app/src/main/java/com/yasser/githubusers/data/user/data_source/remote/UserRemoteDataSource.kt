package com.yasser.githubusers.data.user.data_source.remote

import com.yasser.githubusers.data.GitHubApiService
import com.yasser.githubusers.data.user.model.useres.UserRemote
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(private val gitHubApiService: GitHubApiService){

    suspend fun findUserByUserName(userName:String)=
        gitHubApiService.findUserByUserName(userName=userName)

    suspend fun getFollowing(userName:String, page:Int?, perPage:Int):List<UserRemote> =
        gitHubApiService.getFollowing(userName=userName, page = page,perPage=perPage).orEmpty()

    suspend fun getFollowers(userName:String, page:Int?, perPage:Int):List<UserRemote> =
        gitHubApiService.getFollowers(userName=userName, page = page,perPage=perPage).orEmpty()
}