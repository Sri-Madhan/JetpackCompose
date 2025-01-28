package com.example.compose.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag

class element_actions {

    @Composable
    fun textCheck(identifier: String, expectedText: String, context: ComposeTestRule) {
        context.onNodeWithTag(identifier)
            .assertExists("Element with identifier '$identifier' does not exist")
            .assertIsDisplayed()
            .assertTextEquals(expectedText)

        context.onNodeWithTag(identifier)
            .assertTextEquals("This is a Text")
    }
}