package com.example.compose.common

import androidx.compose.ui.test.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import org.junit.Rule
import androidx.test.core.app.ActivityScenario
import com.example.compose.MainActivity


// This function now accepts a test rule as a parameter
fun ComposeTestRule.findElement(
    identifier: String,
    byText: Boolean = false,
    byTag: Boolean = false,
    byContentDescription: Boolean = false
): SemanticsNodeInteraction {
    return when {
        byText -> onNodeWithText(identifier)
        byTag -> onNodeWithTag(identifier)
        byContentDescription -> onNodeWithContentDescription(identifier)
        else -> throw IllegalArgumentException("Specify either byText, byTag, or byContentDescription.")
    }
}

// Function to assert that an element exists
fun ComposeTestRule.assertElementExists(
    identifier: String,
    byText: Boolean = false,
    byTag: Boolean = false,
    byContentDescription: Boolean = false
) {
    findElement(identifier, byText, byTag, byContentDescription).assertExists()
}
