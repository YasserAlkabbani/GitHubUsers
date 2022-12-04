package com.yasser.githubusers.ui.screen.search_for_user

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yasser.githubusers.R
import com.yasser.githubusers.manager.NavigationManager
import com.yasser.githubusers.ui.component.GitHubIcon
import com.yasser.githubusers.ui.component.GitHubIconButton
import com.yasser.githubusers.ui.component.GitHubText
import com.yasser.githubusers.ui.component.GithubTextField
import com.yasser.githubusers.ui.slot.UserDetailsSlot

fun NavGraphBuilder.searchForUser(navigateToUsers:(String, NavigationManager.Users.GetUsersFilter)->Unit){
    composable(NavigationManager.SearchForUser.route){
        val searchForUserViewModel:SearchForUserViewModel = hiltViewModel()
        val searchForUserUIState=searchForUserViewModel.searchForUserUIState

        val navigationToFollowers by searchForUserUIState.navigationToFollowers.collectAsState()
        LaunchedEffect(key1 = navigationToFollowers, block = {
            navigationToFollowers?.let {
                searchForUserUIState.doneNavigationToFollowers()
                navigateToUsers(it,NavigationManager.Users.GetUsersFilter.Follower)
            }
        })

        val navigationToFollowing by searchForUserUIState.navigationToFollowing.collectAsState()
        LaunchedEffect(key1 = navigationToFollowing, block = {
            navigationToFollowing?.let {
                searchForUserUIState.doneNavigationToFollowing()
                navigateToUsers(it,NavigationManager.Users.GetUsersFilter.Following)
            }
        })

        SearchForUserScreen(searchForUserUIState = searchForUserUIState)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchForUserScreen(searchForUserUIState: SearchForUserUIState){

    val userSearchKey by searchForUserUIState.userNameSearchKey.collectAsState()
    val user by searchForUserUIState.user.collectAsState()
    val isLoading by searchForUserUIState.isLoading.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
        GithubTextField(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
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
        AnimatedVisibility(visible = isLoading) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(visible = user!=null) {
            Column(
                Modifier
                    .padding(5.dp)
                    .fillMaxWidth()) {
                user?.let { AnimatedContent(it) {
                    UserDetailsSlot(
                        userDomain = it,
                        showFollowers = searchForUserUIState::startNavigationToFollowers,
                        showFollowing = searchForUserUIState::startNavigationToFollowing
                    )
                } }
            }
        }
        AnimatedVisibility(visible = user==null) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GitHubText(text = stringResource(R.string.no_user_found), style = MaterialTheme.typography.displaySmall)
            }
        }
    }
}