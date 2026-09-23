package com.github.inlinefun.lazygreetings.composables.components.calendar

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LazyCalendarEvents(
    modifier: Modifier = Modifier
) {
    val calendarPermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_CALENDAR
    )
    Column(
        modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.label_planned_events),
            style = MaterialTheme.typography.titleMedium
        )
        Crossfade(
            targetState = calendarPermissionState.status,
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
        ) { calendarPermissionStatus ->
            if (calendarPermissionStatus.isGranted) {
                CalendarEventsList()
            } else {
                PermissionRequest(
                    permissionState = calendarPermissionState
                )
            }
        }
    }
}

@Composable
private fun CalendarEventsList(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.str_not_implemented),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionRequest(
    permissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var triedAskingPermission by remember { mutableStateOf(false) }
    val deniedPermission by remember {
        derivedStateOf {
            !permissionState.status.shouldShowRationale && triedAskingPermission
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {
        AnimatedContent(
            targetState = if (deniedPermission) {
                R.string.str_calender_permission_denied
            } else {
                R.string.str_request_calendar_permission
            }
        ) { labelID ->
            Text(
                text = stringResource(id = labelID),
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = {
                if (deniedPermission) {
                    Intent()
                        .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(
                            Uri.fromParts(
                                "package",
                                context.packageName,
                                null
                            )
                        )
                        .let { intent ->
                            context
                                .startActivity(intent)
                        }
                } else {
                    permissionState.launchPermissionRequest()
                }
                triedAskingPermission = true
            }
        ) {
            AnimatedContent(
                targetState = if (deniedPermission) {
                    R.string.label_open_settings
                } else {
                    R.string.label_grant_permission
                }
            ) { labelID ->
                Text(
                    text = stringResource(id = labelID)
                )
            }
        }
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
