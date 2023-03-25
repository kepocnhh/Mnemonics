package org.kepocnhh.mnemonics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.implementation.module.theme.ThemeViewModel

class MainActivity : AppCompatActivity() {
    @Composable
    private fun Composition(themeState: ThemeState) {
        App.Theme.Composition(
            themeState = themeState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(App.Theme.colors.background)
            ) {
                val TAG = "[MainActivity|${hashCode()}]"
                println("$TAG:\n\tcompose...")
                var main by remember { mutableStateOf(true) }
                if (main) {
                    MainScreen(
                        toSettings = {
                            main = false
                        }
                    )
                } else {
                    SettingsScreen(
                        onBack = {
                            main = true
                        }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel = App.viewModel<ThemeViewModel>()
            when (val themeState = themeViewModel.state.collectAsState().value) {
                null -> themeViewModel.requestThemeState()
                else -> Composition(themeState = themeState)
            }
        }
    }
}
