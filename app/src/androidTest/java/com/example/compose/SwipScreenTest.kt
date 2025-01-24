package com.example.compose

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SwipScreenTest {
    @get:Rule
    var rule = createComposeRule()
    var mockViewModel = mock(HomeViewModel::class.java)

    @Before
    fun init(){
        val itemsFlow = MutableStateFlow(emptyList<String>())
        mockViewModel.items=itemsFlow
    }

    @Test
    fun verifyEmptyListState(){

        rule.setContent {
            HomeScreen(viewModel = mockViewModel)
        }
        rule.onNodeWithTag("item_0").assertDoesNotExist()
        rule.onNodeWithTag("item_1").assertDoesNotExist()
    }

    @Test
    fun verifyInitListState(){
        rule.setContent {
            MyApp()
        }
        rule.onNodeWithTag("swipNav").performClick()
        rule.onNodeWithTag("item_0").assertExists()
        rule.onNodeWithTag("item_1").assertExists()
    }

    @Test
    fun verifyAddItem(){
        rule.setContent {
            MyApp()
        }
        rule.onNodeWithTag("swipNav").performClick()
        rule.onNodeWithText("Add Item").performClick()
        rule.onNodeWithTag("item_2").assertExists()
    }

    @Test
    fun verifyMultipleAddItem(){
        rule.setContent {
            MyApp()
        }
        rule.onNodeWithTag("swipNav").performClick()
        repeat(2){
            rule.onNodeWithText("Add Item").performClick()
        }
        rule.onNodeWithTag("item_3").assertExists()
    }

    @Test
    fun swipToElement(){
        rule.setContent {
            MyApp()
        }
        rule.onNodeWithTag("swipNav").performClick()
        repeat(20){
            rule.onNodeWithText("Add Item").performClick()
        }
        rule.onNodeWithTag("listView").performScrollToNode(matcher = hasText("#16",substring = true))
        rule.onNodeWithTag("listView").performScrollToNode(matcher = hasText("#9",substring = true))
    }
}