package com.github.inlinefun.lazygreetings.composables.screens.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.inlinefun.lazygreetings.composables.components.calendar.LazyMonthCalendar
import com.github.inlinefun.lazygreetings.data.viewmodels.CalendarViewModel

@Composable
fun CalendarContent(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
) {
    val yearMonth by viewModel.yearMonth.collectAsState()
    val days by viewModel.days.collectAsState()
    val selectedCalendarDay by viewModel.selectedCalendarDay.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyMonthCalendar(
            yearMonth = yearMonth,
            days = days,
            selectedCalendarDay = selectedCalendarDay,
            onDaySelect = viewModel::selectCalendarDay,
            onLastMonth = viewModel::lastMonth,
            onNextMonth = viewModel::nextMonth
        )
    }
}
