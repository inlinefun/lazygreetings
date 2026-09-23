package com.github.inlinefun.lazygreetings.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

const val TOTAL_CALENDAR_MONTHS: Int = (12 * 20 * 2) + 1
const val DEFAULT_CALENDAR_MONTH_OFFSET: Int = (TOTAL_CALENDAR_MONTHS / 2) + 1

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    val startingMonth: YearMonth = YearMonth.now()
    private val _today = MutableStateFlow(value = LocalDate.now())
    private val _selectedDate = MutableStateFlow(value = LocalDate.now())

    private val _currentMonthOffset = MutableStateFlow(value = DEFAULT_CALENDAR_MONTH_OFFSET)

    val today = _today.asStateFlow()
    val selectedDate = _selectedDate.asStateFlow()
    val currentMonthOffset = _currentMonthOffset.asStateFlow()
    val currentMonth = combine(
        _currentMonthOffset,
        transform = { (offset) ->
            val monthsToAdd = offset - DEFAULT_CALENDAR_MONTH_OFFSET
            return@combine startingMonth
                .plusMonths(monthsToAdd.toLong())
        }
    )
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = YearMonth.now()
        )

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

}