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
fun AddEventScreen(
    data: AddCalendarEventData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AddEventContent(
        data = data,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun AddEventContent(
    data: AddCalendarEventData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyTopAppbar(
                title = R.string.label_add_event,
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
        AddEventContent(
            data = AddCalendarEventData(
                date = today.toEpochDay()
            ),
            onBack = { },
            modifier = Modifier
        )
    }
}
