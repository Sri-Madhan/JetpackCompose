package com.example.compose.screens

import com.example.compose.InputFieldsDemo
import com.example.compose.common.RuleSelector
import com.example.compose.common.TestRuleType
import com.example.compose.common.assertElementExists
import com.example.compose.common.checkComponentProperties
import io.qameta.allure.Description
import org.junit.Rule
import org.junit.Test
//import org.junit.jupiter.api.Tag

class FormScreenTest {

    @get:Rule
    var rule = RuleSelector().getTestRule(TestRuleType.COMPOSE)

    @Test
//    @Tag("componentTest")
    @Description("Test to check the component properties")
    fun verifyInputField(){

        rule.setContent {
            InputFieldsDemo()
        }
        rule.assertElementExists("normal_inp", byText = false, byTag = true,byContentDescription = false)
        var property = rule.checkComponentProperties("normal_inp", byText = false, byTag = true,byContentDescription = false)
        println("properties: " + property)

        assert(property["isInputField"] == true)
        assert(property["canSetText"] == true)
        assert(property["isPasteable"] == true)
        assert(property["isEnabled"] == true)

    }

}