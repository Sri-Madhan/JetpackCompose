package com.example.compose.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule


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

fun ComposeTestRule.assertElementExists(
    identifier: String,
    byText: Boolean = false,
    byTag: Boolean = false,
    byContentDescription: Boolean = false
) {
    findElement(identifier, byText, byTag, byContentDescription).assertExists()
}
