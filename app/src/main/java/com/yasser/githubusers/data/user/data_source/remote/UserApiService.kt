package com.yasser.githubusers.data.user.data_source.remote

import com.yasser.githubusers.data.user.model.useres.UserRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("users/{userName}")
    suspend fun findUserByUserName(
        @Path("userName")userName:String,
    ):UserRemote?

    @GET("users/{userName}/following")
    suspend fun getFollowing(
        @Path("userName")userName:String,
        @Query("page")page:Int?,
        @Query("per_page")perPage:Int,
    ):List<UserRemote>?

    @GET("users/{userName}/followers")
    suspend fun getFollowers(
        @Path("userName")userName:String,
        @Query("page")page:Int?,
        @Query("per_page")perPage:Int,
    ):List<UserRemote>?


}