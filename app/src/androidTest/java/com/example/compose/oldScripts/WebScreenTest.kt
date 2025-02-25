package com.example.compose.oldScripts

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.model.Atoms.getCurrentUrl
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import com.example.compose.WebScreen
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test

class WebScreenTest {

    @get:Rule
    var rule = createComposeRule()

    @Test
    fun verifyWebViewPresent(){
        rule.setContent {
            WebScreen()
        }
        rule.onNodeWithTag("web_view").assertExists()
    }

    @Test
    fun verifyWebPageURL(){
        rule.setContent {
            WebScreen()
        }
        onWebView()
            .check(webMatches(getCurrentUrl(), containsString("compose")));
    }

    @Test
    fun verifyClickActionWeb(){
        rule.setContent {
            WebScreen()
        }
        onWebView().withElement(findElement(Locator.LINK_TEXT,"View tutorial")).perform(webClick())
        onWebView().withElement(findElement(Locator.XPATH,"//h1[@id='jetpack-compose-tutorial']"))
            .check(webMatches(getText(), containsString("Tutorial")))
    }
}