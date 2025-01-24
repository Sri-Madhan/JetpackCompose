package com.example.compose

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.hamcrest.Matchers.not

class PermissionScreenTest {
    @get:Rule
    var rule = createAndroidComposeRule<MainActivity>()
    @get:Rule
    val permissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA
    )

    @Test
    fun verifyPermissionButton(){
        rule.onNodeWithTag("permissionNav").performClick()
        rule.onNodeWithTag("permission_button").assertExists()
    }

    @Test
    fun verifyPermissonPopupDisplayed(){
        rule.onNodeWithTag("permissionNav").performClick()
        rule.onNodeWithTag("permission_button").performClick()
//        onView(withText("Permission Granted"))
//            .inRoot(withDecorView(not(rule.activity.window.decorView)))
//            .check(matches(withText("Permission Granted")))
    }

}