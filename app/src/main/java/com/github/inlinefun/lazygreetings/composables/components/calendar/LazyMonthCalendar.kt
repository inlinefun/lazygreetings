package com.github.inlinefun.lazygreetings.composables.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.components.common.LazyIconButton
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.asCalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDayOfWeek
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarMonthOfYear
import com.github.inlinefun.lazygreetings.data.generateCalendarGrid
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun LazyMonthCalendar(
    yearMonth: YearMonth,
    days: List<LazyCalendarDay>,
    selectedCalendarDay: LazyCalendarDay,
    onDaySelect: (LazyCalendarDay) -> Unit,
    onLastMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        LazyCalendarTopBar(
            onLastMonth = onLastMonth,
            onNextMonth = onNextMonth,
            month = LazyCalendarMonthOfYear.entries[yearMonth.month.value - 1]
        )
        LazyCalendarGrid(
            days = days,
            selectedCalendarDay = selectedCalendarDay,
            onDaySelect = onDaySelect
        )
    }
}

@Composable
private fun LazyCalendarTopBar(
    month: LazyCalendarMonthOfYear,
    onLastMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        LazyIconButton(
            icon = R.drawable.chevron_left,
            onClick = onLastMonth
        )
        Text(
            text = stringResource(id = month.label),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1.0f)
        )
        LazyIconButton(
            icon = R.drawable.chevron_right,
            onClick = onNextMonth
        )
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
        modifier = Modifier
            .fillMaxWidth()
    ) {
        this.drawDaysOfWeekStrip()
        item(
            span = { GridItemSpan(currentLineSpan = 7) }
        ) {
            Spacer(Modifier.height(16.dp))
        }
        items(items = days) { day ->
            val focused = selectedCalendarDay == day

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
                    focused -> MaterialTheme.colorScheme.primary
                    day.isToday -> MaterialTheme.colorScheme.surfaceVariant
                    else -> Color.Transparent
                }
            )
            val textColor by animateColorAsState(
                targetValue = when {
                    !day.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.8f
                    )

                    focused -> MaterialTheme.colorScheme.onPrimary
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
private fun PreviewLazyMonthCalendar() {
    val yearMonth = YearMonth.now()
    val calendarDay = LocalDate
        .now()
        .asCalendarDay(yearMonth)
    LazyGreetingsTheme {
        LazyMonthCalendar(
            yearMonth = yearMonth,
            days = yearMonth.generateCalendarGrid(),
            selectedCalendarDay = calendarDay,
            onDaySelect = { },
            onLastMonth = { },
            onNextMonth = { },
        )
    }
}
