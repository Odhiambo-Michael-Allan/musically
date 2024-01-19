package com.odesa.musically.data.preferences.storage

interface PreferenceStore {
    fun setLanguage( localeCode: String )
    fun getLanguage(): String

    fun setFontName( fontName: String )
    fun getFontName(): String
    fun setFontScale( fontScale: Float )
    fun getFontScale(): Float
}