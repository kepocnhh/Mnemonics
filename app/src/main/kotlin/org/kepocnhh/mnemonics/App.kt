package org.kepocnhh.mnemonics

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Colors
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.foundation.provider.coroutine.Contexts
import org.kepocnhh.mnemonics.implementation.provider.data.local.FinalLocalDataProvider
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Strings
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings.En
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings.Ru

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("no colors") }
        private val LocalStrings = staticCompositionLocalOf<Strings> { error("no strings") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        val strings: Strings
            @Composable
            @ReadOnlyComposable
            get() = LocalStrings.current

        @Composable
        fun Composition(
            themeState: ThemeState,
            content: @Composable () -> Unit,
        ) {
            CompositionLocalProvider(
                LocalColors provides when (themeState.colorsType) {
                    ColorsType.DARK -> Colors.Dark
                    ColorsType.LIGHT -> Colors.Light
                    ColorsType.AUTO -> if (isSystemInDarkTheme()) Colors.Dark else Colors.Light
                },
                LocalStrings provides when (themeState.language) {
                    Language.RU -> Ru
                    Language.EN -> En
                    Language.AUTO -> {
                        val locale = LocalConfiguration.current.locales.get(0)
                        when (locale?.language) {
                            "ru" -> Ru
                            else -> En
                        }
                    }
                },
                content = content,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        val injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                io = Dispatchers.IO,
            ),
            local = FinalLocalDataProvider(
                context = this,
                default = ThemeState(
                    colorsType = ColorsType.AUTO,
                    language = Language.AUTO,
                )
            )
        )
        _viewModelFactory = object : ViewModelProvider.Factory {
            override fun <U : ViewModel> create(modelClass: Class<U>): U {
                return modelClass.getConstructor(Injection::class.java).newInstance(injection)
            }
        }
    }

    companion object {
        private var _viewModelFactory: ViewModelProvider.Factory? = null

        @Composable
        inline fun <reified T : ViewModel> viewModel(): T {
            return viewModel(factory = checkNotNull(_viewModelFactory))
        }
    }
}
