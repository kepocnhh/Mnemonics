package org.kepocnhh.mnemonics

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.publishOn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.kepocnhh.mnemonics.implementation.module.main.MainViewModel
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.catchClicks
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.onClick
import java.util.Random
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.padding

private val times = flow {
    while (true) {
        delay(64.milliseconds)
        emit(System.nanoTime().nanoseconds)
    }
}

private fun times(
    delay: Duration,
    start: Duration,
    target: Duration
) = flow {
    while (true) {
        delay(delay)
        val div = System.nanoTime().nanoseconds - start
        emit(div / target)
    }
}

@Composable
private fun ProgressBar(state: MainViewModel.State) {
    val animatable = remember { Animatable(initialValue = state.progress) }
    val duration = MainViewModel.time - MainViewModel.time * state.progress.toDouble()
    val animationSpec = tween<Float>(
        durationMillis = duration.inWholeMilliseconds.toInt(),
        easing = LinearEasing
    )
    LaunchedEffect(state.number) {
        animatable.snapTo(state.progress)
    }
    LaunchedEffect(state.number, state.isPaused) {
        if (state.isPaused) {
            if (animatable.isRunning) animatable.stop()
        } else {
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
private fun ProgressBarVertical(
    value: Float,
    foreground: Color = App.Theme.colors.foreground,
    background: Color = foreground.copy(alpha = 0.5f),
) {
    Box(
        modifier = Modifier
            .width(App.Theme.dimensions.progress)
            .background(background)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .background(foreground)
                .fillMaxWidth()
                .fillMaxHeight(value)
        )
    }
}

@Composable
private fun PlayPauseButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(App.Theme.dimensions.button * 2)
            .clickable {
                onClick()
            },
    ) {
        Image(
            modifier = Modifier
                .size(App.Theme.dimensions.icon)
                .align(Alignment.Center),
            painter = painterResource(icon),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
        )
    }
}

private fun MutableState<Long?>.getOrSet(newValue: Long): Long {
    val result = value
    if (result != null) return result
    value = newValue
    return newValue
}

@Composable
internal fun ToSettings(onBack: () -> Unit) {
    val TAG = "[ToSettings]"
    println("$TAG:\n\tcompose...")
    val orientation = LocalConfiguration.current.orientation
    val initialWidth = LocalConfiguration.current.screenWidthDp.dp
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
                .background(App.Theme.colors.background.copy(alpha = alpha))
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
private fun MainScreenPortrait() {
    Box(Modifier.fillMaxSize()) {
        val TAG = "[MS|P|${hashCode()}]"
        val viewModel = App.viewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()
        var toSettings by rememberSaveable { mutableStateOf(false) }
        Toolbar(
            toSettings = {
                viewModel.pause(true)
                toSettings = true
            }
        )
        LaunchedEffect(state.number, state.isPaused) {
            viewModel.nextNumber()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val text = when (val number = state.number) {
                null -> "..."
                -100 -> " 00"
                else -> {
                    if (number < 0) {
                        " %02d".format(number.absoluteValue)
                    } else {
                        "%03d".format(number)
                    }
                }
            } // todo theme strings
            Text(
                value = text,
                size = 128.sp,
                family = FontFamily.Monospace
            )
            ProgressBar(state)
            PlayPauseButton(
                icon = if (state.isPaused) R.drawable.play else R.drawable.pause,
                contentDescription = if (state.isPaused) App.Theme.strings.play else App.Theme.strings.pause,
                onClick = {
                    viewModel.pause(!state.isPaused)
                },
            )
        }
        if (toSettings) {
            ToSettings(
                onBack = {
                    toSettings = false
                }
            )
        }
    }
}

@Composable
private fun MainScreenLandscape() {
    Box(Modifier.fillMaxSize()) {
        val viewModel = App.viewModel<MainViewModel>()
        val state by viewModel.state.collectAsState()
        var toSettings by rememberSaveable { mutableStateOf(false) }
        Toolbar(
            toSettings = {
                viewModel.pause(true)
                toSettings = true
            }
        )
        LaunchedEffect(state.number, state.isPaused) {
            viewModel.nextNumber()
        }
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
                val text = when (val number = state.number) {
                    null -> "..."
                    -100 -> " 00"
                    else -> {
                        if (number < 0) {
                            " %02d".format(number.absoluteValue)
                        } else {
                            "%03d".format(number)
                        }
                    }
                } // todo theme strings
                Text(
                    value = text,
                    size = 128.sp,
                    family = FontFamily.Monospace
                )
                PlayPauseButton(
                    icon = if (state.isPaused) R.drawable.play else R.drawable.pause,
                    contentDescription = if (state.isPaused) App.Theme.strings.play else App.Theme.strings.pause,
                    onClick = {
                        viewModel.pause(!state.isPaused)
                    },
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
                ProgressBar(state)
            }
        }
        if (toSettings) {
            ToSettings(
                onBack = {
                    toSettings = false
                }
            )
        }
    }
}

@Composable
internal fun MainScreen() {
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> MainScreenLandscape()
        else -> MainScreenPortrait()
    }
}
