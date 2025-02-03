package com.example.compose.screens

import android.widget.TextView
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.compose.MainActivity
import com.example.compose.R
import com.example.compose.common.checkViewProperties
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class XMLScreenTest {

    @get:Rule
    var rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyTextElement(){
        var property  = checkViewProperties(R.id.sample_text)
        println("properties "+property)
        assert(property["isEnabled"] == true)
        assert(property["isVisible"] == true)
    }

    @Test
    fun verifyButtonElement(){
        var property  = checkViewProperties(R.id.sample_button)
        println("properties "+property)
        assert(property["isClickable"] == true)
        assert(property["isEnabled"] == true)
    }

}