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
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesImpl( private val preferenceStore: PreferenceStore ) : Preferences {

    private val _language = MutableStateFlow( getLanguage( preferenceStore.getLanguage() ) )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( getFont( preferenceStore.getFontName() ) )
    override val font = _font.asStateFlow()
    override val textDirection: StateFlow<TextDirection>
        get() = TODO("Not yet implemented")

    private val _fontScale = MutableStateFlow( preferenceStore.getFontScale() )
    override val fontScale = _fontScale.asStateFlow()

    private val _themeMode = MutableStateFlow( preferenceStore.getThemeMode() )
    override val themeMode = _themeMode.asStateFlow()

    private val _useMaterialYou = MutableStateFlow( preferenceStore.getUseMaterialYou() )
    override val useMaterialYou = _useMaterialYou.asStateFlow()

    private val _primaryColorName = MutableStateFlow( preferenceStore.getPrimaryColorName() )
    override val primaryColorName = _primaryColorName.asStateFlow()


    private fun getLanguage( localeCode: String ) : Language {
        return when ( localeCode ) {
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
            getLanguage( localeCode )
        }
    }

    override fun setFont( fontName: String ) {
        preferenceStore.setFontName( fontName )
        _font.update {
            getFont( fontName )
        }
    }

    private fun getFont( fontName: String ): MusicallyFont {
        return when( fontName ) {
            SupportedFonts.DMSans.name -> SupportedFonts.DMSans
            SupportedFonts.Inter.name -> SupportedFonts.Inter
            SupportedFonts.Poppins.name -> SupportedFonts.Poppins
            SupportedFonts.Roboto.name -> SupportedFonts.Roboto
            else -> SupportedFonts.ProductSans
        }
    }

    override fun setFontScale( fontScale: Float ) {
        preferenceStore.setFontScale( fontScale )
        _fontScale.update {
            fontScale
        }
    }


    override fun setThemeMode( themeMode: ThemeMode ) {
        preferenceStore.setThemeMode( themeMode )
        _themeMode.update {
            themeMode
        }
    }

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        preferenceStore.setUseMaterialYou( useMaterialYou )
        _useMaterialYou.update {
            useMaterialYou
        }
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        preferenceStore.setPrimaryColorName( primaryColorName )
        _primaryColorName.update {
            primaryColorName
        }
    }

}
