package org.kepocnhh.mnemonics.implementation.provider.data.local

import android.content.Context
import org.kepocnhh.mnemonics.BuildConfig
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.foundation.entity.MainEnvironment
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.foundation.provider.data.local.LocalDataProvider
import kotlin.time.Duration.Companion.milliseconds

internal data class Defaults(
    val themeState: ThemeState,
    val env: MainEnvironment,
)

internal class FinalLocalDataProvider(
    context: Context,
    private val defaults: Defaults,
) : LocalDataProvider {
    private val preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override var themeState: ThemeState
        get() {
            return ThemeState(
                colorsType = preferences
                    .getString("colorsType", null)
                    ?.let(ColorsType::valueOf)
                    ?: defaults.themeState.colorsType,
                language = preferences
                    .getString("language", null)
                    ?.let(Language::valueOf)
                    ?: defaults.themeState.language,
            )
        }
        set(value) {
            preferences.edit()
                .putString("colorsType", value.colorsType.name)
                .putString("language", value.language.name)
                .commit()
        }

    override var env: MainEnvironment
        get() {
            return MainEnvironment(
                time = preferences.getLong("time", defaults.env.time.inWholeMilliseconds).milliseconds,
                length = preferences.getInt("length", defaults.env.length),
                range = MainEnvironment.Range(
                    start = preferences.getInt("start", defaults.env.range.start),
                    endInclusive = preferences.getInt("endInclusive", defaults.env.range.endInclusive),
                )
            )
        }
        set(value) {
            preferences.edit()
                .putLong("time", value.time.inWholeMilliseconds)
                .putInt("length", value.length)
                .putInt("start", value.range.start)
                .putInt("endInclusive", value.range.endInclusive)
                .commit()
        }
}
