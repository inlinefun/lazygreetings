package com.github.inlinefun.lazygreetings.composables.components.common

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.misc.LazyGreetingsTheme

@Composable
fun LazyIconButton(
    @DrawableRes
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun PreviewLazyIconButton() {
    LazyGreetingsTheme {
        LazyIconButton(
            icon = R.drawable.arrow_back,
            onClick = { }
        )
    }
}
