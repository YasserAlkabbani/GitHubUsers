package com.yasser.githubusers.ui.component

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GitHubButton(modifier: Modifier=Modifier,text:String,onClick:()->Unit){
    Button(modifier = modifier, onClick = onClick){ GitHubText(text = text) }
}


@Preview
@Composable
private fun GitHubButtonPreview(){
    GitHubButton(text = "CLICK_ME", onClick = {})
}