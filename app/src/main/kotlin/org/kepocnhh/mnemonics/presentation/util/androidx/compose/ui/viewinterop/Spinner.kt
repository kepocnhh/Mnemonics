package org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.viewinterop

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import org.kepocnhh.mnemonics.App
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Insets
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.unit.toPx

@Composable
internal fun Spinner(
    modifier: Modifier,
    values: List<String>,
    index: Int,
    textSize: Float,
    padding: Insets,
    onChange: (Int) -> Unit
) {
    val backgroundColor = App.Theme.colors.background.toArgb()
    val foregroundColor = App.Theme.colors.foreground.toArgb()
    val paddingTop = padding.top.toPx().toInt()
    val paddingBottom = padding.bottom.toPx().toInt()
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
                        )
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
