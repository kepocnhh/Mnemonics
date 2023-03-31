package org.kepocnhh.mnemonics.presentation.util.androidx.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
internal data class Dimensions(
    val insets: Insets,
    val toolbar: Dp,
    val button: Dp,
    val icon: Dp,
)
