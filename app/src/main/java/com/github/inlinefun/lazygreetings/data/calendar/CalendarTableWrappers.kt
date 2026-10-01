package com.github.inlinefun.lazygreetings.data.calendar

// https://developer.android.com/identity/providers/calendar-provider#calendar
data class CalendarData(
    val id: Long,
    val accountName: String?,
    val displayName: String?,
    val ownerAccount: String?,
    val isPrimary: Boolean
)

// https://developer.android.com/identity/providers/calendar-provider#events
data class CalendarEvent(
    val id: Long,
    val instanceID: Long,
    val title: String?,
    val description: String?,
    val startTime: Long,
    val endTime: Long,
)

// https://developer.android.com/identity/providers/calendar-provider#reminders
data class CalendarEventReminder(
    val id: Long,
    val method: CalendarEventReminderMethod
)

// https://developer.android.com/identity/providers/calendar-provider#reminders
// The alarm method, as set on the server. One of:
//
//    METHOD_ALERT
//    METHOD_DEFAULT
//    METHOD_EMAIL
//    METHOD_SMS
enum class CalendarEventReminderMethod(
    val id: String
) {
    ALERT(id = "METHOD_ALERT"),
    DEFAULT(id = "METHOD_DEFAULT"),
    EMAIL(id = "METHOD_EMAIL"),
    SMS(id = "METHOD_SMS");

    companion object {
        fun get(method: String): CalendarEventReminderMethod {
            return entries
                .find { entry ->
                    entry.id.equals(other = method, ignoreCase = true)
                } ?: DEFAULT
        }
    }
}
