package com.github.inlinefun.lazygreetings.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyTopAppbar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyTopAppbar(
                title = R.string.label_settings,
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    LazyGreetingsTheme {
        SettingsScreen(
            onBack = { }
        )
    }
}
