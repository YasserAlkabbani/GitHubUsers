package com.yasser.githubusers.ui.screen.search_for_user

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yasser.githubusers.R
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.manager.NavigationManager
import com.yasser.githubusers.ui.component.GitHubIcon
import com.yasser.githubusers.ui.component.GitHubIconButton
import com.yasser.githubusers.ui.component.GitHubText
import com.yasser.githubusers.ui.component.GithubTextField
import com.yasser.githubusers.ui.slot.UserDetailsSlot
import com.yasser.githubusers.utils.const.TestTag
import kotlinx.coroutines.flow.StateFlow

fun NavGraphBuilder.searchForUser(
    navigateToUsers:(String, NavigationManager.Users.UsersFilter)->Unit,
    clearSelectedUser:()->Unit
){
    composable(NavigationManager.SearchForUser.route){
        val searchForUserViewModel:SearchForUserViewModel = hiltViewModel()
        val searchForUserUIState=searchForUserViewModel.searchForUserUIState
        val user=searchForUserViewModel.user

        val navigationToFollowers by searchForUserUIState.navigationToFollowers.collectAsState()
        LaunchedEffect(key1 = navigationToFollowers, block = {
            navigationToFollowers?.let {
                searchForUserUIState.doneNavigationToFollowers()
                navigateToUsers(it,NavigationManager.Users.UsersFilter.Follower)
            }
        })

        val navigationToFollowing by searchForUserUIState.navigationToFollowing.collectAsState()
        LaunchedEffect(key1 = navigationToFollowing, block = {
            navigationToFollowing?.let {
                searchForUserUIState.doneNavigationToFollowing()
                navigateToUsers(it,NavigationManager.Users.UsersFilter.Following)
            }
        })

        LaunchedEffect(key1 = Unit, block = {
            clearSelectedUser()
        })

        SearchForUserScreen(searchForUserUIState = searchForUserUIState,user=user)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchForUserScreen(searchForUserUIState: SearchForUserUIState,user:StateFlow<UserDomain?>){

    val userSearchKey by searchForUserUIState.userNameSearchKey.collectAsState()
    val userValue by user.collectAsState()
    val isLoading by searchForUserUIState.isLoading.collectAsState()
    val textForDisplay by searchForUserUIState.textForDisplay.collectAsState()

    Column(Modifier.fillMaxSize()) {
        GithubTextField(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .testTag(TestTag.SEARCH_TEXT_FIELD),
            text = userSearchKey, onTextChanged = searchForUserUIState::updateUserNameSearchKey,
            placeHolder = stringResource(R.string.search_for_user),
            leading = { GitHubIcon(imageVector = Icons.Default.Search) },
            trailing = {
                AnimatedVisibility(visible = userSearchKey.isNotBlank()) {
                    GitHubIconButton(
                        imageVector = Icons.Default.Close,
                        onClick = { searchForUserUIState.updateUserNameSearchKey("") })
                }
            },
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            AnimatedVisibility(visible = isLoading) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(Modifier.testTag(TestTag.LOADING_PROGRESS))
                }
            }
            AnimatedVisibility(visible = textForDisplay!=null) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    textForDisplay?.let { GitHubText(Modifier,stringResource(id = it),style = MaterialTheme.typography.titleLarge) }
                }
            }
            AnimatedVisibility(visible = userValue!=null) {
                Column(
                    Modifier
                        .padding(5.dp)
                        .fillMaxWidth()) {
                    userValue?.let { AnimatedContent(it) {
                        UserDetailsSlot(
                            userDomain = it,
                            showFollowers = searchForUserUIState::startNavigationToFollowers,
                            showFollowing = searchForUserUIState::startNavigationToFollowing
                        )
                    } }
                }
            }
        }
    }

}