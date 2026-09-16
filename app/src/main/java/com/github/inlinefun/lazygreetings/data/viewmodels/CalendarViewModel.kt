package com.github.inlinefun.lazygreetings.data.viewmodels

import androidx.lifecycle.ViewModel
import com.github.inlinefun.lazygreetings.data.asCalendarDay
import com.github.inlinefun.lazygreetings.data.calendar.LazyCalendarDay
import com.github.inlinefun.lazygreetings.data.generateCalendarGrid
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _yearMonth = MutableStateFlow(value = YearMonth.now())
    private val _days = MutableStateFlow<List<LazyCalendarDay>>(value = emptyList())

    // false data, gets init anyway, so it's "probably" fine
    private val _selectedCalendarDay = MutableStateFlow(
        value = LazyCalendarDay(
            date = LocalDate.now(),
            isToday = false,
            isCurrentMonth = false
        )
    )

    val yearMonth = _yearMonth.asStateFlow()
    val days = _days.asStateFlow()
    val selectedCalendarDay = _selectedCalendarDay.asStateFlow()

    init {
        YearMonth.now()
            .let { yearMonth ->
                yearMonth.updateCalendar()
                _selectedCalendarDay.value = LocalDate
                    .now()
                    .asCalendarDay(yearMonth)
            }
    }

    fun selectCalendarDay(day: LazyCalendarDay) {
        if (_selectedCalendarDay.value == day) {
            _selectedCalendarDay.value = LocalDate
                .now()
                .asCalendarDay(yearMonth.value)
        } else {
            _selectedCalendarDay.value = day
        }
    }

    fun nextMonth() {
        yearMonth.value
            .plusMonths(1)
            .updateCalendar()
    }

    fun lastMonth() {
        yearMonth.value
            .minusMonths(1)
            .updateCalendar()
    }

    private fun YearMonth.updateCalendar() {
        _yearMonth.value = this@updateCalendar
        _days.value = this@updateCalendar.generateCalendarGrid()
    }

}
