package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
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
            BelarusianTranslation.locale -> _language.value = BelarusianTranslation
            ChineseTranslation.locale -> _language.value = ChineseTranslation
            FrenchTranslation.locale -> _language.value = FrenchTranslation
            GermanTranslation.locale -> _language.value = GermanTranslation
            SpanishTranslation.locale -> _language.value = SpanishTranslation
            else -> _language.value = EnglishTranslation
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

    private fun getLanguage() : Translation {
        return EnglishTranslation
    }

    private fun getFont(): MusicallyFont {
        return SupportedFonts.ProductSans
    }

    private fun getFontScale(): Float {
        return 1.0f
    }

}