package com.yasser.githubusers.data.user

import androidx.paging.*
import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.user.data_source.local.FakeUserLocalDataSource
import com.yasser.githubusers.data.user.data_source.remote.FakeUserRemoteDataSource
import com.yasser.githubusers.data.user.model.useres.UserEntity
import com.yasser.githubusers.data.user_remote_key.FakeUserRemoteKeyLocalDataSource
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

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val userName:String="USER_1"
        defaultUserRepository.refreshUserByUserName(userName)
        val remoteMediator =
            UserRemoteMediator(
                currentUserName = userName,
                getUsers = fakeUserRemoteDataSource::getFollowing,
                getUserRemoteKeyByUserName = fakeUserRemoteKeyLocalDataSource::getRemoteKeyByUserName,
                insertUsers = {followingUser,userRemoteKey->
                    defaultUserRepository.insertFollowersUsersWithFollowingUserWithRemoteKey(
                        userName,followingUser,null,userRemoteKey
                    )
                }
            )
        val pagingState = PagingState<Int, UserEntity>(listOf(), null, PagingConfig(10), 10)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue (result is RemoteMediator.MediatorResult.Success)
        assertFalse ((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val userName:String="USER_1"
        defaultUserRepository.refreshUserByUserName(userName)
        val remoteMediator =
            UserRemoteMediator(
                currentUserName = userName,
                getUsers = {userN,page,perPage->listOf()},
                getUserRemoteKeyByUserName = fakeUserRemoteKeyLocalDataSource::getRemoteKeyByUserName,
                insertUsers = {followingUser,userRemoteKey->
                    defaultUserRepository.insertFollowersUsersWithFollowingUserWithRemoteKey(
                        userName,followingUser,null,userRemoteKey
                    )
                }
            )
        val pagingState = PagingState<Int, UserEntity>(listOf(), null, PagingConfig(10), 10)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue (result is RemoteMediator.MediatorResult.Success)
        assertTrue ((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        val userName:String="USER_1"
        defaultUserRepository.refreshUserByUserName(userName)
        val remoteMediator =
            UserRemoteMediator(
                currentUserName = userName,
                getUsers = {userN,page,perPage->throw IllegalStateException("TEST_MEDIATOR_EXCEPTION") },
                getUserRemoteKeyByUserName = fakeUserRemoteKeyLocalDataSource::getRemoteKeyByUserName,
                insertUsers = {folloingUser,userRemoteKey->
                    defaultUserRepository.insertFollowersUsersWithFollowingUserWithRemoteKey(
                        userName,folloingUser,null,userRemoteKey
                    )
                }
            )
        val pagingState = PagingState<Int, UserEntity>(listOf(), null, PagingConfig(10), 10)
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue (result is RemoteMediator.MediatorResult.Error)
    }

}