package org.kepocnhh.mnemonics.implementation.provider.data.local

import android.content.Context
import org.kepocnhh.mnemonics.BuildConfig
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.foundation.provider.data.local.LocalDataProvider

internal class FinalLocalDataProvider(
    context: Context,
    private val default: ThemeState
) : LocalDataProvider {
    private val preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override var themeState: ThemeState
        get() {
            return ThemeState(
                colorsType = preferences
                    .getString("colorsType", null)
                    ?.let(ColorsType::valueOf)
                    ?: default.colorsType
            )
        }
        set(value) {
            preferences.edit()
                .putString("colorsType", value.colorsType.name)
                .commit()
        }
}
