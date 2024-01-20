package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val _currentLanguage: MutableStateFlow<Language> = MutableStateFlow( English )
    override val language = _currentLanguage.asStateFlow()

    private val _currentFont = MutableStateFlow( SupportedFonts.ProductSans )
    override val font = _currentFont.asStateFlow()

    private val _fontScale = MutableStateFlow( 1.0f )
    override val fontScale = _fontScale.asStateFlow()

    private val _themeMode = MutableStateFlow( ThemeMode.SYSTEM )
    override val themeMode = _themeMode.asStateFlow()

    override fun setLanguage( localeCode: String ) {
        _currentLanguage.value = resolveLanguage( localeCode )
    }



    private fun resolveLanguage( language: String ) = when( language ) {
        Belarusian.locale -> Belarusian
        Chinese.locale -> Chinese
        French.locale -> French
        German.locale -> German
        Spanish.locale -> Spanish
        else -> English
    }

    override fun setFont( fontName: String ) {
        _currentFont.value = resolveFont( fontName )
    }

    override fun setFontScale( fontScale: Float ) {
        _fontScale.value = fontScale
    }

    override fun setThemeMode( themeMode: ThemeMode ) {
        _themeMode.value = themeMode
    }

    private fun resolveFont( fontName: String ) = when( fontName ) {
        SupportedFonts.DMSans.name -> SupportedFonts.DMSans
        SupportedFonts.Inter.name -> SupportedFonts.Inter
        SupportedFonts.Poppins.name -> SupportedFonts.Poppins
        SupportedFonts.Roboto.name -> SupportedFonts.Roboto
        else -> SupportedFonts.ProductSans
    }


}