package com.example.compose.common

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule

fun ComposeTestRule.checkComponentProperties(
    identifier: String,
    byText: Boolean = false,
    byTag: Boolean = false,
    byContentDescription: Boolean = false
): Map<String, Any> {
    val node = when {
        byText -> onNodeWithText(identifier)
        byTag -> onNodeWithTag(identifier)
        byContentDescription -> onNodeWithContentDescription(identifier)
        else -> throw IllegalArgumentException("Specify either byText, byTag, or byContentDescription.")
    }

    node.assertExists()
    val semanticsNode = node.fetchSemanticsNode()
    val allProperties = mutableMapOf<String, Any>()
     when {
        isButton(semanticsNode) -> {
            allProperties.putAll(checkButtonProperties(semanticsNode))
            allProperties.putAll(checkGeneralProperties(semanticsNode))
        }
        isTextField(semanticsNode) -> {
            allProperties.putAll(checkTextFieldProperties(semanticsNode))
            allProperties.putAll(checkGeneralProperties(semanticsNode))
        }
        isScrollable(semanticsNode) -> {
            allProperties.putAll(checkScrollableProperties(semanticsNode))
            allProperties.putAll(checkGeneralProperties(semanticsNode))
        }
        isExpandable(semanticsNode) -> {
            allProperties.putAll(checkExpandableProperties(semanticsNode))
            allProperties.putAll(checkGeneralProperties(semanticsNode))
        }
        isDialog(semanticsNode) -> {
            allProperties.putAll(checkDialogProperties(semanticsNode))
            allProperties.putAll(checkGeneralProperties(semanticsNode))
        }
        else -> allProperties.putAll(checkGeneralProperties(semanticsNode))
    }
    return allProperties
}

private fun isButton(node: SemanticsNode): Boolean {
    return node.config.getOrNull(SemanticsActions.OnClick) != null &&
            node.config.getOrNull(SemanticsProperties.EditableText) == null
}

private fun isTextField(node: SemanticsNode): Boolean {
    return node.config.getOrNull(SemanticsProperties.EditableText) != null
}

private fun isScrollable(node: SemanticsNode): Boolean {
    return node.config.getOrNull(SemanticsProperties.HorizontalScrollAxisRange) != null ||
            node.config.getOrNull(SemanticsProperties.VerticalScrollAxisRange) != null
}

private fun isExpandable(node: SemanticsNode): Boolean {
    return node.config.getOrNull(SemanticsActions.Expand) != null ||
            node.config.getOrNull(SemanticsActions.Collapse) != null
}

private fun isDialog(node: SemanticsNode): Boolean {
    return node.config.getOrNull(SemanticsProperties.IsDialog) != null ||
            node.config.getOrNull(SemanticsProperties.IsPopup) != null
}

private fun checkGeneralProperties(node: SemanticsNode): Map<String, Boolean> {
    return mutableMapOf(
        "isEnabled" to (node.config.getOrNull(SemanticsProperties.Disabled) == null),
        "isFocusable" to (node.config.getOrNull(SemanticsProperties.Focused) != null),
        "isSelected" to (node.config.getOrNull(SemanticsProperties.Selected) != null),
        "hasRole" to (node.config.getOrNull(SemanticsProperties.Role) != null)
    )
}

private fun checkButtonProperties(node: SemanticsNode): Map<String, Boolean> {
    return mapOf(
        "isClickable" to true,
        "isEnabled" to (node.config.getOrNull(SemanticsProperties.Disabled) == null)
    )
}

private fun checkTextFieldProperties(node: SemanticsNode): Map<String, Boolean> {
    return mapOf(
        "isInputField" to true,
        "canSetText" to (node.config.getOrNull(SemanticsActions.SetText) != null),
        "isCopyable" to (node.config.getOrNull(SemanticsActions.CopyText) != null),
        "isPasteable" to (node.config.getOrNull(SemanticsActions.PasteText) != null),
        "isCuttable" to (node.config.getOrNull(SemanticsActions.CutText) != null)
    )
}

private fun checkScrollableProperties(node: SemanticsNode): Map<String, Boolean> {
    return mapOf(
        "canScrollBy" to (node.config.getOrNull(SemanticsActions.ScrollBy) != null),
        "canScrollToIndex" to (node.config.getOrNull(SemanticsActions.ScrollToIndex) != null),
        "isHorizontallyScrollable" to (node.config.getOrNull(SemanticsProperties.HorizontalScrollAxisRange) != null),
        "isVerticallyScrollable" to (node.config.getOrNull(SemanticsProperties.VerticalScrollAxisRange) != null)
    )
}

private fun checkExpandableProperties(node: SemanticsNode): Map<String, Boolean> {
    return mapOf(
        "isExpandable" to (node.config.getOrNull(SemanticsActions.Expand) != null),
        "isCollapsible" to (node.config.getOrNull(SemanticsActions.Collapse) != null)
    )
}

private fun checkDialogProperties(node: SemanticsNode): Map<String, Boolean> {
    return mapOf(
        "isDialog" to (node.config.getOrNull(SemanticsProperties.IsDialog) != null),
        "isPopup" to (node.config.getOrNull(SemanticsProperties.IsPopup) != null)
    )
}

