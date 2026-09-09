package com.github.inlinefun.lazygreetings.composables.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyNavDisplay
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.navigation.LazyBottomNavigationBar
import com.github.inlinefun.lazygreetings.composables.navigation.LazyContentTopBar
import com.github.inlinefun.lazygreetings.composables.screens.content.CalendarContent
import com.github.inlinefun.lazygreetings.composables.screens.content.GreetingCardsContent
import com.github.inlinefun.lazygreetings.data.LazyContentChoice

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier
) {
    val contentBackStack = rememberNavBackStack(LazyContentChoice.Calendar)
    val currentChoice by remember {
        derivedStateOf(contentBackStack::last)
    }
    Scaffold(
        topBar = {
            LazyContentTopBar(
                // not a very good idea
                currentChoice = currentChoice as LazyContentChoice,
                onNavigateAction = { }
            )
        },
        bottomBar = {
            LazyBottomNavigationBar(
                currentChoice = currentChoice,
                switchTo = { choice ->
                    when (choice) {
                        LazyContentChoice.Calendar -> contentBackStack.removeAll { it != choice }
                        else -> contentBackStack.add(index = 1, element = choice)
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyNavDisplay(
            backStack = contentBackStack,
            modifier = Modifier
                .padding(paddingValues),
            entryProvider = entryProvider {
                entry<LazyContentChoice.Calendar> {
                    CalendarContent()
                }
                entry<LazyContentChoice.GreetingCards> {
                    GreetingCardsContent()
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewContentScreen() {
    LazyGreetingsTheme {
        ContentScreen()
    }
}