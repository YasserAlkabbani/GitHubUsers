package com.yasser.githubusers.data.user.data_source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.GithubDatabase
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserEntityReadWriteTest {

    private lateinit var userDao: UserDao
    private lateinit var githubDatabase: GithubDatabase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        githubDatabase = Room.inMemoryDatabaseBuilder(context, GithubDatabase::class.java).build()
        userDao = githubDatabase.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() { githubDatabase.close() }

    @Test
    @Throws(Exception::class)
    fun writeAndReadUser() = runTest{
        val it=0
        val user= UserEntity(
            id=it.toLong(), userName = "USER_$it", nodeId = "NODE_$it",avatarUrl ="AVATAR_$it",
            gravatarId = "GRAVATAR_$it", name = "NAME_$it", email = "EMAIL@EMAIL.COM_$it",
            type = "TYPE_$it", bio = "BIO_$it", blog = "BLOG_$it", company = "COMPANY_$it",
            eventsUrl = "", followers = it, following = it,
            followersUrl = "", followingUrl = "", gistsUrl = "",
            hireable = listOf(true,false).random(), htmlUrl = "", location = "LOCATION_$it",
            organizationsUrl = "", publicGists = it, publicRepos = it,
            reposUrl = "", siteAdmin = listOf(true,false).random(), starredUrl = "",
            url = "", receivedEventsUrl = "", subscriptionsUrl = "",
            twitterUsername ="TWITTER_USER_NAME_$it",
            updatedAt = System.currentTimeMillis(), createdAt = System.currentTimeMillis()
        )
        userDao.insertOrReplaceUsers(user)
        val userByName=userDao.findUserByUserNameAsFlow(user.userName).first()
        assertEquals(user.id, userByName?.id)
        assertEquals(user.userName,userByName?.userName)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadFollowUserCrossRef() = runTest{
        val followUserCrossRef:FollowUserCrossRef=FollowUserCrossRef(
            "Yasser","ALkabbani"
        )
        userDao.insertFollowers(followUserCrossRef)
        val followUserCrossRefByUserName=userDao.getFollowUserCrossRefByUserName("Yasser")

        assertEquals(followUserCrossRef.userName,followUserCrossRefByUserName?.userName)
        assertEquals(followUserCrossRef.followingUserName,followUserCrossRefByUserName?.followingUserName)
    }


}