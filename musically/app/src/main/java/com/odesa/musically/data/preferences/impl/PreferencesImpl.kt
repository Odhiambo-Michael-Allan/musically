package com.odesa.musically.data.preferences.impl

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.PreferenceStore
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
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

    private val _fontScale = MutableStateFlow( getFontScale() )
    override val fontScale = _fontScale.asStateFlow()

    private fun getFont(): MusicallyFont {
        return when( preferenceStore.getFontName() ) {
            SupportedFonts.DMSans.name -> SupportedFonts.DMSans
            SupportedFonts.Inter.name -> SupportedFonts.Inter
            SupportedFonts.Poppins.name -> SupportedFonts.Poppins
            SupportedFonts.Roboto.name -> SupportedFonts.Roboto
            else -> SupportedFonts.ProductSans
        }
    }

    private fun getLanguage() : Language {
        return when ( preferenceStore.getLanguage() ) {
            Belarusian.locale -> Belarusian
            Chinese.locale -> Chinese
            French.locale -> French
            German.locale -> German
            Spanish.locale -> Spanish
            else -> English
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

    override fun setFontScale( fontScale: Float ) {
        preferenceStore.setFontScale( fontScale )
        _fontScale.update {
            getFontScale()
        }
    }

    private fun getFontScale() = preferenceStore.getFontScale()

}
