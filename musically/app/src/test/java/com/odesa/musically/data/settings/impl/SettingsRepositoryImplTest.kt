package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.FakePreferences
import com.odesa.musically.data.settings.SettingsRepository
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
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private lateinit var preferences: FakePreferences
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        preferences = FakePreferences()
        settingsRepository = SettingsRepositoryImpl( preferences )
    }

    @Test
    fun whenNoLanguageHasBeenSet_englishLanguageIsUsedAsTheDefault() {
        assertEquals( "Settings", settingsRepository.language.value.settings )
    }

    @Test
    fun testLanguageChange() {
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) {
        preferences.setLanguage( language.locale )
        val currentLanguage = settingsRepository.language.value
        assertEquals( testString, currentLanguage.settings )
    }


    @Test
    fun whenNoFontHasBeenSet_productSansIsUsedAsDefault() {
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.ProductSans.name, currentFont.name )
    }

    @Test
    fun testFontChange() {
        changeFontTo( SupportedFonts.ProductSans )
        changeFontTo( SupportedFonts.Roboto )
        changeFontTo( SupportedFonts.DMSans )
        changeFontTo( SupportedFonts.Poppins )
        changeFontTo( SupportedFonts.Inter )
    }

    private fun changeFontTo( font: MusicallyFont ) {
        preferences.setFont( font.name )
        val currentFont = settingsRepository.font.value
        assertEquals( font.name, currentFont.name )
    }

    @Test
    fun whenNoFontScaleHasBeenSet_oneIsUsedAsTheDefault() {
        assertEquals( 1.0f, settingsRepository.fontScale.value )
    }

    @Test
    fun testFontScaleChange() {
        changeFontScaleTo( 0.25f )
        changeFontScaleTo( 0.5f )
        changeFontScaleTo( 0.75f )
        changeFontScaleTo( 1.0f )
        changeFontScaleTo( 1.25f )
        changeFontScaleTo( 1.5f )
        changeFontScaleTo( 1.75f )
        changeFontScaleTo( 2.0f )
        changeFontScaleTo( 2.25f )
        changeFontScaleTo( 2.5f )
        changeFontScaleTo( 2.75f )
        changeFontScaleTo( 2.0f )
    }

    private fun changeFontScaleTo( fontScale: Float ) {
        preferences.setFontScale( fontScale )
        assertEquals( fontScale, settingsRepository.fontScale.value )
    }

    @Test
    fun whenNoThemeModeHasBeenSet_systemIsUsedAsTheDefault() {
        assertEquals( ThemeMode.SYSTEM.name, settingsRepository.themeMode.value.name )
    }

    @Test
    fun testThemeModeChange() {
        changeThemeModeTo( ThemeMode.SYSTEM )
        changeThemeModeTo( ThemeMode.SYSTEM_BLACK )
        changeThemeModeTo( ThemeMode.LIGHT )
        changeThemeModeTo( ThemeMode.DARK )
        changeThemeModeTo( ThemeMode.BLACK )
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) {
        preferences.setThemeMode( themeMode )
        assertEquals( themeMode.name, settingsRepository.themeMode.value.name )
    }

}