package org.kepocnhh.mnemonics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Settings {
    @Composable
    fun Screen(onBack: () -> Unit) {
        Box(Modifier.fillMaxSize()) {
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {
                        onBack()
                    }
                    .wrapContentHeight(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                text = "back"
            )
        }
    }
}
