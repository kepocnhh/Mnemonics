package org.kepocnhh.mnemonics

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.foundation.provider.coroutine.Contexts
import org.kepocnhh.mnemonics.implementation.entity.Environment
import org.kepocnhh.mnemonics.implementation.entity.Range
import org.kepocnhh.mnemonics.implementation.provider.data.local.Defaults
import org.kepocnhh.mnemonics.implementation.provider.data.local.FinalLocalDataProvider
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Colors
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Dimensions
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Strings
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings.En
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings.Ru
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.toInsets
import kotlin.time.Duration.Companion.seconds

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("no colors") }
        private val LocalDimensions = staticCompositionLocalOf<Dimensions> { error("no dimensions") }
        private val LocalStrings = staticCompositionLocalOf<Strings> { error("no strings") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        val dimensions: Dimensions
            @Composable
            @ReadOnlyComposable
            get() = LocalDimensions.current

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
                LocalDimensions provides Dimensions(
                    insets = LocalView.current.rootWindowInsets.toInsets(),
                    toolbar = 56.dp,
                    button = 56.dp,
                    icon = 24.dp,
                    progress = 8.dp,
                    text = 14.sp,
                ),
                content = content,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        val defaultLength = 3
        val injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                io = Dispatchers.IO,
            ),
            local = FinalLocalDataProvider(
                context = this,
                defaults = Defaults(
                    themeState = ThemeState(
                        colorsType = ColorsType.AUTO,
                        language = Language.AUTO,
                    ),
                    env = Environment.new(
                        delay = 6.seconds,
                        length = defaultLength,
                        range = Range.new(
                            start = 0,
                            endInclusive = 1109,
                            length = defaultLength
                        )
                    )
                )
            )
        )
        _injecion = injection
        _viewModelFactory = object : ViewModelProvider.Factory {
            override fun <U : ViewModel> create(modelClass: Class<U>): U {
                return modelClass.getConstructor(Injection::class.java).newInstance(injection)
            }
        }
    }

    companion object {
        private var _viewModelFactory: ViewModelProvider.Factory? = null
        private var _injecion: Injection? = null

        @Composable
        inline fun <reified T : ViewModel> viewModel(): T {
            return viewModel(factory = checkNotNull(_viewModelFactory))
        }

        @Composable
        inline fun <reified T : ViewModel> viewModel(builder: (Injection) -> T): T {
            return builder(checkNotNull(_injecion))
        }
    }
}
