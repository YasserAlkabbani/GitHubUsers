package com.yasser.githubusers

import androidx.lifecycle.ViewModel
import com.yasser.githubusers.data.user.model.useres.UserDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel() {

    val mainUIState:MainUIState=MainUIState()

}

class MainUIState(){

    private val _selectedUser:MutableStateFlow<UserDomain?> = MutableStateFlow(null)
    val selectedUser:StateFlow<UserDomain?> =_selectedUser
    fun updateSelectedUser(userDomain: UserDomain?){_selectedUser.update { userDomain }}

}