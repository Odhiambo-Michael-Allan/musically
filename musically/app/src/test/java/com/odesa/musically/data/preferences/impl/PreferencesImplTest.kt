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
import com.odesa.musically.ui.settings.fontScale.scalingPresets
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.MusicallyTypography
import com.odesa.musically.ui.theme.PrimaryThemeColors
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
        MusicallyTypography.all.values.forEach { changeFontTo( it ) }
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
        scalingPresets.forEach { changeFontScaleTo( it ) }
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
        ThemeMode.entries.forEach {
            changeThemeModeTo( it )
        }
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) {
        preferences.setThemeMode( themeMode )
        assertEquals( themeMode.name, preferences.themeMode.value.name )
    }

    @Test
    fun testMaterialYouIsUsedByDefault() {
        assertTrue( preferences.useMaterialYou.value )
    }

    @Test
    fun testUseMaterialYouChange() {
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }

    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) {
        preferences.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, preferences.useMaterialYou.value )
    }

    @Test
    fun testDefaultPrimaryColorNameIsBlue() {
        assertEquals( PrimaryThemeColors.Blue.name, preferences.primaryColorName.value )
    }

    @Test
    fun testPrimaryColorChange() {
        PrimaryThemeColors.entries.forEach {
            changePrimaryColorTo( it.name )
        }
    }

    private fun changePrimaryColorTo( primaryColorName: String ) {
        preferences.setPrimaryColorName( primaryColorName )
        assertEquals( primaryColorName, preferences.primaryColorName.value )
    }
}