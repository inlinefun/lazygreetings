package com.github.inlinefun.lazygreetings.composables.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyNavDisplay
import com.github.inlinefun.lazygreetings.composables.screens.AddCalendarEventScreen
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
            entry<LazyNavRoute.AddCalendarEvent>(
                metadata = metadata {
                    val offset = 400
                    put(NavDisplay.TransitionKey) {
                        val entryTransition = fadeIn() + slideInVertically { offset }
                        val exitTransition = ExitTransition.None
                        entryTransition togetherWith exitTransition
                    }
                    put(NavDisplay.PopTransitionKey) {
                        val entryTransition = EnterTransition.None
                        val exitTransition = fadeOut() + slideOutVertically { offset }
                        entryTransition togetherWith exitTransition
                    }
                    put(NavDisplay.PredictivePopTransitionKey) {
                        val entryTransition = EnterTransition.None
                        val exitTransition = fadeOut() + slideOutVertically { offset }
                        entryTransition togetherWith exitTransition
                    }
                },
            ) {
                AddCalendarEventScreen(
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
