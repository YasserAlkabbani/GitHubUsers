package com.yasser.githubusers.ui.screen.users

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.manager.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor (
        val savedStateHandle: SavedStateHandle, private val userRepository: UserRepository
    ):ViewModel(){

    private val userName:Flow<String> =savedStateHandle.getStateFlow(NavigationManager.Users.userNameArg,null).filterNotNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    val userDomain=userName
        .flatMapLatest { userRepository.findUserByUserNameAsFlow(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)

    val usersFilter=
        savedStateHandle.getStateFlow<String?>(NavigationManager.Users.usersFilterArg,null).mapNotNull {
            when(it){
                NavigationManager.Users.UsersFilter.Follower.name->NavigationManager.Users.UsersFilter.Follower
                NavigationManager.Users.UsersFilter.Following.name->NavigationManager.Users.UsersFilter.Following
                else -> null
        }
    }

    private val userNameWithGetUserFilter=combine(userName,usersFilter){
            userName,getUsersFilter-> userName to getUsersFilter
    }

    val usersPagingData = userNameWithGetUserFilter.flatMapLatest { (userName,getUsersFilter)->
            when(getUsersFilter){
                NavigationManager.Users.UsersFilter.Follower -> userRepository.getFollowersAsPagingSource(userName)
                NavigationManager.Users.UsersFilter.Following -> userRepository.getFollowingAsPagingSource(userName)
            }
        }.flowOn(Dispatchers.Default).cachedIn(viewModelScope)


}
