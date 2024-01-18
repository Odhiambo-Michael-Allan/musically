package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.SupportedFonts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val _currentLanguage: MutableStateFlow<Translation> = MutableStateFlow( EnglishTranslation )
    override val language = _currentLanguage.asStateFlow()

    private val _currentFont = MutableStateFlow( SupportedFonts.ProductSans )
    override val font = _currentFont.asStateFlow()

    override fun setLanguage( localeCode: String ) {
        _currentLanguage.value = resolveLanguage( localeCode )
    }



    private fun resolveLanguage( language: String ) = when( language ) {
        BelarusianTranslation.locale -> BelarusianTranslation
        ChineseTranslation.locale -> ChineseTranslation
        FrenchTranslation.locale -> FrenchTranslation
        GermanTranslation.locale -> GermanTranslation
        SpanishTranslation.locale -> SpanishTranslation
        else -> EnglishTranslation
    }

    override fun setFont( fontName: String ) {
        println( "Setting Font to $fontName.." )
        _currentFont.value = resolveFont( fontName )
        println( "New font name: ${font.value.fontName}" )
    }

    private fun resolveFont( fontName: String ) = when( fontName ) {
        SupportedFonts.DMSans.fontName -> SupportedFonts.DMSans
        SupportedFonts.Inter.fontName -> SupportedFonts.Inter
        SupportedFonts.Poppins.fontName -> SupportedFonts.Poppins
        SupportedFonts.Roboto.fontName -> SupportedFonts.Roboto
        else -> SupportedFonts.ProductSans
    }


}