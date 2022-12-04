package com.yasser.githubusers.data.user.model.useres

import com.squareup.moshi.Json
import com.yasser.githubusers.utils.extention.dateAsTimeStamp

data class UserRemote(
    @Json(name = "id") val id: Long?,
    @Json(name = "login") val login: String?,
    @Json(name = "node_id") val nodeId: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "gravatar_id") val gravatarId: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "html_url") val htmlUrl:String?,
    @Json(name = "followers_url") val followersUrl:String?,
    @Json(name = "following_url") val followingUrl:String?,
    @Json(name = "gists_url") val gistsUrl: String?,
    @Json(name = "starred_url") val starredUrl: String?,
    @Json(name = "subscriptions_url") val subscriptionsUrl: String?,
    @Json(name = "organizations_url") val organizationsUrl: String?,
    @Json(name = "repos_url") val reposUrl: String?,
    @Json(name = "events_url") val eventsUrl:String?,
    @Json(name = "received_events_url") val receivedEventsUrl:String?,
    @Json(name = "type") val type: String?,
    @Json(name = "site_admin") val siteAdmin: Boolean?,
    @Json(name = "name") val name : String?,
    @Json(name = "company") val company : String?,
    @Json(name = "blog") val blog : String?,
    @Json(name = "location") val location : String?,
    @Json(name = "email") val email : String?,
    @Json(name = "hireable") val hireable : Boolean?,
    @Json(name = "bio") val bio : String?,
    @Json(name = "twitter_username") val twitterUsername : String?,
    @Json(name = "public_repos") val publicRepos : Int?,
    @Json(name = "public_gists") val publicGists : Int?,
    @Json(name = "followers") val followers : Int?,
    @Json(name = "following") val following : Int?,
    @Json(name = "created_at") val createdAt : String?,
    @Json(name = "updated_at") val updatedAt : String?
){
    fun asEntity()=id?.let {
        UserEntity(
            id = id, userName=login.orEmpty(), nodeId=nodeId.orEmpty(),
            avatarUrl=avatarUrl.orEmpty(),gravatarId=gravatarId.orEmpty(),
            url=url.orEmpty(), htmlUrl=htmlUrl.orEmpty(), followersUrl=followersUrl.orEmpty(),
            followingUrl=followingUrl.orEmpty(), gistsUrl=gistsUrl.orEmpty(), starredUrl=starredUrl.orEmpty(),
            subscriptionsUrl=subscriptionsUrl.orEmpty(), organizationsUrl=organizationsUrl.orEmpty(),
            reposUrl=reposUrl.orEmpty(), eventsUrl=eventsUrl.orEmpty(), receivedEventsUrl=receivedEventsUrl.orEmpty(),
            type=type.orEmpty(), siteAdmin=siteAdmin?:false,
            name=name.orEmpty(), company=company.orEmpty(), blog=blog.orEmpty(), location=location.orEmpty(),
            email=email.orEmpty(), hireable=hireable?:false, bio=bio.orEmpty(), twitterUsername=twitterUsername.orEmpty(),
            publicRepos=publicRepos?:0, publicGists=publicGists?:0, followers=followers?:0,
            following=following?:0, createdAt=createdAt?.dateAsTimeStamp()?:0, updatedAt=updatedAt?.dateAsTimeStamp()?:0,
        )
    }
}