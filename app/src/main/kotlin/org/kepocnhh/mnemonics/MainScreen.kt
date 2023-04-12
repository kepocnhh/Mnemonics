package org.kepocnhh.mnemonics

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.mnemonics.implementation.entity.formatted
import org.kepocnhh.mnemonics.implementation.module.main.MainViewModel
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.catchClicks
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.onClick
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.padding
import kotlin.time.Duration.Companion.milliseconds

@Composable
private fun ProgressBar() {
    val viewModel = App.viewModel<MainViewModel>()
    val state by viewModel.state.collectAsState()
    val env by viewModel.env.collectAsState()
    val animatable = remember { Animatable(initialValue = state.progress) }
    LaunchedEffect(state.number) {
        animatable.snapTo(state.progress)
    }
    LaunchedEffect(state.number, state.isPaused) {
        val time = env?.delay
        if (state.isPaused || time == null) {
            if (animatable.isRunning) animatable.stop()
        } else {
            val duration = time - time * state.progress.toDouble()
            val animationSpec = tween<Float>(
                durationMillis = duration.inWholeMilliseconds.toInt(),
                easing = LinearEasing
            )
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = animationSpec,
            )
        }
    }
    ProgressBarHorizontal(value = animatable.value)
}

@Composable
private fun ProgressBarHorizontal(value: Float) {
    Box(
        modifier = Modifier
            .height(App.Theme.dimensions.progress)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(App.Theme.colors.foreground)
                .fillMaxHeight()
                .fillMaxWidth(value)
        )
    }
}

@Composable
private fun PlayPauseButton() {
    val viewModel = App.viewModel<MainViewModel>()
    val state by viewModel.state.collectAsState()
    Box(
        modifier = Modifier
            .size(App.Theme.dimensions.button)
            .clickable {
                viewModel.pause(!state.isPaused)
            },
    ) {
        Image(
            modifier = Modifier
                .size(App.Theme.dimensions.icon)
                .align(Alignment.Center),
            painter = painterResource(
                id = if (state.isPaused) R.drawable.play else R.drawable.pause
            ),
            contentDescription = if (state.isPaused) App.Theme.strings.play else App.Theme.strings.pause,
            colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
        )
    }
}

@Composable
internal fun ToSettings(onBack: () -> Unit) {
    val TAG = "[ToSettings]"
    println("$TAG:\n\tcompose...")
    val orientation = LocalConfiguration.current.orientation
    val initialWidth = LocalConfiguration.current.screenWidthDp.dp + App.Theme.dimensions.insets.end
    val targetWidth = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> initialWidth / 2
        else -> initialWidth
    }
    val animatable = remember { Animatable(initialValue = 1f) }
    val delay = 250.milliseconds
    var back by remember { mutableStateOf(false) }
    val targetValue = if (back) 1f else 0f
    LaunchedEffect(back) {
        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = delay.inWholeMilliseconds.toInt(),
                easing = LinearEasing
            ),
        )
    }
    if (back) {
        if (animatable.value == targetValue) onBack()
    }
    println("$TAG: animatable: ${animatable.value}")
    println("$TAG: target value: $targetValue")
    Box(modifier = Modifier.fillMaxSize()) {
        val alpha = (1f - animatable.value) * 0.75f
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background.copy(alpha = alpha)) // todo black?
                .onClick {
                    if (!back) back = true
                },
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(targetWidth)
                .offset(x = initialWidth * animatable.value + (initialWidth - targetWidth))
                .catchClicks(),
        ) {
            SettingsScreen(
                onBack = {
                    if (!back) back = true
                }
            )
        }
    }
}

