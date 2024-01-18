package com.odesa.musically.data.preferences.impl

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.PreferenceStore
import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.SupportedFonts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesImpl( private val preferenceStore: PreferenceStore ) : Preferences {

    private val _language = MutableStateFlow( getLanguage() )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( getFont() )
    override val font = _font.asStateFlow()
    override val textDirection: StateFlow<TextDirection>
        get() = TODO("Not yet implemented")

    private fun getFont(): MusicallyFont {
        return when( preferenceStore.getFontName() ) {
            SupportedFonts.DMSans.fontName -> SupportedFonts.DMSans
            SupportedFonts.Inter.fontName -> SupportedFonts.Inter
            SupportedFonts.Poppins.fontName -> SupportedFonts.Poppins
            SupportedFonts.Roboto.fontName -> SupportedFonts.Roboto
            else -> SupportedFonts.ProductSans
        }
    }

    private fun getLanguage() : Translation {
        return when ( preferenceStore.getLanguage() ) {
            BelarusianTranslation.locale -> BelarusianTranslation
            ChineseTranslation.locale -> ChineseTranslation
            FrenchTranslation.locale -> FrenchTranslation
            GermanTranslation.locale -> GermanTranslation
            SpanishTranslation.locale -> SpanishTranslation
            else -> EnglishTranslation
        }
    }

    override fun setLanguage( localeCode: String ) {
        preferenceStore.setLanguage( localeCode )
        _language.update {
            getLanguage()
        }
    }

    override fun setFont( fontName: String ) {
        preferenceStore.setFontName( fontName )
        _font.update {
            getFont()
        }
    }

}

object SettingsKeys {
    const val identifier = "musically_settings"
    const val language = "language"
    const val fontName = "font_name"
}