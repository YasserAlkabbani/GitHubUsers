package com.yasser.githubusers.data.user_remote_key.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.GithubDatabase
import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyDao
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserRemoteKeyReadWriteTest {

    private lateinit var userRemoteKeyDao: UserRemoteKeyDao
    private lateinit var githubDatabase: GithubDatabase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        githubDatabase = Room.inMemoryDatabaseBuilder(context, GithubDatabase::class.java).build()
        userRemoteKeyDao = githubDatabase.userRemoteKeyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() { githubDatabase.close() }

    @Test
    @Throws(Exception::class)
    fun writeAndReadUserRemoteKey() = runTest{
        val userRemoteKey=UserRemoteKeyEntity("Yasser",2)
        userRemoteKeyDao.insertOrReplaceUserRemoteKey(userRemoteKey)
        val userByName=userRemoteKeyDao.findUserRemoteKeyByUserName(userRemoteKey.userName)
        assertEquals(userRemoteKey.userName, userByName?.userName)
        assertEquals(userRemoteKey.nextKey,userByName?.nextKey)
    }

}