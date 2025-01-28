package com.example.compose

import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class XMLScreenTest {
    @get:Rule
    var rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyXMLTitle(){

        rule.activity.runOnUiThread{
            var textView = rule.activity.findViewById<TextView>(R.id.sample_text)
            assertEquals("Updated Text from Compose",textView.text.toString())
        }
    }

    @Test
    fun verifyXMLButtonAction(){
        rule.activity.runOnUiThread{
            var button = rule.activity.findViewById<Button>(R.id.sample_button)
            button.performClick()
            var textView = rule.activity.findViewById<TextView>(R.id.sample_text)
            assertEquals("Button Clicked!",textView.text.toString())
        }
    }

}