package com.odesa.musically.data.preferences.storage

import com.odesa.musically.ui.theme.ThemeMode

interface PreferenceStore {
    fun setLanguage( localeCode: String )
    fun getLanguage(): String
    fun setFontName( fontName: String )
    fun getFontName(): String
    fun setFontScale( fontScale: Float )
    fun getFontScale(): Float
    fun setThemeMode( themeMode: ThemeMode )
    fun getThemeMode(): ThemeMode
    fun getUseMaterialYou(): Boolean
    fun setUseMaterialYou( useMaterialYou: Boolean )
    fun setPrimaryColorName( primaryColorName: String )
    fun getPrimaryColorName(): String

}