package org.kepocnhh.mnemonics

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.catchClicks
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation.onClick
import java.util.Random
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

private val random = Random()

private fun nextNumber(random: Random, max: Int, actual: Int?): Int {
    while (true) {
        val result = random.nextInt(max + 100) - 100
        if (result != actual) return result
    }
}

@Composable
private fun ProgressBar(
    value: Float
) {
    Box(
        modifier = Modifier
            .background(App.Theme.colors.foreground)
            .height(8.dp)
            .padding(end = App.Theme.dimensions.insets.end)
            .fillMaxWidth(value)
    )
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
internal fun MainScreen() {
    Box(Modifier.fillMaxSize()) {
        val TAG = "[MainScreen|${hashCode()}]"
        println("$TAG:\n\tcompose...")
        var isPaused by rememberSaveable { mutableStateOf(true) }
        var toSettings by rememberSaveable { mutableStateOf(false) }
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
                            isPaused = true
                            toSettings = true
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(end = App.Theme.dimensions.insets.end)
        ) {
            println("$TAG: is paused: $isPaused")
            var index: Int by remember { mutableStateOf(0) }
            println("$TAG: index: $index")
            var value: Int? by rememberSaveable { mutableStateOf(null) }
            println("$TAG: value: $value")
            val startState = rememberSaveable { mutableStateOf<Long?>(null) }
            val max = 6.seconds
            println("$TAG: start: ${startState.value}")
            var progress: Float? by rememberSaveable { mutableStateOf(null) }
            println("$TAG: progress: $progress")
            val delay = 64.milliseconds
            val animationSpec = if (progress == null || progress == 0f) {
                snap<Float>()
            } else {
                tween(
                    durationMillis = delay.inWholeMilliseconds.toInt(),
                    easing = LinearEasing
                )
            }
            val animatable = remember { Animatable(initialValue = progress ?: 0f) }
            LaunchedEffect(progress, isPaused) {
                if (isPaused) {
                    if (animatable.isRunning) animatable.stop()
                } else {
                    animatable.animateTo(
                        targetValue = progress ?: 0f,
                        animationSpec = animationSpec,
                    )
                }
            }
            LaunchedEffect(value, index, isPaused) {
                println("$TAG: launched effect...")
                if (!isPaused) {
                    val next = async(Dispatchers.Default) {
                        nextNumber(random, 100, value)
                    }
                    if (value != null) {
                        val start = startState.getOrSet(System.nanoTime()).nanoseconds
                        if (progress == null) {
                            progress = 0f
                        }
                        withContext(Dispatchers.Default) {
                            while (isActive && !isPaused) {
                                val now = System.nanoTime().nanoseconds
                                val duration = (now - start) / max
                                if (duration < 1.0) {
                                    progress = duration.toFloat()
                                    delay(delay)
                                } else {
                                    progress = null
                                    startState.value = null
                                    break
                                }
                            }
                        }
                    }
                    if (!isPaused) {
                        index++
                        println("$TAG: effect index: $index")
                        value = next.await()
                        println("$TAG: effect value: $value")
                    }
                }
            }
            val number = value
            val text = when (number) {
                null -> "..."
                -100 -> " 00"
                else -> {
                    if (number < 0) {
                        " %02d".format(number.absoluteValue)
                    } else {
                        "%03d".format(number)
                    }
                }
            }
            BasicText(
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 128.sp,
                    fontFamily = FontFamily.Monospace,
                    color = App.Theme.colors.foreground,
                    textAlign = TextAlign.Center
                ),
                text = text
            )
            ProgressBar(value = animatable.value)
            Spacer(
                modifier = Modifier
                    .height(App.Theme.dimensions.button)
            )
            Box(
                modifier = Modifier
                    .size(App.Theme.dimensions.button)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        println("$TAG: on click...")
                        if (isPaused) {
                            startState.value = progress
                                ?.let {
                                    System.nanoTime() - it * max.inWholeNanoseconds
                                }
                                ?.toLong()
                            isPaused = false
                        } else {
                            isPaused = true
                        }
                    },
            ) {
                Image(
                    modifier = Modifier
                        .size(App.Theme.dimensions.icon)
                        .align(Alignment.Center),
                    painter = painterResource(
                        id = if (isPaused) R.drawable.play else R.drawable.pause
                    ),
                    contentDescription = if (isPaused) App.Theme.strings.play else App.Theme.strings.pause,
                    colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
                )
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
