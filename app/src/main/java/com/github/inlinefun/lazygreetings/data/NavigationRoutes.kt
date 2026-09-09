package com.github.inlinefun.lazygreetings.data

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface LazyNavRoute : NavKey {
    @Serializable
    data object Content : LazyNavRoute

    @Serializable
    data object Settings : LazyNavRoute
}

sealed interface LazyContentChoice : NavKey {
    @Serializable
    data object Calendar : LazyContentChoice

    @Serializable
    data object GreetingCards : LazyContentChoice
}
