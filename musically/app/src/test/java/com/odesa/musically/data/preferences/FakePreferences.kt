package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.SupportedFonts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePreferences : Preferences {

    private val _language = MutableStateFlow( getLanguage() )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( getFont() )
    override val font = _font.asStateFlow()
    override val textDirection: StateFlow<TextDirection>
        get() = TODO("Not yet implemented")

    private val _fontScale = MutableStateFlow( getFontScale() )
    override val fontScale = _fontScale.asStateFlow()

    override fun setLanguage( localeCode: String ) {
        when ( localeCode ) {
            Belarusian.locale -> _language.value = Belarusian
            Chinese.locale -> _language.value = Chinese
            French.locale -> _language.value = French
            German.locale -> _language.value = German
            Spanish.locale -> _language.value = Spanish
            else -> _language.value = English
        }
    }

    override fun setFont( fontName: String ) {
        when ( fontName ) {
            SupportedFonts.Inter.name -> _font.value = SupportedFonts.Inter
            SupportedFonts.ProductSans.name -> _font.value = SupportedFonts.ProductSans
            SupportedFonts.DMSans.name -> _font.value = SupportedFonts.DMSans
            SupportedFonts.Roboto.name -> _font.value = SupportedFonts.Roboto
            else -> _font.value = SupportedFonts.Poppins
        }
    }

    override fun setFontScale( fontScale: Float ) {
        _fontScale.value = fontScale
    }

    private fun getLanguage() : Language {
        return English
    }

    private fun getFont(): MusicallyFont {
        return SupportedFonts.ProductSans
    }

    private fun getFontScale(): Float {
        return 1.0f
    }

}