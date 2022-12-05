package com.yasser.githubusers.ui.screen.users

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.manager.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor (
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
):ViewModel(){

    private val userName:Flow<String> =savedStateHandle.getStateFlow(NavigationManager.Users.userNameArg,null).filterNotNull()
    val userDomain=userName.flatMapLatest { userRepository.findUserByUserNameAsFlow(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly,null)
    private val getUsersFilter=
        savedStateHandle.getStateFlow<String?>(NavigationManager.Users.getUsersFilterArg,null).mapNotNull {
            when(it){
                NavigationManager.Users.GetUsersFilter.Follower.name->NavigationManager.Users.GetUsersFilter.Follower
                NavigationManager.Users.GetUsersFilter.Following.name->NavigationManager.Users.GetUsersFilter.Following
                else -> null
        }
    }

    private val userNameWithGetUserFilter= combine(userName,getUsersFilter){
            userName,getUsersFilter-> userName to getUsersFilter
    }

    val usersUIState:UsersUIState=UsersUIState(
        usersPagingData = userNameWithGetUserFilter.filterNotNull()
            .flatMapLatest { (userName,getUsersFilter)->
                Timber.d("TEST_GET_USER $userName $getUsersFilter")
                when(getUsersFilter){
                    NavigationManager.Users.GetUsersFilter.Follower -> userRepository.getFollowersAsPagingSource(userName)
                    NavigationManager.Users.GetUsersFilter.Following -> userRepository.getFollowingAsPagingSource(userName)
                }
        }.flowOn(Dispatchers.Default).cachedIn(viewModelScope),
    )


}

class UsersUIState(val usersPagingData:Flow<PagingData<UserDomain>>)
