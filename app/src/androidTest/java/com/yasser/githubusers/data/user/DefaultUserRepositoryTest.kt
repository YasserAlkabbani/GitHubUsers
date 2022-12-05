package com.yasser.githubusers.data.user

import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.user.data_source.local.FakeUserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.FakeUserRemoteDataSource
import com.yasser.githubusers.data.user_remote_key.local.FakeUserRemoteKeyLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultUserRepositoryTest {

    private lateinit var fakeUserLocalDataSource: FakeUserLocalDataSource
    private lateinit var fakeUserRemoteDataSource: FakeUserRemoteDataSource
    private lateinit var fakeUserRemoteKeyLocalDataSource: FakeUserRemoteKeyLocalDataSource
    private lateinit var defaultUserRepository: DefaultUserRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun initRepository(){
        fakeUserRemoteDataSource=FakeUserRemoteDataSource()
        fakeUserLocalDataSource=FakeUserLocalDataSource()
        fakeUserRemoteKeyLocalDataSource= FakeUserRemoteKeyLocalDataSource()
        defaultUserRepository=
            DefaultUserRepository(fakeUserRemoteDataSource,fakeUserLocalDataSource,fakeUserRemoteKeyLocalDataSource)
    }

    @Test
    fun refreshUserByUserName() = runTest{
        defaultUserRepository.refreshUserByUserName("USER_1")

        val user1=defaultUserRepository.findUserByUserNameAsFlow("USER_1").first()
        val user2=defaultUserRepository.findUserByUserNameAsFlow("USER_2").first()

        advanceUntilIdle()

        assertEquals(user1?.userName,"USER_1")
        assertNull(user2?.userName)
    }

    @Test
    fun findUserByUserNameAsFlow() = runTest{
        defaultUserRepository.refreshUserByUserName("USER_1")
        val user1AsFlow=defaultUserRepository.findUserByUserNameAsFlow("USER_1").first()
        val user2AsFlow=defaultUserRepository.findUserByUserNameAsFlow("USER_2").first()
        advanceUntilIdle()
        assertEquals(user1AsFlow?.userName,"USER_1")
        assertNull(user2AsFlow?.userName)
    }


    @Test
    fun getFollowingAsPagingSource() {

    }

    @Test
    fun getFollowersAsPagingSource() {
    }

}