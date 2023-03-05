package org.kepocnhh.mnemonics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random
import kotlin.time.Duration.Companion.seconds

private fun numbers() = flow {
    println("[Flow|${hashCode()}]: start...")
    val random = Random()
    while (currentCoroutineContext().isActive) {
        delay(1.seconds)
        val value = random.nextInt(1_000)
        println("[Flow|${hashCode()}]: emmit $value")
        emit(value)
    }
    println("[Flow|${hashCode()}]: finish...")
}

private val random = Random()

@Composable
internal fun MainScreen() {
    Box(Modifier.fillMaxSize()) {
        val TAG = "[MainScreen|${hashCode()}]"
        println("$TAG:\n\tcompose...")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            var isPaused by rememberSaveable { mutableStateOf(true) }
            println("$TAG: is paused: $isPaused")
//            var value by rememberSaveable { mutableStateOf(null) }
            val lifecycleOwner = LocalLifecycleOwner.current
//            val flow = numbers()
//            val minActiveState = Lifecycle.State.STARTED
            var index: Int by remember { mutableStateOf(0) }
            println("$TAG: index: $index")
            var value: Int? by remember { mutableStateOf(null) }
            println("$TAG: value: $value")
            LaunchedEffect(value, index, isPaused) {
                println("$TAG: launched effect...")
                if (!isPaused) withContext(Dispatchers.IO) {
                    println("$TAG: delay...")
                    delay(1.seconds)
                    val next = random.nextInt(1_000)
//                    val next = 128
                    index++
                    println("$TAG: effect index: $index")
                    value = next
                    println("$TAG: effect value: $value")
                }
            }
//            val value: Int? by produceState<Int?>(null, lifecycleOwner.lifecycle) {
//                println("$TAG: produce state...")
//                val state: MutableState<Int?> = this
//                if (!isPaused) lifecycleOwner.lifecycleScope.launch {
//                    println("$TAG: launch...")
//                    delay(1.seconds)
//                    state.value = random.nextInt(1_000)
//                }
//            }
            val text = when (value) {
                null -> "..."
                -2 -> "  0"
                -1 -> " 00"
                else -> "%03d".format(value)
            }
            BasicText(
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 48.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                text = text
            )
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        println("$TAG: on click...")
                        isPaused = !isPaused
                    }
                    .wrapContentHeight(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                text = if (isPaused) "play" else "pause"
            )
        }
    }
}
