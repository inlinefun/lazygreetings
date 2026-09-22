package com.github.inlinefun.lazygreetings.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyNavDisplay
import com.github.inlinefun.lazygreetings.composables.screens.AddEventScreen
import com.github.inlinefun.lazygreetings.composables.screens.CalendarScreen
import com.github.inlinefun.lazygreetings.composables.screens.SettingsScreen
import com.github.inlinefun.lazygreetings.data.navigation.LazyNavRoute
import com.github.inlinefun.lazygreetings.data.viewmodels.CalendarViewModel

@Composable
fun LazyNavigationHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(LazyNavRoute.Calendar)
    LazyNavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<LazyNavRoute.Calendar> {
                val calendarViewModel = hiltViewModel<CalendarViewModel>()
                CalendarScreen(
                    navigateTo = backStack::add,
                    calendarViewModel = calendarViewModel
                )
            }
            entry<LazyNavRoute.AddCalendarEvent> { route ->
                AddEventScreen(
                    data = route.data,
                    onBack = backStack::removeLastOrNull
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
