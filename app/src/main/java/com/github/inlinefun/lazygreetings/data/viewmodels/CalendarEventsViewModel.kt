package com.github.inlinefun.lazygreetings.data.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.github.inlinefun.lazygreetings.data.calendar.CalendarData
import com.github.inlinefun.lazygreetings.data.calendar.CalendarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.YearMonth
import java.time.ZoneOffset

private val TIME_OFFSET = ZoneOffset.UTC
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

private val CALENDAR_EVENT_QUERY_PROJECTION = arrayOf(
    CalendarContract.Events._ID,                            // 0
    CalendarContract.Events.TITLE,                          // 1
    CalendarContract.Events.DESCRIPTION,                    // 2
    CalendarContract.Events.DTSTART,                        // 3
    CalendarContract.Events.DTEND                           // 4
)
private const val CALENDAR_EVENT_QUERY_PROJECTION_ID_INDEX = 0
private const val CALENDAR_EVENT_QUERY_PROJECTION_TITLE_INDEX = 1
private const val CALENDAR_EVENT_QUERY_PROJECTION_DESCRIPTION_INDEX = 2
private const val CALENDAR_EVENT_QUERY_PROJECTION_DTSTART_INDEX = 3
private const val CALENDAR_EVENT_QUERY_PROJECTION_DTEND_INDEX = 4

@HiltViewModel
class CalendarEventsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(
    application = application
) {

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

    fun getCalendarEventsInMonth(calendarID: Long, month: YearMonth): List<CalendarEvent>? {
        // TODO: A whole lot to do. SQL!!!

        // looks like the time has to be in nanosecond precision for comparison,
        // yet it fails because for some reason google calendar decides that a birthday
        // gets added to the previous year, instead of the current
        // which is that it adds an event on 2025, if it was added in 2026
        // this needs a lot of thought
        // and, to be fair this does require checking for repeatable events and such
        // so this wasn't a complete solution

//        val startTimeInMillis = month
//            .atDay(1)
//            .atTime(0, 0, 0, 0)
//            .toEpochSecond(TIME_OFFSET)
//        val endTimeInMillis = month
//            .atEndOfMonth()
//            .atTime(23, 59, 59, 999)
//            .toEpochSecond(TIME_OFFSET)

        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "(" +
                "(${CalendarContract.Events.CALENDAR_ID} = ?)" +
//                " AND " +
//                "(${CalendarContract.Events.DTSTART} > ?)" +
//                " AND " +
//                "(${CalendarContract.Events.DTEND} < ?)" +
                ")"
        val selectionArgs = arrayOf(
            "$calendarID",
//            "$startTimeInMillis",
//            "$endTimeInMillis"
        )
        return calendarPermittedAction {
            application
                .contentResolver
                .query(uri, CALENDAR_EVENT_QUERY_PROJECTION, selection, selectionArgs, null)
                ?.use { cursor ->
                    val events = mutableListOf<CalendarEvent>()
                    while (cursor.moveToNext()) {
                        val eventID = cursor
                            .getLong(CALENDAR_EVENT_QUERY_PROJECTION_ID_INDEX)
                        val title = cursor
                            .getStringOrNull(CALENDAR_EVENT_QUERY_PROJECTION_TITLE_INDEX)
                        val description = cursor
                            .getStringOrNull(CALENDAR_EVENT_QUERY_PROJECTION_DESCRIPTION_INDEX)
                        val startTime = cursor
                            .getLong(CALENDAR_EVENT_QUERY_PROJECTION_DTSTART_INDEX)
                        val endTime = cursor
                            .getLong(CALENDAR_EVENT_QUERY_PROJECTION_DTEND_INDEX)

                        CalendarEvent(
                            id = eventID,
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
