package com.github.inlinefun.lazygreetings.composables.screens.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.str_not_yet_implemented)
        )
    }
}

@Preview
@Composable
private fun PreviewCalendarContent() {
    LazyGreetingsTheme {
        CalendarContent()
    }
}
