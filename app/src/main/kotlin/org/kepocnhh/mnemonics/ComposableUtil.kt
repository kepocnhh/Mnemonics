package org.kepocnhh.mnemonics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
internal fun Text(
    height: Dp = 48.dp,
    fontSize: TextUnit = 14.sp,
    textColor: Color = Color.White,
    text: String
) {
    BasicText(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .wrapContentHeight(Alignment.CenterVertically),
        style = TextStyle(
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center
        ),
        text = text
    )
}

@Composable
internal fun Button(
    height: Dp = 48.dp,
    fontSize: TextUnit = 14.sp,
    textColor: Color = Color.White,
    text: String,
    onClick: () -> Unit
) {
    BasicText(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clickable(onClick = onClick)
            .wrapContentHeight(Alignment.CenterVertically),
        style = TextStyle(
            fontSize = fontSize,
            color = textColor,
            textAlign = TextAlign.Center
        ),
        text = text
    )
}

@Composable
fun OnLifecycleEvent(
    block: (Lifecycle.Event) -> Unit
) {
    val owner = LocalLifecycleOwner.current
    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            block(event)
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OneShotEffect(block: () -> Unit) {
    var isLaunched by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!isLaunched) {
            isLaunched = true
            block()
        }
    }
}
