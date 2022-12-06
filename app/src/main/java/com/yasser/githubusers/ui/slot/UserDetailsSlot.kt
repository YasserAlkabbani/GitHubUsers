package com.yasser.githubusers.ui.slot

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yasser.githubusers.R
import com.yasser.githubusers.data.user.model.useres.UserDomain
import com.yasser.githubusers.ui.component.GitHubButton
import com.yasser.githubusers.ui.component.GitHubImage
import com.yasser.githubusers.ui.component.GitHubText
import com.yasser.githubusers.utils.const.TestTag
import com.yasser.githubusers.utils.const.TestTag.USER_DETAILS_USER_NAME

@Composable
private fun KeyWithValue(
    modifier: Modifier=Modifier,key:String,value:String,onClick:(()->Unit)?=null,
    buttonModifier: Modifier=Modifier
){
    Surface(modifier = modifier.padding(1.dp), shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GitHubText(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                text = key, style = MaterialTheme.typography.titleLarge
            )
            if (onClick!=null){
                GitHubButton(
                    modifier = buttonModifier
                        .fillMaxWidth()
                        .weight(3f),
                    text = "${stringResource(R.string.show)} $value ${stringResource(R.string.users)}", onClick = onClick
                )
            }else{
                GitHubText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    text = ": $value", style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun UserDetailsSlot(
    userDomain: UserDomain,
    showFollowers:(String)->Unit,showFollowing:(String)->Unit
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .testTag(TestTag.USER_DETAILS)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
            Row(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GitHubImage(modifier=Modifier.size(70.dp),imageUrl = userDomain.avatarUrl)
                Spacer(modifier = Modifier.width(15.dp))
                GitHubText(modifier = Modifier.testTag(USER_DETAILS_USER_NAME), text = userDomain.userName, style = MaterialTheme.typography.displaySmall)
            }
            KeyWithValue(key = stringResource(R.string.type), value = userDomain.type)
            KeyWithValue(key = stringResource(R.string.name), value = userDomain.name)
            KeyWithValue(key = stringResource(R.string.company), value = userDomain.company)
            KeyWithValue(
                key = stringResource(R.string.followers), value = userDomain.followers.toString(),
                onClick = {showFollowers(userDomain.userName)},
                buttonModifier = Modifier.testTag(TestTag.FOLLOWER_BUTTON)
            )
            KeyWithValue(
                key = stringResource(R.string.following), value = userDomain.following.toString(),
                onClick = {showFollowing(userDomain.userName)},
                buttonModifier = Modifier.testTag(TestTag.FOLLOWING_BUTTON)
            )
            KeyWithValue(key = stringResource(R.string.blog), value = userDomain.blog)
            KeyWithValue(key = stringResource(R.string.location), value = userDomain.location)
            KeyWithValue(key = stringResource(R.string.email), value = userDomain.email)
            KeyWithValue(key = stringResource(R.string.bio), value = userDomain.bio)
            KeyWithValue(key = stringResource(R.string.twitter_user_name), value = userDomain.twitterUsername)
            KeyWithValue(key = stringResource(R.string.public_repos), value = userDomain.publicRepos.toString())
            KeyWithValue(key = stringResource(R.string.public_gists), value = userDomain.publicGists.toString())
            KeyWithValue(key = stringResource(R.string.updated_date), value = userDomain.updatedAt)
            KeyWithValue(key = stringResource(R.string.created_date), value = userDomain.createdAt)
        }
    }
}

@Preview
@Composable
private fun UserDetailsSlotPreview(){
    val userDomain= UserDomain(
        1,"USER_NAME","1356","","", "","",
        "", "", "","","",
        "","","","","TYPE",true,
        "NAME","COMPANY","BLOG","LOCATION","EMAIL@EMAIL>COM",
        false,"BIO","TWITTER_USER_NAME",
        30,70,130,15,"2008-01-14T04:33:35Z","2008-01-14T04:33:35Z"
    )
    UserDetailsSlot(userDomain,{},{})
}