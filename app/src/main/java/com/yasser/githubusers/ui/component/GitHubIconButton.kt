package com.yasser.githubusers.ui.component

import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GitHubIconButton(modifier: Modifier=Modifier,imageVector: ImageVector,onClick:()->Unit){
    IconButton(modifier = modifier, onClick = onClick) {
        GitHubIcon(imageVector = imageVector)
    }
}

@Preview
@Composable
private fun GithubIconButtonPreview(){
    GitHubIconButton(Modifier,Icons.Default.Android,{})
}