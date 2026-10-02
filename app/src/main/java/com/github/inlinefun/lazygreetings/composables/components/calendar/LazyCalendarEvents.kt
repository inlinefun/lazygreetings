package com.github.inlinefun.lazygreetings.composables.components.calendar

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.inlinefun.lazygreetings.R
import com.github.inlinefun.lazygreetings.composables.common.LazyGreetingsTheme
import com.github.inlinefun.lazygreetings.data.calendar.CalendarEventInstance
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LazyCalendarEvents(
    instances: List<CalendarEventInstance>?,
    refreshEventInstances: () -> Unit,
    modifier: Modifier = Modifier
) {
    val calendarPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    )
    LaunchedEffect(
        key1 = calendarPermissionState
    ) {
        refreshEventInstances()
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.label_planned_events),
            style = MaterialTheme.typography.titleMedium
        )
        Crossfade(
            targetState = calendarPermissionState.allPermissionsGranted,
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
        ) { calendarPermissionStatus ->
            if (calendarPermissionStatus) {
                CalendarEventsList(
                    instances = instances
                )
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
    instances: List<CalendarEventInstance>?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Crossfade(
        targetState = instances,
        modifier = modifier
    ) { instances ->
        when {
            instances == null -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularWavyProgressIndicator()
                }
            }

            instances.isEmpty() -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.str_no_planned_events),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(
                            state = scrollState,
                            enabled = true
                        )
                ) {
                    instances
                        .forEach { event ->
                            CalendarEventItem(
                                event = event
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun CalendarEventItem(
    event: CalendarEventInstance,
    modifier: Modifier = Modifier,
    index: Int = 0,
    count: Int = 1
) {
    val instant = Instant
        .ofEpochMilli(event.startTime)
    val date = LocalDateTime
        .ofInstant(instant, ZoneOffset.UTC)
    SegmentedListItem(
        leadingContent = {
            val shape = CircleShape
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .height(54.dp)
                    .aspectRatio(ratio = 1.0f)
                    .padding(all = 2.dp)
                    .clip(shape)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "${date.dayOfMonth}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
        supportingContent = {
            event.description?.let { description ->
                if (description.isNotEmpty()) {
                    Text(
                        text = description
                    )
                }
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(all = 0.dp),
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count
        ),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = modifier
    ) {
        Text(
            text = event.title ?: "default title"
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionRequest(
    permissionState: MultiplePermissionsState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var triedAskingPermission by remember { mutableStateOf(false) }
    val deniedPermission by remember {
        derivedStateOf {
            !permissionState.shouldShowRationale && triedAskingPermission
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
                    permissionState.launchMultiplePermissionRequest()
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
    val month = YearMonth.now()
    val two = month
        .atDay(2)
        .toEpochDay()
    val eleven = month
        .atDay(11)
        .toEpochDay()
    val instances = listOf(
        CalendarEventInstance(
            eventID = 0L,
            instanceID = 0L,
            title = "very cool",
            description = null,
            startTime = two,
            endTime = 0L,
        ),
        CalendarEventInstance(
            eventID = 0L,
            instanceID = 0L,
            title = "awesome sauce",
            description = "Description",
            startTime = eleven,
            endTime = 0L,
        )
    )
    LazyGreetingsTheme {
        CalendarEventsList(
            instances = instances
        )
    }
}
