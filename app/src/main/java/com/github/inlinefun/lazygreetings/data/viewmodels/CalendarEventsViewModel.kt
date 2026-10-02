package com.github.inlinefun.lazygreetings.data.viewmodels

import android.Manifest
import android.app.Application
import android.content.ContentUris
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.github.inlinefun.lazygreetings.data.calendar.CalendarData
import com.github.inlinefun.lazygreetings.data.calendar.CalendarEventInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.YearMonth
import java.time.ZoneOffset

private val CALENDAR_QUERY_PROJECTION = arrayOf(
    CalendarContract.Calendars._ID,                         // 0
    CalendarContract.Calendars.ACCOUNT_NAME,                // 1
    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,       // 2
    CalendarContract.Calendars.OWNER_ACCOUNT,               // 3
    CalendarContract.Calendars.IS_PRIMARY                   // 4
)
private const val CALENDAR_QUERY_PROJECTION_ID_INDEX = 0
private const val CALENDAR_QUERY_PROJECTION_ACCOUNT_NAME_INDEX = 1
private const val CALENDAR_QUERY_PROJECTION_CALENDAR_DISPLAY_NAME_INDEX = 2
private const val CALENDAR_QUERY_PROJECTION_OWNER_ACCOUNT_INDEX = 3
private const val CALENDAR_QUERY_PROJECTION_IS_PRIMARY_INDEX = 4

private val CALENDAR_INSTANCE_QUERY_PROJECTION = arrayOf(
    CalendarContract.Instances._ID,                         // 0
    CalendarContract.Instances.EVENT_ID,                    // 1
    CalendarContract.Instances.TITLE,                       // 2
    CalendarContract.Instances.DESCRIPTION,                 // 3
    CalendarContract.Instances.BEGIN,                       // 4
    CalendarContract.Instances.END                          // 5
)
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_ID_INDEX = 0
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_EVENT_ID_INDEX = 1
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_TITLE_INDEX = 2
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_DESCRIPTION_INDEX = 3
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_BEGIN_INDEX = 4
private const val CALENDAR_INSTANCE_QUERY_PROJECTION_END_INDEX = 5

private val TIME_OFFSET = ZoneOffset.UTC

@HiltViewModel
class CalendarEventsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(
    application = application
) {

    private val _primaryCalendar = MutableStateFlow<CalendarData?>(value = null)
    private val _currentEventInstances = MutableStateFlow<List<CalendarEventInstance>?>(value = null)

    val primaryCalendar = _primaryCalendar.asStateFlow()
    val currentEventInstances = _currentEventInstances.asStateFlow()

    fun refreshEventInstances(
        currentMonth: YearMonth
    ) {
        _primaryCalendar.value = getPrimaryCalendar()
        primaryCalendar.value?.let { calendar ->
            _currentEventInstances.value = getCalendarEventsInMonth(
                calendarID = calendar.id,
                month = currentMonth
            )
        }
    }

    fun getPrimaryCalendar(): CalendarData? {
        val uri = CalendarContract.Calendars.CONTENT_URI
        val selection = "(" + "(${CalendarContract.Calendars.IS_PRIMARY} = ?)" + ")"
        val selectionArgs = arrayOf(
            "1"
        )
        return calendarPermittedAction {
            application
                .contentResolver
                .query(uri, CALENDAR_QUERY_PROJECTION, selection, selectionArgs, null)
                ?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val calendarID = cursor.getLong(CALENDAR_QUERY_PROJECTION_ID_INDEX)
                        val accountName =
                            cursor.getStringOrNull(CALENDAR_QUERY_PROJECTION_ACCOUNT_NAME_INDEX)
                        val displayName =
                            cursor.getStringOrNull(
                                CALENDAR_QUERY_PROJECTION_CALENDAR_DISPLAY_NAME_INDEX
                            )
                        val ownerAccount =
                            cursor.getStringOrNull(CALENDAR_QUERY_PROJECTION_OWNER_ACCOUNT_INDEX)
                        val isPrimary =
                            cursor.getIntOrNull(CALENDAR_QUERY_PROJECTION_IS_PRIMARY_INDEX)
                        val isPrimaryButInBoolean = isPrimary == 1
                        if (isPrimaryButInBoolean) {
                            return CalendarData(
                                id = calendarID,
                                accountName = accountName,
                                displayName = displayName,
                                ownerAccount = ownerAccount,
                                isPrimary = true
                            )
                        }
                    }
                }
            null
        }
    }

    fun getCalendarEventsInMonth(calendarID: Long, month: YearMonth): List<CalendarEventInstance>? {
        val startTimeInMillis = month
            .atDay(1)
            .atTime(0, 0, 0)
            .toEpochSecond(TIME_OFFSET)
            .times(1000)
        val endTimeInMillis = month
            .atEndOfMonth()
            .atTime(23, 59, 59)
            .toEpochSecond(TIME_OFFSET)
            .times(1000)

        val uri = CalendarContract.Instances.CONTENT_URI.buildUpon()
            .also { builder ->
                ContentUris.appendId(builder, startTimeInMillis)
                ContentUris.appendId(builder, endTimeInMillis)
            }
            .build()
        val selection = "${CalendarContract.Instances.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf(
            "$calendarID"
        )
        val sortOrder = "${CalendarContract.Instances.BEGIN} ASC"
        return calendarPermittedAction {
            application
                .contentResolver
                .query(uri, CALENDAR_INSTANCE_QUERY_PROJECTION, selection, selectionArgs, sortOrder)
                ?.use { cursor ->
                    val events = mutableListOf<CalendarEventInstance>()
                    while (cursor.moveToNext()) {
                        val instanceID = cursor
                            .getLong(CALENDAR_INSTANCE_QUERY_PROJECTION_ID_INDEX)
                        val eventID = cursor
                            .getLong(CALENDAR_INSTANCE_QUERY_PROJECTION_EVENT_ID_INDEX)
                        val title = cursor
                            .getStringOrNull(CALENDAR_INSTANCE_QUERY_PROJECTION_TITLE_INDEX)
                        val description = cursor
                            .getStringOrNull(CALENDAR_INSTANCE_QUERY_PROJECTION_DESCRIPTION_INDEX)
                        val startTime = cursor
                            .getLong(CALENDAR_INSTANCE_QUERY_PROJECTION_BEGIN_INDEX)
                        val endTime = cursor
                            .getLong(CALENDAR_INSTANCE_QUERY_PROJECTION_END_INDEX)

                        CalendarEventInstance(
                            eventID = eventID,
                            instanceID = instanceID,
                            title = title,
                            description = description,
                            startTime = startTime,
                            endTime = endTime
                        ).let { event ->
                            events.add(event)
                        }
                    }
                    return events
                }
        }
    }

    private fun hasCalendarPermissions(): Boolean {
        return ContextCompat
            .checkSelfPermission(
                application,
                Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
    }

    private inline fun <T> calendarPermittedAction(action: () -> T): T? {
        return if (hasCalendarPermissions()) {
            action()
        } else {
            null
        }
    }

}
