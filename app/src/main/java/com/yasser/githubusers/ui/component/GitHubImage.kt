package com.yasser.githubusers.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yasser.githubusers.R

@Composable
fun GitHubImage(modifier: Modifier, imageUrl:String) {
    AsyncImage(
        modifier = modifier.clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl).crossfade(true).build(),
        placeholder = rememberVectorPainter(image = Icons.Default.Person),
        contentDescription = stringResource(R.string.avatar),
        contentScale = ContentScale.Crop,
    )
}


@Preview
@Composable
private fun GitHubImagePreview(){
    GitHubImage(Modifier,"")
}