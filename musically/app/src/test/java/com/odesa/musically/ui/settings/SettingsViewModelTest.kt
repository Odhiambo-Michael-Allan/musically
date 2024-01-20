package com.odesa.musically.ui.settings

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
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
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SettingsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        settingsViewModel = SettingsViewModel( settingsRepository )
    }


    @Test
    fun whenNoLanguageHasBeenSet_theDefaultIsEnglish() {
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( "Settings", currentLanguage.settings )
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
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun whenNoFontHasBeenSet_theDefaultIsProductSans() {
        val currentFont = settingsViewModel.uiState.value.font
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
        settingsRepository.setFont( font.name )
        val currentFont = settingsViewModel.uiState.value.font
        assertEquals( font.name, currentFont.name )
    }

    @Test
    fun whenNoFontScaleHasBeenSet_theDefaultIsOne() {
        val currentFontScale = settingsViewModel.uiState.value.fontScale
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
        settingsRepository.setFontScale( fontScale )
        val currentFontScale = settingsViewModel.uiState.value.fontScale
        assertEquals( fontScale, currentFontScale )
    }

    @Test
    fun whenValidFontScaleStringIsProvided_settingsViewModelCorrectlyParsesIt() {
        settingsViewModel.setFontScale( "1.25" )
        assertEquals( "1.25", settingsViewModel.uiState.value.fontScale.toString() )
    }

    @Test
    fun whenInvalidFontScaleIsProvided_settingsViewModelCorrectlyIdentifiesIt() {
        settingsViewModel.setFontScale( "1245323" )
        assertEquals( "1.0", settingsViewModel.uiState.value.fontScale.toString() )
    }

    @Test
    fun whenNoThemeModeHasBeenSet_systemIsUsedAsTheDefault() {
        val currentThemeMode = settingsViewModel.uiState.value.themeMode
        assertEquals( ThemeMode.SYSTEM.name, currentThemeMode.name )
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
        settingsRepository.setThemeMode( themeMode )
        assertEquals( themeMode.name, settingsViewModel.uiState.value.themeMode.name )
    }

    @Test
    fun testMaterialYouIsUsedByDefault() {
        assertTrue( settingsViewModel.uiState.value.useMaterialYou )
    }

    @Test
    fun testUseMaterialYouChange() {
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }

    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) {
        settingsRepository.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, settingsViewModel.uiState.value.useMaterialYou )
    }

}