package com.yasser.githubusers.ui.screen.search_for_user

import com.yasser.githubusers.MainDispatcherRule
import com.yasser.githubusers.data.user.FakeUserRepository
import com.yasser.githubusers.data.user.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchForUserViewModelTest {

    private lateinit var searchForUserViewModel: SearchForUserViewModel
    private lateinit var userRepository: UserRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun initSearchForUserUIState(){
        userRepository=FakeUserRepository()
        searchForUserViewModel=SearchForUserViewModel(userRepository)
    }

    @Test
    fun searchForUser_USER2_USER2()= runTest{

        assertNull(searchForUserViewModel.user.value)
        searchForUserViewModel.searchForUserUIState.updateUserNameSearchKey("USER_2")

        advanceUntilIdle()
        
        assertEquals(searchForUserViewModel.searchForUserUIState.isLoading.value,false)
        assertEquals(searchForUserViewModel.user.first()?.userName,"USER_2")
    }

    @Test
    fun updateUserNameSearchKey_A_EqualA() {
        searchForUserViewModel.searchForUserUIState.updateUserNameSearchKey("")
        assertEquals(searchForUserViewModel.searchForUserUIState.userNameSearchKey.value,"")
        searchForUserViewModel.searchForUserUIState.updateUserNameSearchKey("A")
        assertEquals(searchForUserViewModel.searchForUserUIState.userNameSearchKey.value,"A")
    }

    @Test
    fun loadingState_startLoading_LoadingStateTrue() {
        searchForUserViewModel.searchForUserUIState.startLoading()
        assertEquals(searchForUserViewModel.searchForUserUIState.isLoading.value,true)
    }

    @Test
    fun loadingState_endLoading_LoadingStateFalse() {
        searchForUserViewModel.searchForUserUIState.endLoading()
        assertEquals(searchForUserViewModel.searchForUserUIState.isLoading.value,false)
    }

    @Test
    fun navigationToFollowers_startNavigationToFollowers_navigationToFollowerValueIsYasser() {
        searchForUserViewModel.searchForUserUIState.startNavigationToFollowers("Yasser")
        assertEquals(searchForUserViewModel.searchForUserUIState.navigationToFollowers.value,"Yasser")
    }

    @Test
    fun navigationToFollowers_doneNavigationToFollowers_navigationToFollowerValueIsNull() {
        searchForUserViewModel.searchForUserUIState.doneNavigationToFollowers()
        assertEquals(searchForUserViewModel.searchForUserUIState.navigationToFollowers.value,null)
    }

    @Test
    fun navigationToFollowing_startNavigationToFollowing_navigationToFollowingValueIsYasser() {
        searchForUserViewModel.searchForUserUIState.startNavigationToFollowing("Yasser")
        assertEquals(searchForUserViewModel.searchForUserUIState.navigationToFollowing.value,"Yasser")
    }

    @Test
    fun navigationToFollowing_doneNavigationToFollowing_navigationToFollowingValueIsNull() {
        searchForUserViewModel.searchForUserUIState.doneNavigationToFollowers()
        assertEquals(searchForUserViewModel.searchForUserUIState.navigationToFollowing.value,null)
    }

}