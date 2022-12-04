package com.yasser.githubusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yasser.githubusers.ui.theme.GitHubUsersTheme
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
fun MainContent() {
    val navController= rememberNavController()
    Scaffold(
        content = {
            val topPadding=it.calculateTopPadding()
            GitHubUsersNavHost(topPadding=topPadding,navHostController = navController)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    GitHubUsersTheme {
        MainContent()
    }
}