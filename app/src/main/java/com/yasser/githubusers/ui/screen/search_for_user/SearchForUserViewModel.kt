package com.yasser.githubusers.ui.screen.search_for_user

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasser.githubusers.R
import com.yasser.githubusers.data.user.UserRepository
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.manager.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchForUserViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel() {

    val searchForUserUIState:SearchForUserUIState = SearchForUserUIState()

    @OptIn(ExperimentalCoroutinesApi::class)
    val user:StateFlow<UserDomain?> = searchForUserUIState.userNameSearchKey
        .flatMapLatest {userName-> userRepository.findUserByUserNameAsFlow(userName) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),null)

    init {
        refreshUserByUserNameSearchKey()
    }

    private fun refreshUserByUserNameSearchKey(){
        viewModelScope.launch {
            searchForUserUIState.userNameSearchKey.collectLatest {userName->
                searchForUserUIState.startLoading()
                searchForUserUIState.updateTextForDisplay(R.string.loading)
                userRepository.refreshUserByUserName(userName).let {
                    when(it){
                        is RequestResult.Failure ->
                            when {
                                it.throwable is HttpException && it.throwable.code()==404->
                                    searchForUserUIState.updateTextForDisplay(R.string.user_not_found)
                                it.throwable is HttpException && it.throwable.code()==403->
                                    searchForUserUIState.updateTextForDisplay(R.string.too_many_request)
                                else ->searchForUserUIState.updateTextForDisplay(R.string.connection_error)
                        }
                        is RequestResult.Success ->searchForUserUIState.updateTextForDisplay(null)
                    }
                }
                searchForUserUIState.endLoading()
            }
        }
    }

}

class SearchForUserUIState{

    private val _userNameSearchKey: MutableStateFlow<String> = MutableStateFlow("")
    val userNameSearchKey: StateFlow<String> =_userNameSearchKey
    fun updateUserNameSearchKey(newSearchKey:String){_userNameSearchKey.update { newSearchKey }}

    private val _isLoading:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading:StateFlow<Boolean> =_isLoading
    fun startLoading(){_isLoading.update { true }}
    fun endLoading(){_isLoading.update { false }}

    private val _navigationToFollowers:MutableStateFlow<String?> = MutableStateFlow(null)
    val navigationToFollowers:StateFlow<String?> = _navigationToFollowers
    fun startNavigationToFollowers(userName:String){_navigationToFollowers.update { userName }}
    fun doneNavigationToFollowers(){_navigationToFollowers.update { null }}

    private val _navigationToFollowing:MutableStateFlow<String?> = MutableStateFlow(null)
    val navigationToFollowing:StateFlow<String?> = _navigationToFollowing
    fun startNavigationToFollowing(userName: String){_navigationToFollowing.update { userName }}
    fun doneNavigationToFollowing(){_navigationToFollowing.update { null }}

    private val _textForDisplay:MutableStateFlow<Int?> = MutableStateFlow(null)
    val textForDisplay:StateFlow<Int?> =_textForDisplay
    fun updateTextForDisplay(@StringRes text:Int?){_textForDisplay.update { text }}

}