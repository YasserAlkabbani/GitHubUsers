package com.yasser.githubusers.ui.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GitHubText(modifier: Modifier=Modifier,text:String,style:TextStyle= LocalTextStyle.current){
    Text(modifier = modifier,text = text, style = style, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Preview
@Composable
fun GitHubTextPreview(){
    GitHubText(text = "TEST_TEXT")
}
