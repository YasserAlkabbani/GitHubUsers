package com.yasser.githubusers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.manager.NavigationManager
import com.yasser.githubusers.ui.screen.search_for_user.searchForUser
import com.yasser.githubusers.ui.screen.users.navigateToUsers
import com.yasser.githubusers.ui.screen.users.users
import timber.log.Timber

@Composable
fun GitHubUsersNavHost(
    navHostController: NavHostController, topPadding: Dp,
    updateSelectedUser:(userDomain: UserDomain?)->Unit
) {

    NavHost(
        modifier = Modifier.padding(top = topPadding),navController = navHostController,
        startDestination = NavigationManager.SearchForUser.route,
        builder = {
            users(updateSelectedUser)
            searchForUser(
                navigateToUsers = { userName,getUserFilter->
                    navHostController.navigateToUsers(userName,getUserFilter)
                },
                clearSelectedUser = {updateSelectedUser(null)}
            )
        }
    )

}