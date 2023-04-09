package org.kepocnhh.mnemonics.presentation.util.androidx.compose.ui.unit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
internal fun Int.px(density: Float): Dp {
    return (this / density).dp
}

@Stable
internal fun Dp.toPx(density: Float): Float {
    return value * density
}

@Composable
@Stable
internal fun Dp.toPx(density: Density = LocalDensity.current): Float {
    return toPx(density = density.density)
}
