package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.StateFlow

interface Preferences {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val textDirection: StateFlow<TextDirection>
    val fontScale: StateFlow<Float>

    fun setLanguage( localeCode: String )
    fun setFont( fontName: String )
    fun setFontScale( fontScale: Float )

}