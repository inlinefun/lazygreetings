package com.github.inlinefun.lazygreetings.data.calendar

import androidx.annotation.StringRes
import com.github.inlinefun.lazygreetings.R
import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean
)

enum class CalendarMonthOfYear(
    @field:StringRes
    val label: Int
) {
    JANUARY(label = R.string.month_january),
    FEBRUARY(label = R.string.month_february),
    MARCH(label = R.string.month_march),
    APRIL(label = R.string.month_april),
    MAY(label = R.string.month_may),
    JUNE(label = R.string.month_june),
    JULY(label = R.string.month_july),
    AUGUST(label = R.string.month_august),
    SEPTEMBER(label = R.string.month_september),
    OCTOBER(label = R.string.month_october),
    NOVEMBER(label = R.string.month_november),
    DECEMBER(label = R.string.month_december);
}

enum class CalendarDayOfWeek(
    @field:StringRes
    val label: Int
) {
    SUNDAY(label = R.string.day_sunday),
    MONDAY(label = R.string.day_monday),
    TUESDAY(label = R.string.day_tuesday),
    WEDNESDAY(label = R.string.day_wednesday),
    THURSDAY(label = R.string.day_thursday),
    FRIDAY(label = R.string.day_friday),
    SATURDAY(label = R.string.day_saturday);
}