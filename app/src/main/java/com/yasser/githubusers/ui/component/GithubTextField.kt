package com.yasser.githubusers.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubTextField(
    modifier: Modifier,text:String,placeHolder:String,onTextChanged:(String)->Unit,
    leading:(@Composable ()->Unit)?,trailing:(@Composable ()->Unit)?
){
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        placeholder = {GitHubText(text = placeHolder)},
        onValueChange = onTextChanged,
        leadingIcon = leading,
        trailingIcon = trailing,
        shape = MaterialTheme.shapes.extraLarge,
        singleLine = true
    )
}

@Preview
@Composable
fun GithubTextFieldPreview(){
    GithubTextField(Modifier,"Search Key","",{},{},{})
}