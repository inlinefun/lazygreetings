package com.github.inlinefun.lazygreetings.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.composables.components.common.LazyFloatingActionButton
import com.github.inlinefun.lazygreetings.composables.components.navigation.LazyCalendarAppbar
import com.github.inlinefun.lazygreetings.data.navigation.LazyNavRoute

@Composable
fun CalendarScreen(
    navigateTo: (LazyNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            LazyCalendarAppbar(
                title = {
                    Text(
                        text = stringResource(id = R.string.label_calendar)
                    )
                },
                navigateTo = navigateTo
            )
        },
        floatingActionButton = {
            LazyFloatingActionButton(
                icon = R.drawable.add,
                // TODO: make this add a calendar event
                onClick = { }
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
        CalendarScreen(
            navigateTo = { }
        )
    }
}