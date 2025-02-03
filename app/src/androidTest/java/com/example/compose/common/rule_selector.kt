package com.example.compose.common

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.MainActivity

enum class TestRuleType {
    COMPOSE, ANDROID
}

class RuleSelector {
    fun getTestRule(ruleType: TestRuleType): ComposeContentTestRule {
        return when (ruleType) {
            TestRuleType.COMPOSE -> createComposeRule()
            TestRuleType.ANDROID -> createAndroidComposeRule<MainActivity>()
        }
    }
}