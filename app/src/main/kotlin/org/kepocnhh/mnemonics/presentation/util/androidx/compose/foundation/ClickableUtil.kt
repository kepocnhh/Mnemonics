package org.kepocnhh.mnemonics.presentation.util.androidx.compose.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import org.kepocnhh.mnemonics.App

internal fun Modifier.onClick(
    interactionSource: MutableInteractionSource = App.Theme.NoneMutableInteractionSource,
    onClick: () -> Unit
): Modifier {
    return clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}

internal fun Modifier.catchClicks(): Modifier {
    return onClick(onClick = {})
}
