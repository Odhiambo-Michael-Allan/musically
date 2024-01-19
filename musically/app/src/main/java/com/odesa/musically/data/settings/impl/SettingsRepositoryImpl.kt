package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val preferences: Preferences
) : SettingsRepository {

    override val language = preferences.language
    override val font = preferences.font
    override val fontScale = preferences.fontScale

    override fun setLanguage( localeCode: String ) {
        preferences.setLanguage( localeCode )
    }

    override fun setFont( fontName: String ) {
        preferences.setFont( fontName )
    }

    override fun setFontScale( fontScale: Float ) {
        preferences.setFontScale( fontScale )
    }
}