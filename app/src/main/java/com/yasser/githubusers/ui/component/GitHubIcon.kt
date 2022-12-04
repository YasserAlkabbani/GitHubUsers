package com.yasser.githubusers.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GitHubIcon(imageVector: ImageVector){
    Icon(imageVector = imageVector, contentDescription = null)
}

@Preview
@Composable
private fun GitHubIconPreview(){
    GitHubIcon(imageVector = Icons.Default.Android)
}