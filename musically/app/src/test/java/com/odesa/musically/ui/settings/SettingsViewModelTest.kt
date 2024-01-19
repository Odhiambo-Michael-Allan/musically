package com.odesa.musically.ui.settings

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.SupportedFonts
import junit.framework.TestCase.assertEquals
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
        changeLanguageTo( BelarusianTranslation, "Налады" )
        changeLanguageTo( ChineseTranslation, "设置" )
        changeLanguageTo( EnglishTranslation, "Settings" )
        changeLanguageTo( FrenchTranslation, "Paramètres" )
        changeLanguageTo( GermanTranslation, "Einstellungen" )
        changeLanguageTo( SpanishTranslation, "Configuración" )
    }

    private fun changeLanguageTo( language: Translation, testString: String ) {
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
}