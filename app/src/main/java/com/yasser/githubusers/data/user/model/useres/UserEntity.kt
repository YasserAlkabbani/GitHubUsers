package com.yasser.githubusers.data.user.model.useres

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yasser.githubusers.utils.extention.asDate

@Entity(tableName = "following_user_cross_ref",primaryKeys = ["user_name","following_user_name"])
data class FollowUserCrossRef(
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "following_user_name") val followingUserName:String
)

@Entity(tableName = "users_table")
data class UserEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "gravatar_id") val gravatarId: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "html_url") val htmlUrl:String,
    @ColumnInfo(name = "followers_url") val followersUrl:String,
    @ColumnInfo(name = "following_url") val followingUrl:String,
    @ColumnInfo(name = "gists_url") val gistsUrl: String,
    @ColumnInfo(name = "starred_url") val starredUrl: String,
    @ColumnInfo(name = "subscriptions_url") val subscriptionsUrl: String,
    @ColumnInfo(name = "organizations_url") val organizationsUrl: String,
    @ColumnInfo(name = "repos_url") val reposUrl: String,
    @ColumnInfo(name = "events_url") val eventsUrl:String,
    @ColumnInfo(name = "received_events_url") val receivedEventsUrl:String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "site_admin") val siteAdmin: Boolean,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "company") val company : String,
    @ColumnInfo(name = "blog") val blog : String,
    @ColumnInfo(name = "location") val location : String,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "hireable") val hireable : Boolean,
    @ColumnInfo(name = "bio") val bio : String,
    @ColumnInfo(name = "twitter_username") val twitterUsername : String,
    @ColumnInfo(name = "public_repos") val publicRepos : Int,
    @ColumnInfo(name = "public_gists") val publicGists : Int,
    @ColumnInfo(name = "followers") val followers : Int,
    @ColumnInfo(name = "following") val following : Int,
    @ColumnInfo(name = "created_at") val createdAt : Long,
    @ColumnInfo(name = "updated_at") val updatedAt : Long
) {
    fun asDomain()=UserDomain(
        id = id, userName= userName, nodeId=nodeId,
        avatarUrl=avatarUrl,gravatarId=gravatarId,
        url=url, htmlUrl=htmlUrl, followersUrl=followersUrl,
        followingUrl=followingUrl, gistsUrl=gistsUrl, starredUrl=starredUrl,
        subscriptionsUrl=subscriptionsUrl, organizationsUrl=organizationsUrl,
        reposUrl=reposUrl, eventsUrl=eventsUrl, receivedEventsUrl=receivedEventsUrl,
        type=type, siteAdmin=siteAdmin, name=name, company=company, blog=blog,
        location=location, email=email, hireable=hireable, bio=bio, twitterUsername=twitterUsername,
        publicRepos=publicRepos, publicGists=publicGists, followers=followers, following=following,
        createdAt=createdAt.asDate(), updatedAt=updatedAt.asDate(),
    )
}