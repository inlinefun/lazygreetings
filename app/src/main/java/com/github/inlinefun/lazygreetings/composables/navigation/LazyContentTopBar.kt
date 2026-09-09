package com.github.inlinefun.lazygreetings.composables.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.LazyContentChoice

@Composable
fun <T> LazyContentTopBar(
    currentChoice: T,
    onNavigateAction: () -> Unit,
    modifier: Modifier = Modifier
) where T : NavKey, T : LazyContentChoice {
    TopAppBar(
        title = {
            AnimatedContent(
                targetState = when (currentChoice) {
                    LazyContentChoice.Calendar -> R.string.label_calendar
                    LazyContentChoice.GreetingCards -> R.string.label_greeting_cards
                }
            ) { title ->
                Text(
                    text = stringResource(id = title)
                )
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = stringResource(id = R.string.label_navigation),
                modifier = Modifier
                    .padding(all = 4.dp)
                    .clickable(
                        enabled = true,
                        onClick = onNavigateAction,
                        onClickLabel = stringResource(id = R.string.label_navigation)
                    )
                    .padding(all = 12.dp)
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun PreviewLazyContentTopBar() {
    LazyGreetingsTheme {
        LazyContentTopBar(
            currentChoice = LazyContentChoice.Calendar,
            onNavigateAction = { }
        )
    }
}