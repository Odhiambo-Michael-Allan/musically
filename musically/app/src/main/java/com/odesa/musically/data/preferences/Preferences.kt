package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.StateFlow

interface Preferences {
    val language: StateFlow<Translation>
    val font: StateFlow<MusicallyFont>
    val textDirection: StateFlow<TextDirection>
    fun setLanguage( localeCode: String )
    fun setFont( fontName: String )

}