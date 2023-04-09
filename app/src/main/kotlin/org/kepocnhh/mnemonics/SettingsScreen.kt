package org.kepocnhh.mnemonics

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.NumberPicker
import android.widget.SimpleAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.activity.compose.BackHandler
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.MenuPopupWindow.MenuDropDownListView
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.implementation.module.theme.ThemeViewModel
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Insets
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.padding
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.unit.toPx

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
private fun Spinner(
    modifier: Modifier,
    values: List<String>,
    index: Int,
    backgroundColor: Int,
    foregroundColor: Int,
    textSize: Float,
    paddingTop: Int,
    paddingBottom: Int,
    onChange: (Int) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            Spinner(context).also {
                it.background = null
                it.setPadding(0, 0, 0, 0)
                it.setPopupBackgroundDrawable(ColorDrawable(backgroundColor))
            }
        },
        update = { view ->
            view.onItemSelectedListener = null
            view.adapter = object: BaseAdapter() {
                override fun getCount(): Int {
                    return values.size
                }

                override fun getItem(position: Int): Any {
                    return values[position]
                }

                override fun getItemId(position: Int): Long {
                    return position.toLong()
                }

                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup?
                ): View {
                    val result: TextView = convertView as? TextView ?: TextView(view.context).also {
                        it.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                            itemHeight,
                        )
//                        it.background = ColorDrawable(backgroundColor)
//                        it.background = RippleDrawable(
//                            ColorStateList.valueOf(backgroundColor),
//                            ColorDrawable(backgroundColor),
//                            null,
//                        )
//                        it.foreground = TypedValue().let { value ->
//                            view.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
//                            ContextCompat.getDrawable(view.context, value.resourceId)
//                        }
                        it.background = StateListDrawable().also { states ->
                            states.addState(
                                intArrayOf(android.R.attr.state_pressed),
                                ColorDrawable(foregroundColor).also { drawable ->
                                    drawable.alpha = (256 * 0.25f).toInt()
                                },
                            )
                            states.addState(
                                intArrayOf(),
                                ColorDrawable(backgroundColor),
                            )
                        }
                        it.foreground = null
                        it.setPadding(0, paddingTop, 0, paddingBottom)
                        it.gravity = Gravity.CENTER
                        it.typeface = Typeface.MONOSPACE
                        it.setTextColor(foregroundColor)
                        it.textSize = textSize
                    }
                    result.text = values[position]
                    return result
                }
            }
            view.setSelection(index)
            view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onChange(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented: onNothingSelected")
                }
            }
        },
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
                    backgroundColor = App.Theme.colors.background.toArgb(),
                    foregroundColor = App.Theme.colors.foreground.toArgb(),
                    textSize = App.Theme.dimensions.text.value * 2,
                    paddingTop = 8.dp.toPx().toInt(),
                    paddingBottom = 8.dp.toPx().toInt(),
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
                    backgroundColor = App.Theme.colors.background.toArgb(),
                    foregroundColor = App.Theme.colors.foreground.toArgb(),
                    textSize = App.Theme.dimensions.text.value * 2,
                    paddingTop = 8.dp.toPx().toInt(),
                    paddingBottom = 8.dp.toPx().toInt(),
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
