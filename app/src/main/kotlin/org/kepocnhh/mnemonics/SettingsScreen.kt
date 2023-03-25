package org.kepocnhh.mnemonics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.implementation.module.theme.ThemeViewModel
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Colors
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Text

@Composable
internal fun SettingsScreen(
    onBack: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            var dialog by remember { mutableStateOf(false) }
            Text(
                value = "colors",
                onClick = {
                    dialog = true
                },
            )
            if (dialog) {
                Dialog(
                    onDismissRequest = {
                        dialog = false
                    },
                ) {
                    val themeViewModel = App.viewModel<ThemeViewModel>()
                    Column(modifier = Modifier.background(App.Theme.colors.background)) {
                        Text(
                            value = "light",
                            onClick = {
                                themeViewModel.setColorsType(ColorsType.LIGHT)
                                dialog = false
                            },
                        )
                        Text(
                            value = "dark",
                            onClick = {
                                themeViewModel.setColorsType(ColorsType.DARK)
                                dialog = false
                            },
                        )
                        Text(
                            value = "auto",
                            onClick = {
                                themeViewModel.setColorsType(ColorsType.AUTO)
                                dialog = false
                            },
                        )
                    }
                }
            }
            Text(
                value = "back",
                onClick = {
                    onBack()
                },
            )
        }
    }
}
