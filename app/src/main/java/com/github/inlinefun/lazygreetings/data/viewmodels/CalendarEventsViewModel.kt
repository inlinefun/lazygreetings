package com.github.inlinefun.lazygreetings.data.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import androidx.core.database.getIntOrNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import com.github.inlinefun.lazygreetings.data.calendar.CalendarData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

private val CALENDAR_QUERY_EVENT_PROJECTION = arrayOf(
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
                .query(uri, CALENDAR_QUERY_EVENT_PROJECTION, selection, selectionArgs, null)
                ?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val calendarID = cursor.getLong(CALENDAR_QUERY_PROJECTION_ID_INDEX)
                        val accountName =
                            cursor.getString(CALENDAR_QUERY_PROJECTION_ACCOUNT_NAME_INDEX)
                        val displayName =
                            cursor.getString(CALENDAR_QUERY_PROJECTION_CALENDAR_DISPLAY_NAME_INDEX)
                        val ownerAccount =
                            cursor.getString(CALENDAR_QUERY_PROJECTION_OWNER_ACCOUNT_INDEX)
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
