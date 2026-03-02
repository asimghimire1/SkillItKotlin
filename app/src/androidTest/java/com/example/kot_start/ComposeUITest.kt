package com.example.kot_start

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented Compose UI tests.
 * Validates that core UI components render and respond to interaction.
 * Runs on a device or emulator.
 */
@RunWith(AndroidJUnit4::class)
class ComposeUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ======================== Basic Rendering ========================

    @Test
    fun basicTextRenders() {
        composeTestRule.setContent {
            MaterialTheme {
                Text("Welcome to Skillit")
            }
        }
        composeTestRule.onNodeWithText("Welcome to Skillit").assertIsDisplayed()
    }

    @Test
    fun buttonRendersAndIsClickable() {
        var clicked = false
        composeTestRule.setContent {
            MaterialTheme {
                Button(onClick = { clicked = true }) {
                    Text("Get Started")
                }
            }
        }
        composeTestRule.onNodeWithText("Get Started").assertIsDisplayed()
        composeTestRule.onNodeWithText("Get Started").performClick()
        assert(clicked)
    }

    // ======================== Theme Verification ========================

    @Test
    fun materialThemeAppliesWithoutCrash() {
        composeTestRule.setContent {
            com.example.kot_start.ui.theme.Kot_startTheme {
                Text("Themed Content")
            }
        }
        composeTestRule.onNodeWithText("Themed Content").assertIsDisplayed()
    }

    // ======================== Interactive Elements ========================

    @Test
    fun counterIncrementTest() {
        composeTestRule.setContent {
            MaterialTheme {
                var count by remember { mutableIntStateOf(0) }
                Column {
                    Text("Count: $count")
                    Button(onClick = { count++ }) {
                        Text("Increment")
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Count: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Increment").performClick()
        composeTestRule.onNodeWithText("Count: 1").assertIsDisplayed()
    }

    @Test
    fun multipleClicksUpdateState() {
        composeTestRule.setContent {
            MaterialTheme {
                var count by remember { mutableIntStateOf(0) }
                Column {
                    Text("Value: $count")
                    Button(onClick = { count++ }) {
                        Text("Add")
                    }
                }
            }
        }

        repeat(5) {
            composeTestRule.onNodeWithText("Add").performClick()
        }
        composeTestRule.onNodeWithText("Value: 5").assertIsDisplayed()
    }

    // ======================== Tab-like Navigation ========================

    @Test
    fun tabSwitchingDisplaysCorrectContent() {
        composeTestRule.setContent {
            MaterialTheme {
                var selectedTab by remember { mutableStateOf("Home") }
                Column {
                    // Tab buttons
                    Button(onClick = { selectedTab = "Home" }) { Text("Home") }
                    Button(onClick = { selectedTab = "Wallet" }) { Text("Wallet") }
                    Button(onClick = { selectedTab = "Bids" }) { Text("Bids") }

                    // Content based on tab
                    when (selectedTab) {
                        "Home" -> Text("Dashboard Content")
                        "Wallet" -> Text("Wallet Content")
                        "Bids" -> Text("Bids Content")
                    }
                }
            }
        }

        // Initially on Home
        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()

        // Switch to Wallet
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.onNodeWithText("Wallet Content").assertIsDisplayed()

        // Switch to Bids
        composeTestRule.onNodeWithText("Bids").performClick()
        composeTestRule.onNodeWithText("Bids Content").assertIsDisplayed()

        // Back to Home
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()
    }

    // ======================== Text Input ========================

    @Test
    fun searchInputAcceptsText() {
        composeTestRule.setContent {
            MaterialTheme {
                var text by remember { mutableStateOf("") }
                Column {
                    androidx.compose.material3.TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Search") }
                    )
                    if (text.isNotEmpty()) {
                        Text("Searching: $text")
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Searching: Kotlin").assertIsDisplayed()
    }

    // ======================== List Rendering ========================

    @Test
    fun listItemsRenderCorrectly() {
        val items = listOf("Kotlin Coroutines", "Firebase Mastery", "Compose Advanced")
        composeTestRule.setContent {
            MaterialTheme {
                Column {
                    items.forEach { item ->
                        Text(item)
                    }
                }
            }
        }

        items.forEach { item ->
            composeTestRule.onNodeWithText(item).assertIsDisplayed()
        }
    }

    // ======================== Conditional Rendering ========================

    @Test
    fun conditionalContentShowsOnToggle() {
        composeTestRule.setContent {
            MaterialTheme {
                var showDetails by remember { mutableStateOf(false) }
                Column {
                    Button(onClick = { showDetails = !showDetails }) {
                        Text(if (showDetails) "Hide Details" else "Show Details")
                    }
                    if (showDetails) {
                        Text("Session details are visible")
                    }
                }
            }
        }

        // Initially hidden
        composeTestRule.onNodeWithText("Session details are visible").assertDoesNotExist()

        // Show details
        composeTestRule.onNodeWithText("Show Details").performClick()
        composeTestRule.onNodeWithText("Session details are visible").assertIsDisplayed()

        // Hide details
        composeTestRule.onNodeWithText("Hide Details").performClick()
        composeTestRule.onNodeWithText("Session details are visible").assertDoesNotExist()
    }
}
