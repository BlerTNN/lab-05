package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.listycity.ui.theme.ListyCityTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CityListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun addRenameAndDeleteCity() {
        val cities = mutableStateListOf<City>()
        composeRule.setContent {
            ListyCityTheme {
                CityListScreen(
                    cities = cities,
                    onAddCity = { cities.add(it.copy(documentId = "saved-city")) },
                    onUpdateCity = { old, updated ->
                        assertEquals("saved-city", old.documentId)
                        cities[cities.indexOf(old)] = updated.copy(documentId = old.documentId)
                    },
                    onDeleteCity = {
                        assertEquals("saved-city", it.documentId)
                        cities.remove(it)
                    }
                )
            }
        }

        composeRule.onNodeWithText("+").performClick()
        composeRule.onNodeWithText("City").performTextReplacement(" Edmonton ")
        composeRule.onNodeWithText("Province").performTextReplacement(" AB ")
        composeRule.onNodeWithText("ADD CITY").performClick()
        composeRule.onNodeWithText("Edmonton").assertExists().performClick()
        composeRule.onNodeWithText("Updated City").performTextReplacement("Calgary")
        composeRule.onNodeWithText("UPDATE CITY").performClick()
        composeRule.onNodeWithText("Calgary").assertExists()
        composeRule.onNodeWithText("Edmonton").assertDoesNotExist()

        composeRule.onNodeWithText("Delete").performClick()
        composeRule.onNodeWithText("CANCEL").performClick()
        composeRule.onNodeWithText("Calgary").assertExists()
        composeRule.onNodeWithText("Delete").performClick()
        composeRule.onNodeWithText("DELETE").performClick()
        composeRule.onNodeWithText("Calgary").assertDoesNotExist()
        composeRule.runOnIdle { assertTrue(cities.isEmpty()) }
    }
}
