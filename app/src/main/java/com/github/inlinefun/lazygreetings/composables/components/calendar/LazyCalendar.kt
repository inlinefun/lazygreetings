package com.github.inlinefun.lazygreetings.composables.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.calendar.CalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.CalendarDayOfWeek
import com.github.inlinefun.lazygreetings.data.viewmodels.DEFAULT_CALENDAR_MONTH_OFFSET
import com.github.inlinefun.lazygreetings.data.viewmodels.TOTAL_CALENDAR_MONTHS
import com.github.inlinefun.lazygreetings.util.calendar.generateCalendarDays
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun LazyCalendar(
    today: LocalDate,
    selectedDate: LocalDate,
    startMonth: YearMonth,
    currentMonthOffset: Int,
    updateMonthOffset: (Int) -> Unit,
    onDaySelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = currentMonthOffset,
        pageCount = { TOTAL_CALENDAR_MONTHS }
    )
    LaunchedEffect(pagerState) {
        snapshotFlow(
            block = pagerState::currentPage
        ).collect(
            collector = updateMonthOffset
        )
    }
    Column(
        modifier = modifier
            .padding(all = 8.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 2
        ) { pageOffset ->
            val monthsToAdd = pageOffset - DEFAULT_CALENDAR_MONTH_OFFSET
            val currentMonth = startMonth.plusMonths(monthsToAdd.toLong())
            val daysOfMonth by produceState<List<CalendarDay>?>(
                initialValue = null,
                key1 = currentMonth,
                key2 = selectedDate,
                key3 = today
            ) {
                value = generateCalendarDays(
                    month = currentMonth,
                    selected = selectedDate,
                    today = today
                )
            }
            daysOfMonth?.let { days ->
                CalendarGrid(
                    days = days,
                    onDaySelect = onDaySelect
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    days: List<CalendarDay>,
    onDaySelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 7),
        modifier = modifier
    ) {
        // weekdays
        items(
            items = CalendarDayOfWeek.entries.toTypedArray()
        ) { dayOfWeek ->
            Text(
                text = stringResource(id = dayOfWeek.label).substring(0..2),
                textAlign = TextAlign.Center
            )
        }
        // spacer
        item(
            span = { GridItemSpan(currentLineSpan = 7) }
        ) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        // days of month
        items(
            items = days,
        ) { day ->
            CalendarDayCell(
                day = day,
                onClick = {
                    onDaySelect(day.date)
                }
            )
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDay,
    onClick: () -> Unit
) {
    val borderRadiusPercent by animateIntAsState(
        targetValue = when {
            day.isSelected -> 50
            day.isToday -> 25
            else -> 50
        }
    )
    val textColor by animateColorAsState(
        targetValue = when {
            day.isSelected && !day.isCurrentMonth -> MaterialTheme.colorScheme.onPrimaryContainer
            day.isSelected -> MaterialTheme.colorScheme.onPrimary
            day.isToday -> MaterialTheme.colorScheme.primary
            !day.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> MaterialTheme.colorScheme.onSurface
        }
    )
    val background by animateColorAsState(
        targetValue = when {
            day.isSelected && !day.isCurrentMonth -> MaterialTheme.colorScheme.primaryContainer
            day.isSelected -> MaterialTheme.colorScheme.primary
            day.isToday -> MaterialTheme.colorScheme.surfaceVariant
            else -> Color.Transparent
        }
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            day.isSelected && !day.isCurrentMonth -> MaterialTheme.colorScheme.primaryContainer
            day.isSelected -> MaterialTheme.colorScheme.primary
            day.isToday -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        }
    )
    val shape = RoundedCornerShape(
        percent = borderRadiusPercent
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(ratio = 1.0f)
            .padding(all = 2.dp)
            .clip(shape)
            .background(color = background)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                enabled = day.isCurrentMonth,
                onClick = onClick
            )
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun PreviewComponent() {
    val date = LocalDate.now()
    val month = YearMonth.now()
    LazyGreetingsTheme {
        LazyCalendar(
            modifier = Modifier,
            onDaySelect = { },
            startMonth = month,
            currentMonthOffset = DEFAULT_CALENDAR_MONTH_OFFSET,
            updateMonthOffset = { },
            today = date,
            selectedDate = date
        )
    }
}
