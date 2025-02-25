package com.example.compose.screens

import com.example.compose.R
import com.example.compose.common.RuleSelector
import com.example.compose.common.TestRuleType
import com.example.compose.common.checkViewProperties
import org.junit.Rule
import org.junit.Test
//import org.junit.jupiter.api.Tag

class XMLScreenTest {

    @get:Rule
    var rule = RuleSelector().getTestRule(TestRuleType.ANDROID)

    @Test
//    @Tag("componentTest")
    fun verifyTextElement(){
        var property  = checkViewProperties(R.id.sample_text)
        println("properties "+property)
        assert(property["isEnabled"] == true)
        assert(property["isVisible"] == true)
    }

    @Test
//    @Tag("componentTest")
    fun verifyButtonElement(){
        var property  = checkViewProperties(R.id.sample_button)
        println("properties "+property)
        assert(property["isClickable"] == true)
        assert(property["isEnabled"] == true)
    }

}