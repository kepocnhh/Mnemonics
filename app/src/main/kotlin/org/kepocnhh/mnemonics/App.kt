package org.kepocnhh.mnemonics

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App : Application() {
    companion object {
        val scope = CoroutineScope(Dispatchers.Main)
    }
}
