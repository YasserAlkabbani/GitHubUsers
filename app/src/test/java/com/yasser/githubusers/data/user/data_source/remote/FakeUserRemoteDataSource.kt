package com.yasser.githubusers.data.user.data_source.remote

import com.yasser.githubusers.data.user.model.useres.UserRemote
import com.yasser.githubusers.utils.extention.asTimeZoneDate

class FakeUserRemoteDataSource:UserRemoteDataSource {

    private val users:List<UserRemote> = buildList {
        repeat(100){
            val userRemote:UserRemote=UserRemote(
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

    override suspend fun findUserByUserName(userName: String): UserRemote? =
        users.firstOrNull { it.login==userName }

    override suspend fun getFollowing(userName: String, page: Int?, perPage: Int): List<UserRemote> =
        users.filter { (it.id?:0) in 70 .. 90 }

    override suspend fun getFollowers(userName: String, page: Int?, perPage: Int): List<UserRemote> =
        users.filter { (it.id?:0) in 40 .. 60 }

}