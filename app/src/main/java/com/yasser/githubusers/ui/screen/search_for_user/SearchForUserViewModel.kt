package com.yasser.githubusers.ui.screen.search_for_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.data.user.model.useres.UserDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchForUserViewModel @Inject constructor(
    private val userRepository: UserRepository
):ViewModel() {

    val searchForUserUIState:SearchForUserUIState = SearchForUserUIState(
        returnCoroutineScope = {viewModelScope},
        findUserByUserNameAsFlow = userRepository::findUserByUserNameAsFlow
    )

    init {
        refreshUserByUserNameSearchKey()
    }

    private fun refreshUserByUserNameSearchKey(){
        viewModelScope.launch {
            searchForUserUIState.userNameSearchKey.collectLatest {userName->
                delay(1000)
                searchForUserUIState.startLoading()
                userRepository.refreshUserByUserName(userName)
                searchForUserUIState.endLoading()
            }
        }
    }

}

class SearchForUserUIState(
    returnCoroutineScope: ()->CoroutineScope,
    findUserByUserNameAsFlow:(String)->Flow<UserDomain?>
){

    private val _userNameSearchKey: MutableStateFlow<String> = MutableStateFlow("")
    val userNameSearchKey: StateFlow<String> =_userNameSearchKey
    fun updateUserNameSearchKey(newSearchKey:String){_userNameSearchKey.update { newSearchKey }}

    private val _isLoading:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading:StateFlow<Boolean> =_isLoading
    fun startLoading(){_isLoading.update { true }}
    fun endLoading(){_isLoading.update { false }}

    val user:StateFlow<UserDomain?> = userNameSearchKey
        .flatMapLatest {userName-> findUserByUserNameAsFlow(userName) }
        .stateIn(returnCoroutineScope(), SharingStarted.WhileSubscribed(5000),null)

    private val _navigationToFollowers:MutableStateFlow<String?> = MutableStateFlow(null)
    val navigationToFollowers:StateFlow<String?> = _navigationToFollowers
    fun startNavigationToFollowers(user:UserDomain){_navigationToFollowers.update { user.userName }}
    fun doneNavigationToFollowers(){_navigationToFollowers.update { null }}
    private val _navigationToFollowing:MutableStateFlow<String?> = MutableStateFlow(null)
    val navigationToFollowing:StateFlow<String?> = _navigationToFollowing
    fun startNavigationToFollowing(user: UserDomain){_navigationToFollowing.update { user.userName }}
    fun doneNavigationToFollowing(){_navigationToFollowing.update { null }}
}