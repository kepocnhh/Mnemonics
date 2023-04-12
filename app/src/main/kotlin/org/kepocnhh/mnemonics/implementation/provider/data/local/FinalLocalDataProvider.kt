package org.kepocnhh.mnemonics.implementation.provider.data.local

import android.content.Context
import org.kepocnhh.mnemonics.BuildConfig
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.Language
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.foundation.provider.data.local.LocalDataProvider
import org.kepocnhh.mnemonics.implementation.entity.Environment
import org.kepocnhh.mnemonics.implementation.entity.Range
import kotlin.time.Duration.Companion.milliseconds

internal data class Defaults(
    val themeState: ThemeState,
    val env: Environment,
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

    override var env: Environment
        get() {
            val length = preferences.getInt("length", defaults.env.length)
            return Environment.new(
                delay = preferences.getLong("delay", defaults.env.delay.inWholeMilliseconds).milliseconds,
                length = length,
                range = Range.new(
                    start = preferences.getInt("start", defaults.env.range.start),
                    endInclusive = preferences.getInt("endInclusive", defaults.env.range.endInclusive),
                    length = length,
                )
            )
        }
        set(value) {
            preferences.edit()
                .putLong("delay", value.delay.inWholeMilliseconds)
                .putInt("length", value.length)
                .putInt("start", value.range.start)
                .putInt("endInclusive", value.range.endInclusive)
                .commit()
        }
}
