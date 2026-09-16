package com.github.inlinefun.lazygreetings.data

import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

fun YearMonth.generateCalendarGrid(): List<LazyCalendarDay> {
    val today = LocalDate.now()
    val firstOfMonth = this.atDay(1)
    val lastOfMonth = this.atEndOfMonth()

    val firstDayOfWeek = DayOfWeek.SUNDAY
    val dayOfWeekOffset: Int = (firstOfMonth.dayOfWeek.value - firstDayOfWeek.value + 7) % 7

    val gridStart = firstOfMonth.minusDays(dayOfWeekOffset.toLong())
    val gridSize = if (dayOfWeekOffset + lastOfMonth.dayOfMonth > (7 * 5)) {
        7 * 6
    } else {
        7 * 5
    }

    return (0 until gridSize).map { dayOffset ->
        val date = gridStart
            .plusDays(dayOffset.toLong())
        LazyCalendarDay(
            date = date,
            isCurrentMonth = date.month == this@generateCalendarGrid.month,
            isToday = date == today
        )
    }
}

fun LocalDate.asCalendarDay(yearMonth: YearMonth): LazyCalendarDay {
    return LazyCalendarDay(
        date = this@asCalendarDay,
        isCurrentMonth = this@asCalendarDay.month == yearMonth.month,
        // cool stuff right here
        isToday = this@asCalendarDay == LocalDate.now(),
    )
}