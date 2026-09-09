package com.github.inlinefun.lazygreetings.composables.misc

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun LazyGreetingsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = MaterialExpressiveTheme(
    colorScheme = LocalContext.current.let { context ->
        when (useDarkTheme) {
            true -> dynamicDarkColorScheme(context = context)
            false -> dynamicLightColorScheme(context = context)
        }
    },
    content = content
)
