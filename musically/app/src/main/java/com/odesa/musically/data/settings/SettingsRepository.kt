package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val fontScale: StateFlow<Float>

    fun setLanguage( localeCode: String )
    fun setFont( fontName: String )
    fun setFontScale( fontScale: Float )
}