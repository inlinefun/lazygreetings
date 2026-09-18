package com.github.inlinefun.lazygreetings.composables.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.asCalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDayOfWeek
import com.github.inlinefun.lazygreetings.data.generateCalendarGrid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth

const val MAX_CALENDAR_MONTH_PAGES = (12 * 50) + 1
const val DEFAULT_CALENDAR_MONTH_PAGE = (MAX_CALENDAR_MONTH_PAGES / 2)

@Composable
fun LazyScrollingMonthCalendar(
    yearMonth: YearMonth,
    selectedCalendarDay: LazyCalendarDay,
    onDaySelect: (LazyCalendarDay) -> Unit,
    onCalendarPageUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = DEFAULT_CALENDAR_MONTH_PAGE,
        pageCount = { MAX_CALENDAR_MONTH_PAGES }
    )
    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect { offset ->
            onCalendarPageUpdate(offset)
        }
    }
    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) { page ->
        val pageOffset = (page - DEFAULT_CALENDAR_MONTH_PAGE).toLong()
        val yearMonth = remember(pageOffset, yearMonth) {
            yearMonth.plusMonths(pageOffset)
        }
        val days by produceState<List<LazyCalendarDay>?>(
            initialValue = null,
            key1 = yearMonth
        ) {
            value = withContext(Dispatchers.Default) {
                yearMonth.generateCalendarGrid()
            }
        }

        days?.let {
            LazyCalendarGrid(
                days = it,
                selectedCalendarDay = selectedCalendarDay,
                onDaySelect = onDaySelect
            )
        }
    }
}

private fun LazyGridScope.drawDaysOfWeekStrip() {
    items(
        items = LazyCalendarDayOfWeek.entries.toTypedArray()
    ) { dayOfWeek ->
        Text(
            text = stringResource(dayOfWeek.label).substring(0, 3),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LazyCalendarGrid(
    days: List<LazyCalendarDay>,
    selectedCalendarDay: LazyCalendarDay,
    onDaySelect: (LazyCalendarDay) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 7),
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        this.drawDaysOfWeekStrip()
        item(
            span = { GridItemSpan(currentLineSpan = 7) }
        ) {
            Spacer(Modifier.height(16.dp))
        }
        items(
            items = days,
            key = { it.date }
        ) { day ->
            val focused = selectedCalendarDay.date == day.date

            val shapeRadius by animateIntAsState(targetValue = if (focused) 50 else 25)
            val borderColor by animateColorAsState(
                targetValue = if (day.isToday) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                }
            )
            val backgroundColor by animateColorAsState(
                targetValue = when {
                    focused && day.isCurrentMonth -> MaterialTheme.colorScheme.primary
                    focused && !day.isCurrentMonth -> MaterialTheme.colorScheme.primaryContainer
                    day.isToday -> MaterialTheme.colorScheme.surfaceVariant
                    else -> Color.Transparent
                }
            )
            val textColor by animateColorAsState(
                targetValue = when {
                    focused && day.isCurrentMonth -> MaterialTheme.colorScheme.onPrimary
                    focused && !day.isCurrentMonth -> MaterialTheme.colorScheme.onPrimaryContainer
                    !day.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.8f
                    )

                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            val shape = RoundedCornerShape(percent = shapeRadius)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(all = 2.dp)
                    .clip(shape)
                    .background(color = backgroundColor)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = shape
                    )
                    .clickable(
                        enabled = day.isCurrentMonth,
                        onClick = {
                            onDaySelect(day)
                        }
                    )
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = textColor
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun PreviewLazyScrollingMonthCalendar() {
    val yearMonth = YearMonth.now()
    val calendarDay = LocalDate
        .now()
        .asCalendarDay(yearMonth)
    LazyGreetingsTheme {
        LazyScrollingMonthCalendar(
            yearMonth = yearMonth,
            selectedCalendarDay = calendarDay,
            onDaySelect = { },
            onCalendarPageUpdate = { },
        )
    }
}
