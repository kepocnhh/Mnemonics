package org.kepocnhh.mnemonics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier

object Router {
    private enum class State {
        MAIN,
        SETTINGS
    }

    @Composable
    fun Screen() {
        Box(modifier = Modifier.fillMaxSize()) {
            var state by remember { mutableStateOf(State.MAIN) }
            when (state) {
                State.MAIN -> {
                    Main.Screen(
                        toSettings = {
                            state = State.SETTINGS
                        }
                    )
                }
                State.SETTINGS -> {
                    Settings.Screen(
                        onBack = {
                            state = State.MAIN
                        }
                    )
                }
            }
        }
    }
}
