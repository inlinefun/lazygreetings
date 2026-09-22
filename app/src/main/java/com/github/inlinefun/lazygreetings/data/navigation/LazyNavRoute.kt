package com.github.inlinefun.lazygreetings.data.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface LazyNavRoute : NavKey {
    @Serializable
    data object Calendar : LazyNavRoute

    @Serializable
    data class AddCalendarEvent(
        val data: AddCalendarEventData
    ) : LazyNavRoute

    @Serializable
    data object Settings : LazyNavRoute
}
