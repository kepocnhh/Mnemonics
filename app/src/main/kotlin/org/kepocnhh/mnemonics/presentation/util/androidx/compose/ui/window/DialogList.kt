package org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import org.kepocnhh.mnemonics.App
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text

@Composable
internal fun <T : Comparable<T>> DialogList(
    actual: T,
    values: Iterable<T>,
    transform: @Composable (T) -> String,
    onDismiss: () -> Unit,
    onSelect: (T) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(modifier = Modifier.background(App.Theme.colors.background)) {
            values.forEach {
                Text(
                    weight = if (actual == it) FontWeight.Bold else FontWeight.Normal,
                    value = transform(it),
                    onClick = {
                        if (actual != it) onSelect(it)
                        onDismiss()
                    },
                )
            }
        }
    }
}
