package org.kepocnhh.mnemonics.presentation.util.androidx.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.mnemonics.App

@Composable
internal fun Text(
    value: String,
    height: Dp = App.Theme.dimensions.button,
    size: TextUnit = App.Theme.dimensions.text,
    family: FontFamily = FontFamily.Default,
    weight: FontWeight = FontWeight.Normal,
    color: Color = App.Theme.colors.foreground,
    align: TextAlign = TextAlign.Center,
    onClick: () -> Unit
) {
    BasicText(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clickable {
                onClick()
            }
            .wrapContentHeight(Alignment.CenterVertically),
        style = TextStyle(
            fontFamily = family,
            fontWeight = weight,
            fontSize = size,
            color = color,
            textAlign = align,
        ),
        text = value,
    )
}

@Composable
internal fun Text(
    value: String,
    modifier: Modifier,
    size: TextUnit = App.Theme.dimensions.text,
    family: FontFamily = FontFamily.Default,
    weight: FontWeight = FontWeight.Normal,
    color: Color = App.Theme.colors.foreground,
    align: TextAlign = TextAlign.Center
) {
    BasicText(
        modifier = modifier,
        style = TextStyle(
            fontFamily = family,
            fontWeight = weight,
            fontSize = size,
            color = color,
            textAlign = align,
        ),
        text = value,
    )
}

@Composable
internal fun Text(
    value: String,
    padding: Insets = Insets.empty,
    size: TextUnit = App.Theme.dimensions.text,
    family: FontFamily = FontFamily.Default,
    weight: FontWeight = FontWeight.Normal,
    color: Color = App.Theme.colors.foreground,
    align: TextAlign = TextAlign.Center
) {
    Text(
        value = value,
        modifier = Modifier.padding(padding),
        size = size,
        family = family,
        weight = weight,
        color = color,
        align = align,
    )
}
