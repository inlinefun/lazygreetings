package com.github.inlinefun.lazygreetings.composables.components.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import java.time.YearMonth

@Composable
fun LazyCalendarEvents(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.planned_events),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun PreviewComponent() {
    LazyGreetingsTheme {
        LazyCalendarEvents()
    }
}
