package com.yasser.githubusers.ui.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.yasser.githubusers.MainActivity
import com.yasser.githubusers.MainContent
import com.yasser.githubusers.R
import com.yasser.githubusers.manager.NavigationManager
import com.yasser.githubusers.ui.screen.users.navigateToUsers
import com.yasser.githubusers.ui.theme.GitHubUsersTheme
import com.yasser.githubusers.utils.const.TestTag
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TestUI(){

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val androidComposeTestRole = createAndroidComposeRule<MainActivity>()

    lateinit var navHostController:TestNavHostController

    @Before
    fun initUITest() {
        hiltRule.inject()
        androidComposeTestRole.activity.setContent {
            navHostController = TestNavHostController(LocalContext.current)
            navHostController.navigatorProvider.addNavigator(ComposeNavigator())
            MainContent(navHostController)
        }
    }

    @Test
    fun verifySearchForUserAndUsersScreen(){

        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS).assertDoesNotExist()

        val textField=androidComposeTestRole.onNodeWithTag(TestTag.SEARCH_TEXT_FIELD)
        textField.assertIsDisplayed()
        val value = textField.fetchSemanticsNode().config[SemanticsProperties.EditableText]
        assertEquals("",value.text)
        textField.performTextInput("USER_1")
        val editedValue=textField.fetchSemanticsNode().config[SemanticsProperties.EditableText]
        assertEquals("USER_1",editedValue.text)

        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS_USER_NAME).assertTextContains("USER_1")

        androidComposeTestRole.onNodeWithTag(TestTag.FOLLOWER_BUTTON).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.FOLLOWING_BUTTON).assertExists()

        val route0 = navHostController.currentBackStackEntry?.destination?.route
        assertEquals(route0, NavigationManager.SearchForUser.route)

        androidComposeTestRole.onNodeWithTag(TestTag.FOLLOWER_BUTTON).performClick()

        androidComposeTestRole.onNodeWithTag(TestTag.USERS_COLUMN).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.TOP_BAR).assertExists()

        val route2 = navHostController.currentBackStackEntry?.destination?.route
        assertTrue(route2.orEmpty().contains(NavigationManager.Users.route))

        androidComposeTestRole.onNodeWithTag(TestTag.USER_LAZY_COLUMN).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.USER_LAZY_COLUMN).performScrollToIndex(5)

        androidComposeTestRole.onNodeWithTag(TestTag.BACK_BUTTON).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.BACK_BUTTON).performClick()

        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS).assertExists()
        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS_USER_NAME).assertTextContains("USER_1")

    }

}