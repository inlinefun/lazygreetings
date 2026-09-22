package com.github.inlinefun.lazygreetings.composables.components.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme

@Composable
fun LazyTopAppbar(
    @StringRes
    title: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int = R.drawable.arrow_back,
) = TopAppBar(
    modifier = modifier,
    title = {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleLargeEmphasized
        )
    },
    navigationIcon = {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = R.string.label_back),
            modifier = Modifier
                .padding(all = 4.dp)
                .clickable(
                    enabled = true,
                    onClick = onBack,
                    onClickLabel = stringResource(id = R.string.label_back)
                )
                .padding(12.dp)
        )
    }
)

@Preview
@Composable
private fun PreviewComponent() {
    LazyGreetingsTheme {
        LazyTopAppbar(
            title = R.string.label_settings,
            icon = R.drawable.arrow_back,
            onBack = { }
        )
    }
}
