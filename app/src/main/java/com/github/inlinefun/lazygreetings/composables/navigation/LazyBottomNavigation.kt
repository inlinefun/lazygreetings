package com.github.inlinefun.lazygreetings.composables.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.LazyContentChoice


private data class LazyBottomNavigationBarItem(
    @field:DrawableRes
    val icon: Int,
    @field:StringRes
    val label: Int,
    val choice: LazyContentChoice
)

@Composable
fun LazyBottomNavigationBar(
    currentChoice: NavKey,
    switchTo: (LazyContentChoice) -> Unit
) {
    val items = listOf(
        LazyBottomNavigationBarItem(
            icon = R.drawable.calendar_month,
            label = R.string.label_calendar,
            choice = LazyContentChoice.Calendar
        ),
        LazyBottomNavigationBarItem(
            icon = R.drawable.cards_stack,
            label = R.string.label_greeting_cards,
            choice = LazyContentChoice.GreetingCards
        )
    )
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentChoice == item.choice,
                onClick = {
                    switchTo(item.choice)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.label)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLazyBottomNavigationBar() {
    LazyGreetingsTheme {
        LazyBottomNavigationBar(
            currentChoice = LazyContentChoice.Calendar,
            switchTo = { }
        )
    }
}
