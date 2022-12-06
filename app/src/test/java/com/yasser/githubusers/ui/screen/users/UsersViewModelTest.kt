package com.yasser.githubusers.ui.screen.users

import androidx.lifecycle.SavedStateHandle
import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.user.FakeUserRepository
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.manager.NavigationManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private lateinit var usersViewModel: UsersViewModel
    private lateinit var userRepository: UserRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun initSearchForUserUIState()= runTest{
        val userName="USER_1"
        userRepository= FakeUserRepository()
        val savedStateHandle:SavedStateHandle = SavedStateHandle().apply {
            set(NavigationManager.Users.userNameArg,userName)
            set(NavigationManager.Users.usersFilterArg,NavigationManager.Users.UsersFilter.Following.name)
        }
        usersViewModel= UsersViewModel(savedStateHandle, userRepository)
        advanceUntilIdle()
        userRepository.refreshUserByUserName(userName)
    }

    @Test
    fun getUserDomain_USER1() = runTest{
        val userName:String=usersViewModel.savedStateHandle.get<String>(NavigationManager.Users.userNameArg)!!
        val userNameResult=usersViewModel.userDomain.first()?.userName
        assertEquals(userName,userNameResult)
    }

    @Test
    fun getUserFilter_Following()= runTest{
        val userFilter:String=usersViewModel.savedStateHandle.get<String>(NavigationManager.Users.usersFilterArg)!!
        assertEquals(userFilter,usersViewModel.usersFilter.first().name)
    }

}