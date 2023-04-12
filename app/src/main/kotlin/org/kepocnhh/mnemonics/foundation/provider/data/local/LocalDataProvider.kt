package org.kepocnhh.mnemonics.foundation.provider.data.local

import org.kepocnhh.mnemonics.foundation.entity.MainEnvironment
import org.kepocnhh.mnemonics.foundation.entity.ThemeState

internal interface LocalDataProvider {
    var themeState: ThemeState
    var env: MainEnvironment
}
