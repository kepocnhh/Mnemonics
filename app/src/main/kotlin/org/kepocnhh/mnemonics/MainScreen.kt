package org.kepocnhh.mnemonics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
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
    color: Color,
    value: Float
) {
    Box(
        modifier = Modifier
            .background(color)
            .height(8.dp)
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
internal fun MainScreen() {
    val foregroundColor = Color.Black
    val backgroundColor = Color.White
    Box(
        Modifier.fillMaxSize()
            .background(backgroundColor)
    ) {
        val TAG = "[MainScreen|${hashCode()}]"
        println("$TAG:\n\tcompose...")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            var isPaused by rememberSaveable { mutableStateOf(true) }
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
                                    delay(250.milliseconds) // todo animation
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
                    color = foregroundColor,
                    textAlign = TextAlign.Center
                ),
                text = text
            )
            ProgressBar(
                color = foregroundColor,
                value = progress ?: 0f
            )
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        println("$TAG: on click...")
                        if (isPaused) {
                            startState.value = progress?.let {
                                System.nanoTime() - it * max.inWholeNanoseconds
                            }?.toLong()
                            isPaused = false
                        } else {
                            isPaused = true
                        }
                    }
                    .wrapContentHeight(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = foregroundColor,
                    textAlign = TextAlign.Center
                ),
                text = if (isPaused) "play" else "pause"
            )
        }
    }
}
