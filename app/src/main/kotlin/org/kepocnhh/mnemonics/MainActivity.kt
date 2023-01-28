package org.kepocnhh.mnemonics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                BasicText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White
                    ),
                    text = """
                        APPLICATION_ID: ${BuildConfig.APPLICATION_ID}
                        BUILD_TYPE: ${BuildConfig.BUILD_TYPE}
                        VERSION: ${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}
                    """.trimIndent()
                )
            }
        }
    }
}
