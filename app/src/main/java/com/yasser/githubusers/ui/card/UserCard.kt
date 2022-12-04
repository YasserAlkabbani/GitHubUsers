package com.yasser.githubusers.ui.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.ui.component.GitHubImage
import com.yasser.githubusers.ui.component.GitHubText

@Composable
fun UserCard(modifier: Modifier,userDomain: UserDomain){
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GitHubImage(modifier=Modifier.size(70.dp),imageUrl = userDomain.avatarUrl)
            Spacer(modifier = Modifier.width(15.dp))
            GitHubText(text = userDomain.userName, style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
private fun UserCardPreview(){
    val userDomain=UserDomain(
        1,"USER_NAME","1356","","", "","",
        "", "", "","","",
        "","","","","TYPE",true,
        "NAME","COMPANY","BLOG","LOCATION","EMAIL@EMAIL>COM",
        false,"BIO","TWITTER_USER_NAME",
        30,70,130,15,"2008-01-14T04:33:35Z","2008-01-14T04:33:35Z"
    )
}