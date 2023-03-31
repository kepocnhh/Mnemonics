package org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.unit

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
internal fun Int.px(density: Float): Dp {
    return (this / density).dp
}
