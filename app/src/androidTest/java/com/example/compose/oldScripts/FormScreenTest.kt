package com.example.compose.oldScripts


import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsProperties.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import com.example.compose.InputFieldsDemo
import com.example.compose.ModalsAndPopupsDemo
import com.example.compose.TextDemo
import org.junit.Rule
import org.junit.Test

class FormScreenTest {
    
    @get:Rule
    var rule = createComposeRule()


    @Test
    fun textField(){
        var text ="OutlinedTextField adds an outlined border to the input field, providing a modern and distinct appearance.Error Handling: The isError parameter highlights the field when the input doesn’t meet certain criteria, such as missing an \"@\" for an email address."
        rule.setContent {
            TextDemo(2.dp,text)
        }

        rule.onNodeWithTag("textDemo").assertTextEquals(text)
        rule.onNodeWithText("This is a BasicText").assertTextEquals("This is a BasicText")

    }

    @Test
    fun inputField(){
        var text ="OutlinedTextField adds an outlined border to the input field, providing a modern and distinct appearance.Error Handling: The isError parameter highlights the field when the input doesn’t meet certain criteria, such as missing an \"@\" for an email address."

        rule.setContent {
            InputFieldsDemo()
        }
        rule.onNodeWithTag("normal_inp")
            .assertExists()
            .performTextInput(text)
        rule.onNodeWithTag("outline_inp")
            .assertTextEquals(text)

    }

    @Test
    fun verifySliderInitialValue() {
        rule.setContent {
            var sliderValue by remember { mutableStateOf(0f) }
            Slider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..100f, modifier = Modifier.testTag("slider"))

        }
        rule.onNodeWithTag("slider").assertExists().assert(SemanticsMatcher.expectValue(
            ProgressBarRangeInfo, ProgressBarRangeInfo(0f, 0f..100f, 0)
        ))
    }

    @Test
    fun verifySliderValueOnChange() {
        rule.setContent {
            var sliderValue by remember { mutableStateOf(0f) }
            Slider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..100f, modifier = Modifier.testTag("slider"))

        }
        rule.onNodeWithTag("slider").performSemanticsAction(SemanticsActions.SetProgress){
            it(20.0f)
        }
        rule.onNodeWithTag("slider").assertExists().assert(SemanticsMatcher.expectValue(
            ProgressBarRangeInfo, ProgressBarRangeInfo(20f, 0f..100f, 0)
        ))
    }

    @Test
    fun verifyDialogBox(){
        rule.setContent {
            ModalsAndPopupsDemo()
        }
        rule.onNodeWithText("Show Dialog").performClick()
        rule.onNode(matcher = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
    }


}