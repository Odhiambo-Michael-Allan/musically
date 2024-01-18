package com.odesa.musically.ui.settings

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.ui.theme.SupportedFonts
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SettingsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val settingsRepository = FakeSettingsRepository()
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsViewModel = SettingsViewModel( settingsRepository )
    }


    @Test
    fun whenNoLanguageHasBeenSet_theDefaultIsEnglish() {
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( "Settings", currentLanguage.settings )
    }

    @Test
    fun whenLanguageChangesToBelarusian_belarusianLanguageIsUsed() {
        testLanguage( BelarusianTranslation.locale, "Налады" )
    }

    @Test
    fun whenLanguageChangesToChinese_theChineseLanguageIsUsed() {
        testLanguage( ChineseTranslation.locale, "设置" )
    }

    @Test
    fun whenLanguageChangesToFrench_theFrenchLanguageIsUsed() {
        testLanguage( FrenchTranslation.locale, "Paramètres" )
    }

    @Test
    fun whenLanguageChangesToGerman_germanLanguageIsUsed() {
        testLanguage( GermanTranslation.locale, "Einstellungen" )
    }

    @Test
    fun whenLanguageChangesToSpanish_spanishLanguageIsUsed() {
        testLanguage( SpanishTranslation.locale, "Configuración" )
    }

    private fun testLanguage( languageToTest: String, expected: String ) {
        settingsRepository.setLanguage( languageToTest )
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( expected, currentLanguage.settings )
    }

    @Test
    fun whenNoFontHasBeenSet_theDefaultIsProductSans() {
        val currentFont = settingsViewModel.uiState.value.font
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontIsChangedToInter_interFontIsUsed() {
        testFont( SupportedFonts.Inter.fontName )
    }

    @Test
    fun whenFontIsChangedToPoppins_poppinsFontIsUsed() {
        testFont( SupportedFonts.Poppins.fontName )
    }

    @Test
    fun whenFontIsChangedToDMSans_dmSansFontIsUsed() {
        testFont( SupportedFonts.DMSans.fontName )
    }

    @Test
    fun whenFontIsChangedToRoboto_robotoFontIsUsed() {
        testFont( SupportedFonts.Roboto.fontName )
    }

    @Test
    fun whenFontIsChangedToProductSans_productSansFontIsUsed() {
        testFont( SupportedFonts.ProductSans.fontName )
    }

    private fun testFont( expected: String ) {
        settingsRepository.setFont( expected )
        val currentFont = settingsViewModel.uiState.value.font
        assertEquals( expected, currentFont.fontName )
    }
}