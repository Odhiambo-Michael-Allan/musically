package com.odesa.musically.data.preferences.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.FakePreferencesStoreImpl
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

class PreferencesImplTest {

    private val preferenceStore = FakePreferencesStoreImpl()
    private lateinit var preferences : Preferences

    @Before
    fun setup() {
        preferences = PreferencesImpl( preferenceStore )
    }

    @Test
    fun whenLanguageHasNotBeenSet_EnglishIsUsedAsTheDefault() {
        val currentLanguage = preferences.language.value
        assertEquals( "Settings", currentLanguage.settings )
    }

    @Test
    fun testLanguageChange() {
        testLanguage( Belarusian, "Налады" )
        testLanguage( Chinese, "设置" )
        testLanguage( English, "Settings" )
        testLanguage( French, "Paramètres" )
        testLanguage( German, "Einstellungen" )
        testLanguage( Spanish, "Configuración" )
    }

    private fun testLanguage(languageToTest: Language, testString: String ) {
        preferences.setLanguage( languageToTest.locale )
        val currentLanguage = preferences.language.value
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun whenFontHasNotBeenSet_productSansIsUsedAsTheDefault() {
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.ProductSans.name, currentFont.name )
    }

    @Test
    fun testFontChange() {
        changeFontTo( SupportedFonts.Inter )
        changeFontTo( SupportedFonts.Poppins )
        changeFontTo( SupportedFonts.DMSans )
        changeFontTo( SupportedFonts.Roboto )
        changeFontTo( SupportedFonts.ProductSans )
    }

    private fun changeFontTo( font: MusicallyFont ) {
        preferences.setFont( font.name )
        val currentFont = preferences.font.value
        assertEquals( font.name, currentFont.name )
    }

    @Test
    fun whenNoFontScaleHasBeenSet_oneIsUsedAsTheDefault() {
        val currentFontScale = preferences.fontScale.value
        assertEquals( 1.0f, currentFontScale )
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
        changeFontScaleTo( 3.0f )
    }

    private fun changeFontScaleTo( fontScale: Float ) {
        preferences.setFontScale( fontScale )
        assertEquals( fontScale, preferences.fontScale.value )
    }

    @Test
    fun whenNoThemeModeHasBeenSet_systemIsUsedAsTheDefault() {
        assertEquals( ThemeMode.SYSTEM.name, preferences.themeMode.value.name )
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
        assertEquals( themeMode.name, preferences.themeMode.value.name )
    }

}