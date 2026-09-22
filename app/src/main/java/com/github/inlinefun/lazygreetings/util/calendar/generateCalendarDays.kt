package com.github.inlinefun.lazygreetings.util.calendar

import com.github.inlinefun.lazygreetings.data.calendar.CalendarDay
import java.time.LocalDate
import java.time.YearMonth

fun generateCalendarDays(
    month: YearMonth,
    selected: LocalDate,
    today: LocalDate
): List<CalendarDay> {
    val firstOfMonth = month.atDay(1)
    val firstDayOfGrid = firstOfMonth.minusDays((firstOfMonth.dayOfWeek.value % 7).toLong())
    return List(size = 7 * 6) { offset ->
        val date = firstDayOfGrid.plusDays(offset.toLong())
        CalendarDay(
            date = date,
            isCurrentMonth = date.month == month.month,
            isSelected = date.isEqual(selected),
            isToday = date.isEqual(today),
        )
    }
}