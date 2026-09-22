package com.github.inlinefun.lazygreetings.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.inlinefun.lazygreetings.data.calendar.CalendarDay
import com.github.inlinefun.lazygreetings.util.calendar.generateCalendarDays
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

const val TOTAL_CALENDAR_MONTHS = (12 * 20 * 2) + 1
const val DEFAULT_CALENDAR_MONTH_OFFSET = (TOTAL_CALENDAR_MONTHS / 2) + 1

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _today = MutableStateFlow(value = LocalDate.now())
    private val _selectedDate = MutableStateFlow(value = LocalDate.now())
    private val _currentMonth = MutableStateFlow(value = YearMonth.now())

    private val _currentMonthOffset = MutableStateFlow(value = DEFAULT_CALENDAR_MONTH_OFFSET)

    val today = _today.asStateFlow()
    val selectedDate = _selectedDate.asStateFlow()
    val currentMonth = _currentMonth.asStateFlow()
    val currentMonthOffset = _currentMonthOffset.asStateFlow()
    val calendarDays: StateFlow<List<CalendarDay>> = combine(
        flow = _currentMonth,
        flow2 = _selectedDate,
        flow3 = _today,
        transform = { month, selected, today ->
            generateCalendarDays(month, selected, today)
        }
    )
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun refreshData() {
        if (_currentMonth.value == YearMonth.now()) {
            return
        }
        _currentMonth.update {
            YearMonth.now()
        }
    }

    fun updateCurrentMonthOffset(offset: Int) {
        _currentMonthOffset.update {
            offset
        }
    }

    fun updateSelectedDate(day: LocalDate) {
        _selectedDate.update {
            if (it.isEqual(day)) {
                _today.value
            } else {
                day
            }
        }
    }

    fun lastMonth() {
        _currentMonth.update { month ->
            month.minusMonths(1)
        }
    }

    fun nextMonth() {
        _currentMonth.update { month ->
            month.plusMonths(1)
        }
    }

}