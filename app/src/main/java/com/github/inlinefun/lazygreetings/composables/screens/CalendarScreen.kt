package com.github.inlinefun.lazygreetings.composables.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.components.calendar.LazyCalendar
import com.github.inlinefun.lazygreetings.composables.components.common.LazyFloatingActionButton
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyCalendarAppbar
import com.github.inlinefun.lazygreetings.data.calendar.CalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.CalendarMonthOfYear
import com.github.inlinefun.lazygreetings.data.navigation.LazyNavRoute
import com.github.inlinefun.lazygreetings.data.viewmodels.CalendarViewModel
import com.github.inlinefun.lazygreetings.util.calendar.generateCalendarDays
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    navigateTo: (LazyNavRoute) -> Unit,
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentMonth by calendarViewModel.currentMonth.collectAsStateWithLifecycle()
    val selectedDate by calendarViewModel.selectedDate.collectAsStateWithLifecycle()
    val calendarDays by calendarViewModel.calendarDays.collectAsStateWithLifecycle()

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                calendarViewModel.refreshData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CalendarContent(
        days = calendarDays,
        currentMonth = currentMonth,
        selectedDate = selectedDate,
        navigateTo = navigateTo,
        onLastMonth = calendarViewModel::lastMonth,
        onNextMonth = calendarViewModel::nextMonth,
        onDaySelect = calendarViewModel::updateSelectedDate,
        modifier = modifier
    )
}

@Composable
private fun CalendarContent(
    days: List<CalendarDay>,
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    navigateTo: (LazyNavRoute) -> Unit,
    onLastMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelect: (CalendarDay) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyCalendarAppbar(
                title = {
                    val month = CalendarMonthOfYear
                        .entries[currentMonth.month.value - 1]
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = 6.dp)
                    ) {
                        AnimatedContent(
                            targetState = month.label
                        ) {
                            Text(
                                text = stringResource(id = it)
                            )
                        }
                        AnimatedContent(
                            targetState = currentMonth.year
                        ) {
                            Text(
                                text = it.toString()
                            )
                        }
                    }
                },
                navigateTo = navigateTo
            )
        },
        floatingActionButton = {
            LazyFloatingActionButton(
                icon = R.drawable.add,
                // TODO: make this add a calendar event
                onClick = { }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            LazyCalendar(
                onLastMonth = onLastMonth,
                onNextMonth = onNextMonth,
                days = days,
                onDaySelect = onDaySelect,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    val month = YearMonth.now()
    val day = LocalDate.now()
    val days = generateCalendarDays(
        month = month,
        selected = day,
        today = day
    )
    LazyGreetingsTheme {
        CalendarContent(
            days = days,
            navigateTo = { },
            currentMonth = month,
            selectedDate = day,
            onLastMonth = { },
            onNextMonth = { },
            onDaySelect = { }
        )
    }
}