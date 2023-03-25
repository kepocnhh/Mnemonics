package org.kepocnhh.mnemonics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App.Theme.Composition {
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
    }
}
