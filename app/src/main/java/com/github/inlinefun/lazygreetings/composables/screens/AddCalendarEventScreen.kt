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
import com.github.inlinefun.lazygreetings.data.navigation.AddCalendarEventData
import java.time.LocalDate

@Composable
fun AddCalendarEventScreen(
    data: AddCalendarEventData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AddCalendarEventScreenContent(
        data = data,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun AddCalendarEventScreenContent(
    data: AddCalendarEventData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyTopAppbar(
                title = R.string.label_add_event,
                icon = R.drawable.close,
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
private fun PreviewContent() {
    val today = LocalDate.now()
    LazyGreetingsTheme {
        AddCalendarEventScreenContent(
            data = AddCalendarEventData(
                date = today.toEpochDay()
            ),
            onBack = { },
            modifier = Modifier
        )
    }
}
