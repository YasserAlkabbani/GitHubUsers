package com.yasser.githubusers.ui.screen

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.yasser.githubusers.MainActivity
import com.yasser.githubusers.MainContent
import com.yasser.githubusers.R
import com.yasser.githubusers.ui.theme.GitHubUsersTheme
import com.yasser.githubusers.utils.const.TestTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TestUI(){

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val androidComposeTestRole = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() { hiltRule.inject() }

    lateinit var navHostController:TestNavHostController

    @Before
    fun testUI() {
        androidComposeTestRole.activity.setContent {
            navHostController = TestNavHostController(LocalContext.current)
            navHostController.navigatorProvider.addNavigator(ComposeNavigator())
            GitHubUsersTheme {
                MainContent(navHostController)
            }
        }

    }

    @Test
    fun verifySearchForUserScreen(){

        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS).assertDoesNotExist()

        val textField=androidComposeTestRole.onNodeWithTag(TestTag.SEARCH_TEXT_FIELD)
        textField.assertIsDisplayed()
        val value = textField.fetchSemanticsNode().config[SemanticsProperties.EditableText]
        assertEquals("",value.text)
        textField.performTextInput("TEXT_1")
        val editedValue=textField.fetchSemanticsNode().config[SemanticsProperties.EditableText]
        assertEquals("TEXT_1",editedValue.text)

        androidComposeTestRole.onNodeWithTag(TestTag.USER_DETAILS).assertExists()
    }

}