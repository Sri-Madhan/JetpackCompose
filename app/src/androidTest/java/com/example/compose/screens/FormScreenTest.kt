package com.example.compose.screens

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.InputFieldsDemo
import com.example.compose.common.assertElementExists
import com.example.compose.common.checkComponentProperties
import org.junit.Rule
import org.junit.Test

class FormScreenTest {

    @get:Rule
    var rule = createComposeRule()

    @Test
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