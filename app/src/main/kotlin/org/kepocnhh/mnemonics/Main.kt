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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Main {
    @Composable
    fun Screen(toSettings: () -> Unit) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            toSettings()
                        }
                        .wrapContentHeight(Alignment.CenterVertically),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    text = "settings"
                )
                var isPaused by remember { mutableStateOf(true) }
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    text = "..."
                )
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            isPaused = !isPaused
                        }
                        .wrapContentHeight(Alignment.CenterVertically),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    text = when (isPaused) {
                        true -> "play"
                        false -> "pause"
                    }
                )
            }
        }
    }
}
