package org.kepocnhh.mnemonics.presentation.util.androidx.compose

import androidx.compose.foundation.clickable
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
import org.kepocnhh.mnemonics.App

private val NONE: () -> Unit = {}

@Composable
internal fun Text(
    value: String,
    color: Color = App.Theme.colors.foreground,
    align: TextAlign = TextAlign.Center,
    onClick: () -> Unit = NONE
) {
    BasicText(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // todo
            .let {
                if (onClick !== NONE) {
                    it.clickable {
                        onClick()
                    }
                } else {
                    it
                }
            }
            .wrapContentHeight(Alignment.CenterVertically),
        style = TextStyle(
            fontSize = 14.sp, // todo
            color = color,
            textAlign = align,
        ),
        text = value,
    )
}
