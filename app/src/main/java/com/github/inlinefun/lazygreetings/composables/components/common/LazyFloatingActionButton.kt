package com.github.inlinefun.lazygreetings.composables.components.common

import androidx.annotation.DrawableRes
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme

@Composable
fun LazyFloatingActionButton(
    @DrawableRes
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = FloatingActionButton(
    onClick = onClick,
    modifier = modifier
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = null
    )
}

@Preview
@Composable
private fun PreviewComponent() {
    LazyGreetingsTheme {
        LazyFloatingActionButton(
            icon = R.drawable.add,
            onClick = { }
        )
    }
}
