package org.kepocnhh.mnemonics

import android.widget.NumberPicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.implementation.module.theme.ThemeViewModel
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Insets
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.viewinterop.Spinner

@Composable
private fun DialogColors(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val themeViewModel = App.viewModel<ThemeViewModel>()
        Column(modifier = Modifier.background(App.Theme.colors.background)) {
            Text(
                value = App.Theme.strings.light,
                onClick = {
                    themeViewModel.setColorsType(ColorsType.LIGHT)
                    onDismiss()
                },
            )
            Text(
                value = App.Theme.strings.dark,
                onClick = {
                    themeViewModel.setColorsType(ColorsType.DARK)
                    onDismiss()
                },
            )
            Text(
                value = App.Theme.strings.auto,
                onClick = {
                    themeViewModel.setColorsType(ColorsType.AUTO)
                    onDismiss()
                },
            )
        }
    }
}

@Composable
private fun DialogLanguage(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val themeViewModel = App.viewModel<ThemeViewModel>()
        Column(modifier = Modifier.background(App.Theme.colors.background)) {
            Text(
                value = App.Theme.strings.en,
                onClick = {
                    themeViewModel.setLanguage(Language.EN)
                    onDismiss()
                },
            )
            Text(
                value = App.Theme.strings.ru,
                onClick = {
                    themeViewModel.setLanguage(Language.RU)
                    onDismiss()
                },
            )
            Text(
                value = App.Theme.strings.auto,
                onClick = {
                    themeViewModel.setLanguage(Language.AUTO)
                    onDismiss()
                },
            )
        }
    }
}

@Composable
private fun NumberPicker(
    modifier: Modifier,
    min: Int,
    max: Int,
    value: Int,
    displayedValues: Array<String>? = null,
    wrapSelectorWheel: Boolean = false,
    onChange: (Int) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            NumberPicker(context).also {
                it.minValue = min
                it.maxValue = max
                it.value = value
                it.displayedValues = displayedValues
                it.wrapSelectorWheel = wrapSelectorWheel
                it.setOnValueChangedListener { _, _, value ->
                    onChange(value)
                }
            }
        }
    )
}

@Composable
private fun Picker(
    modifier: Modifier,
    values: List<String>,
    index: Int = 0,
    onChange: (Int) -> Unit
) {
    println("values: $values")
    NumberPicker(
        modifier = modifier,
        min = 0,
        max = values.lastIndex,
        value = index,
        displayedValues = values.toTypedArray(),
        onChange = onChange
    )
}

@Composable
private fun DialogRange(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val values = listOf("a", "b", "c", "d", "e", "f", "g")
        var minIndex by remember { mutableStateOf(0) }
        var maxIndex by remember { mutableStateOf(values.lastIndex) }
        println("min: $minIndex")
        println("max: $maxIndex")
        Column(modifier = Modifier.background(App.Theme.colors.background)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    padding = Insets.empty.copy(bottom = 8.dp),
                    value = App.Theme.strings.rangeFrom,
                    align = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Spinner(
                    modifier = Modifier.weight(1f),
                    values = values.subList(0, maxIndex),
                    index = minIndex,
                    textSize = App.Theme.dimensions.text.value * 2,
                    padding = Insets.empty.copy(top = 8.dp, bottom = 8.dp),
                    onChange = { index ->
                        minIndex = index
                    },
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    padding = Insets.empty.copy(bottom = 8.dp),
                    value = App.Theme.strings.rangeTo,
                    align = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Spinner(
                    modifier = Modifier.weight(1f),
                    values = values.subList(minIndex + 1, values.size),
                    index = maxIndex - minIndex - 1,
                    textSize = App.Theme.dimensions.text.value * 2,
                    padding = Insets.empty.copy(top = 8.dp, bottom = 8.dp),
                    onChange = { index ->
                        maxIndex = index + minIndex + 1
                    },
                )
            }
            Text(
                value = App.Theme.strings.ok,
                onClick = {
                    // todo
                    onDismiss()
                },
            )
        }
    }
}

@Composable
internal fun SettingsScreen(
    onBack: () -> Unit,
) {
    BackHandler {
        onBack()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
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
                    .size(App.Theme.dimensions.toolbar),
            ) {
                Box(
                    modifier = Modifier
                        .size(App.Theme.dimensions.toolbar)
                        .clickable {
                            onBack()
                        },
                ) {
                    Image(
                        modifier = Modifier
                            .size(App.Theme.dimensions.icon)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = App.Theme.strings.back,
                        colorFilter = ColorFilter.tint(App.Theme.colors.foreground)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            var dialogColors by remember { mutableStateOf(false) }
            Text(
                value = App.Theme.strings.colors,
                onClick = {
                    dialogColors = true
                },
            )
            if (dialogColors) {
                DialogColors {
                    dialogColors = false
                }
            }
            var dialogLanguage by remember { mutableStateOf(false) }
            Text(
                value = App.Theme.strings.language,
                onClick = {
                    dialogLanguage = true
                },
            )
            if (dialogLanguage) {
                DialogLanguage {
                    dialogLanguage = false
                }
            }
            var dialogRange by remember { mutableStateOf(false) }
            Text(
                value = App.Theme.strings.range,
                onClick = {
                    dialogRange = true
                },
            )
            if (dialogRange) {
                DialogRange {
                    dialogRange = false
                }
            }
        }
    }
}
