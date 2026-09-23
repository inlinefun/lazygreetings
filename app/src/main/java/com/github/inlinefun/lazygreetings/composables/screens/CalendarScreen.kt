package com.github.inlinefun.lazygreetings.composables.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.components.calendar.LazyCalendar
import com.github.inlinefun.lazygreetings.composables.components.calendar.LazyCalendarEvents
import com.github.inlinefun.lazygreetings.composables.components.common.LazyFloatingActionButton
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyCalendarAppbar
import com.github.inlinefun.lazygreetings.data.calendar.CalendarMonthOfYear
import com.github.inlinefun.lazygreetings.data.navigation.AddCalendarEventData
import com.github.inlinefun.lazygreetings.data.navigation.LazyNavRoute
import com.github.inlinefun.lazygreetings.data.viewmodels.CalendarViewModel
import com.github.inlinefun.lazygreetings.data.viewmodels.DEFAULT_CALENDAR_MONTH_OFFSET
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    navigateTo: (LazyNavRoute) -> Unit,
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel,
) {
    val startingMonth = calendarViewModel.startingMonth
    val today by calendarViewModel.today.collectAsStateWithLifecycle()
    val currentMonthOffset by calendarViewModel.currentMonthOffset.collectAsStateWithLifecycle()
    val currentMonth by calendarViewModel.currentMonth.collectAsStateWithLifecycle()
    val selectedDate by calendarViewModel.selectedDate.collectAsStateWithLifecycle()

    CalendarContent(
        startingMonth = startingMonth,
        currentMonth = currentMonth,
        selectedDate = selectedDate,
        navigateTo = navigateTo,
        today = today,
        currentMonthOffset = currentMonthOffset,
        updateMonthOffset = calendarViewModel::updateCurrentMonthOffset,
        onDaySelect = calendarViewModel::updateSelectedDate,
        modifier = modifier
    )
}

@Composable
private fun CalendarContent(
    today: LocalDate,
    selectedDate: LocalDate,
    currentMonthOffset: Int,
    startingMonth: YearMonth,
    currentMonth: YearMonth,
    updateMonthOffset: (Int) -> Unit,
    onDaySelect: (LocalDate) -> Unit,
    navigateTo: (LazyNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyCalendarAppbar(
                title = {
                    Row {
                        AnimatedContent(
                            targetState = currentMonth,
                            transitionSpec = {
                                val isForward = targetState > initialState
                                val enterTransition = if (isForward) {
                                    slideInVertically { it } + fadeIn()
                                } else {
                                    slideInVertically { -it } + fadeIn()
                                }
                                val exitTransition = if (isForward) {
                                    slideOutVertically { -it } + fadeOut()
                                } else {
                                    slideOutVertically { it } + fadeOut()
                                }
                                enterTransition togetherWith exitTransition
                            }
                        ) { yearMonth ->
                            val month = CalendarMonthOfYear
                                .entries[yearMonth.month.value - 1]
                            Text(
                                text = stringResource(id = month.label)
                            )
                        }
                        Text(
                            text = " "
                        )
                        currentMonth
                            .year
                            .toString()
                            .forEach { char ->
                                AnimatedContent(
                                    targetState = char,
                                    transitionSpec = {
                                        val isForward = targetState > initialState
                                        val enterTransition = if (isForward) {
                                            slideInVertically { it } + fadeIn()
                                        } else {
                                            slideInVertically { -it } + fadeIn()
                                        }
                                        val exitTransition = if (isForward) {
                                            slideOutVertically { -it } + fadeOut()
                                        } else {
                                            slideOutVertically { it } + fadeOut()
                                        }
                                        enterTransition togetherWith exitTransition
                                    }
                                ) {
                                    Text(
                                        text = "$it"
                                    )
                                }
                            }
                    }
                },
                navigateTo = navigateTo
            )
        },
        floatingActionButton = {
            LazyFloatingActionButton(
                icon = R.drawable.add,
                onClick = {
                    val dateInEpochTime = selectedDate.toEpochDay()
                    navigateTo(
                        LazyNavRoute.AddCalendarEvent(
                            data = AddCalendarEventData(
                                date = dateInEpochTime
                            )
                        )
                    )
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(all = 8.dp)
        ) {
            LazyCalendar(
                today = today,
                selectedDate = selectedDate,
                startMonth = startingMonth,
                currentMonthOffset = currentMonthOffset,
                updateMonthOffset = updateMonthOffset,
                onDaySelect = onDaySelect,
            )
            LazyCalendarEvents()
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    val month = YearMonth.now()
    val day = LocalDate.now()
    LazyGreetingsTheme {
        CalendarContent(
            navigateTo = { },
            currentMonth = month,
            startingMonth = month,
            today = day,
            selectedDate = day,
            currentMonthOffset = DEFAULT_CALENDAR_MONTH_OFFSET,
            updateMonthOffset = { },
            onDaySelect = { },
        )
    }
}