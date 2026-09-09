package com.github.inlinefun.lazygreetings.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyNavDisplay
import com.github.inlinefun.lazygreetings.composables.screens.ContentScreen
import com.github.inlinefun.lazygreetings.composables.screens.SettingsScreen
import com.github.inlinefun.lazygreetings.data.LazyNavRoute

@Composable
fun LazyNavigationHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(LazyNavRoute.Content)
    LazyNavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<LazyNavRoute.Content> {
                ContentScreen(
                    navigateTo = backStack::add
                )
            }
            entry<LazyNavRoute.Settings> {
                SettingsScreen(
                    onBack = backStack::removeLastOrNull
                )
            }
        }
    )
}
