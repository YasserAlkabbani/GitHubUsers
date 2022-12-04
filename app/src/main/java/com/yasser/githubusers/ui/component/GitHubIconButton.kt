package com.yasser.githubusers.ui.component

import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GitHubIconButton(imageVector: ImageVector,onClick:()->Unit){
    IconButton(onClick = onClick) {
        GitHubIcon(imageVector = imageVector)
    }
}

@Preview
@Composable
private fun GithubIconButtonPreview(){
    GitHubIconButton(Icons.Default.Android,{})
}