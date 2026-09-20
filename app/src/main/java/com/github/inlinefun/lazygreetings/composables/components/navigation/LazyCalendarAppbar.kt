package com.github.inlinefun.lazygreetings.composables.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.navigation.LazyNavRoute

@Composable
fun LazyCalendarAppbar(
    title: @Composable () -> Unit,
    navigateTo: (LazyNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier,
        title = title,
        actions = {
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.more_vert),
                    contentDescription = stringResource(id = R.string.label_more),
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                expanded = !expanded
                            },
                            onClickLabel = stringResource(id = R.string.label_more)
                        )
                        .padding(12.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    LazyDropdownMenuItem(
                        label = R.string.label_settings,
                        icon = R.drawable.settings,
                        onClick = {
                            expanded = false
                            navigateTo(LazyNavRoute.Settings)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun LazyDropdownMenuItem(
    label: Int,
    icon: Int,
    onClick: () -> Unit
) {
    val label = stringResource(id = label)
    DropdownMenuItem(
        onClick = onClick,
        text = {
            Text(
                text = label
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label
            )
        }
    )
}

@Preview
@Composable
private fun PreviewComponent() {
    LazyGreetingsTheme {
        LazyCalendarAppbar(
            title = {
                Text(
                    text = "January 2027"
                )
            },
            navigateTo = { }
        )
    }
}