@Composable
private fun Toolbar(
    modifier: Modifier,
    state: MainViewModel.State,
    onPlayPause: () -> Unit,
    toSettings: () -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(App.Theme.dimensions.button)
                .clickable {
                    onPlayPause()
                },
        ) {
            Image(
                modifier = Modifier
                    .size(App.Theme.dimensions.icon)
                    .align(Alignment.Center),
                painter = painterResource(
                    id = if (state.isPaused) R.drawable.play else R.drawable.pause
                ),
                contentDescription = if (state.isPaused) App.Theme.strings.play else App.Theme.strings.pause,
                colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(App.Theme.dimensions.toolbar)
                .clickable {
                    toSettings()
                },
        ) {
            Image(
                modifier = Modifier
                    .size(App.Theme.dimensions.icon)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.gear),
                contentDescription = App.Theme.strings.settings,
                colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
            )
        }
    }
}

@Composable
private fun Toolbar(toSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = App.Theme.dimensions.insets.end,
                start = App.Theme.dimensions.insets.start,
                top = App.Theme.dimensions.insets.top,
            )
            .height(App.Theme.dimensions.toolbar),
    ) {
        Row(
            modifier = Modifier
                .size(App.Theme.dimensions.toolbar)
                .align(Alignment.TopEnd),
        ) {
            Box(
                modifier = Modifier
                    .size(App.Theme.dimensions.toolbar)
                    .clickable {
                        toSettings()
                    },
            ) {
                Image(
                    modifier = Modifier
                        .size(App.Theme.dimensions.icon)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.gear),
                    contentDescription = App.Theme.strings.settings,
                    colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
                )
            }
        }
    }
}

@Composable
private fun NextNumber() {
    val viewModel = App.viewModel<MainViewModel>()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.isPaused, state.number) {
        if (!state.isPaused) viewModel.nextNumber()
    }
}

@Composable
private fun MainScreenPortrait(toSettings: MutableState<Boolean>) {
    Box(Modifier.fillMaxSize()) {
        val viewModel = App.viewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()
        val env by viewModel.env.collectAsState()
        NextNumber()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val number = state.number
            val length = env?.length
            val text = if (number == null || length == null) {
                "---"
            } else {
                number.formatted(length = length)
            }
            Text(
                value = text,
                size = 128.sp,
                family = FontFamily.Monospace
            )
            ProgressBar()
        }
        Toolbar(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = App.Theme.dimensions.insets.bottom)
                .height(App.Theme.dimensions.toolbar)
                .align(Alignment.BottomStart),
            state = state,
            onPlayPause = {
                viewModel.pause(!state.isPaused)
            },
            toSettings = {
                viewModel.pause(true)
                toSettings.value = true
            }
        )
        if (toSettings.value) {
            ToSettings(
                onBack = {
                    toSettings.value = false
                }
            )
        }
    }
}

@Composable
private fun MainScreenLandscape(toSettings: MutableState<Boolean>) {
    Box(Modifier.fillMaxSize()) {
        val viewModel = App.viewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()
        val env by viewModel.env.collectAsState()
        NextNumber()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(App.Theme.dimensions.insets)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val number = state.number
                val length = env?.length
                val text = if (number == null || length == null) {
                    "---"
                } else {
                    number.formatted(length = length)
                }
                Text(
                    value = text,
                    size = 128.sp,
                    family = FontFamily.Monospace
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(App.Theme.dimensions.button * 2)
                    .padding(
                        start = App.Theme.dimensions.button,
                        end = App.Theme.dimensions.button,
                    )
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                ProgressBar()
            }
        }
        Toolbar(
            modifier = Modifier.fillMaxHeight()
                .padding(
                    top = App.Theme.dimensions.insets.top,
                    end = App.Theme.dimensions.insets.end,
                )
                .width(App.Theme.dimensions.toolbar)
                .align(Alignment.CenterEnd),
            state = state,
            onPlayPause = {
                viewModel.pause(!state.isPaused)
            },
            toSettings = {
                viewModel.pause(true)
                toSettings.value = true
            }
        )
        if (toSettings.value) {
            ToSettings(
                onBack = {
                    toSettings.value = false
                }
            )
        }
    }
}

@Composable
internal fun MainScreen() {
    val toSettings = rememberSaveable { mutableStateOf(false) }
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> MainScreenLandscape(toSettings)
        else -> MainScreenPortrait(toSettings)
    }
}
