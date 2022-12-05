package com.yasser.githubusers.ui.screen.users

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.manager.NavigationManager
import com.yasser.githubusers.ui.card.UserCard
import timber.log.Timber

fun NavController.navigateToUsers(userName:String, getUsersFilter: NavigationManager.Users.GetUsersFilter){
    NavigationManager.Users.let {users->
        navigate("${users.route}/$userName/${getUsersFilter.name}"){
            launchSingleTop=true
            restoreState=true
        }
    }
}

fun NavGraphBuilder.users(updateSelectedUser:(userDomain: UserDomain?)->Unit){
    NavigationManager.Users.let {users->
        composable(
            route = "${users.route}/{${users.userNameArg}}/{${users.getUsersFilterArg}}",
            arguments = listOf(
                navArgument(users.userNameArg){type= NavType.StringType},
                navArgument(users.getUsersFilterArg){type= NavType.StringType}
            )
        ){
            val usersViewModel: UsersViewModel= hiltViewModel()
            val usersUIState=usersViewModel.usersUIState

            val selectedUserDomain by usersViewModel.userDomain.collectAsState()
            LaunchedEffect(key1 = selectedUserDomain, block = {
                updateSelectedUser(selectedUserDomain)
            })

            UsersScreen(usersUIState = usersUIState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun UsersScreen(usersUIState: UsersUIState) {

    val usersPagingItem=usersUIState.usersPagingData.collectAsLazyPagingItems()

    val refreshing= remember(usersPagingItem.loadState) {usersPagingItem.loadState.refresh is LoadState.Loading}
    val state = rememberPullRefreshState(refreshing, usersPagingItem::refresh)

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = usersPagingItem.itemSnapshotList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .pullRefresh(state)
                        .fillMaxSize(),
                    content = {
                        items(
                            items = usersPagingItem,
                            key = {it.id},
                            itemContent = { user->
                                user?.let {
                                    UserCard(modifier = Modifier.animateItemPlacement(), userDomain = user)
                                    Spacer(modifier = Modifier.height(1.dp))
                                }
                            }
                        )
                    }
                )
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }

}



@Preview
@Composable
fun UsersScreenPreview(){

}