package com.yasser.githubusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yasser.githubusers.ui.component.GitHubIconButton
import com.yasser.githubusers.ui.component.GitHubImage
import com.yasser.githubusers.ui.component.GitHubText
import com.yasser.githubusers.ui.theme.GitHubUsersTheme
import com.yasser.githubusers.utils.const.TestTag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubUsersTheme {
                val systemUiController = rememberSystemUiController()
                val color=MaterialTheme.colorScheme.background
                LaunchedEffect(systemUiController) { systemUiController.setSystemBarsColor(color = color) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(navController:NavHostController=rememberNavController()) {
    val mainViewModel:MainViewModel= hiltViewModel()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mainUIState=mainViewModel.mainUIState
    val selectedUserDomain by mainUIState.selectedUser.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            val topPadding=it.calculateTopPadding()
            GitHubUsersNavHost(
                topPadding=topPadding,navHostController = navController,
                updateSelectedUser = mainUIState::updateSelectedUser
            )
        },
        topBar = {
            AnimatedVisibility(visible = selectedUserDomain!=null) {
                TopAppBar(
                    modifier = Modifier.testTag(TestTag.TOP_BAR),
                    title = {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GitHubImage(
                                modifier=Modifier.size(50.dp),
                                imageUrl = selectedUserDomain?.avatarUrl.orEmpty()
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            GitHubText(text = selectedUserDomain?.userName.orEmpty(), style = MaterialTheme.typography.titleLarge)
                        }
                    },
                    navigationIcon = {
                        GitHubIconButton(
                            modifier = Modifier.testTag(TestTag.BACK_BUTTON),
                            imageVector = Icons.Default.ArrowBack, onClick = {navController.popBackStack()}
                        )
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    GitHubUsersTheme {
        MainContent(rememberNavController())
    }
}