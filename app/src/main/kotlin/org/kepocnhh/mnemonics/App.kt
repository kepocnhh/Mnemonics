package org.kepocnhh.mnemonics

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Colors

internal class App : Application() {
    object States {
        var colors by mutableStateOf<Colors>(Colors.Dark)
    }

    object Theme {
//        val LocalColors = staticCompositionLocalOf<Colors> { error("no colors") }
        private val LocalColors = compositionLocalOf<Colors> { error("no colors") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        @Composable
        fun Composition(content: @Composable () -> Unit) {
            CompositionLocalProvider(
                LocalColors provides States.colors,
                content = content,
            )
        }
    }
}
