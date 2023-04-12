package org.kepocnhh.mnemonics.foundation.provider.data.local

import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.implementation.entity.Environment

internal interface LocalDataProvider {
    var themeState: ThemeState
    var env: Environment
}
