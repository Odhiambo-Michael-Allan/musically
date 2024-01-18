package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.FakePreferences
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.ui.theme.SupportedFonts
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private val preferences: FakePreferences = FakePreferences
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        settingsRepository = SettingsRepositoryImpl( preferences )
    }

    @Test
    fun whenNoLanguageHasBeenSet_englishLanguageIsUsedAsTheDefault() {
        assertEquals( "Settings", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageChangesToBelarusian_belarusianLanguageIsUsed() {
        settingsRepository.setLanguage( BelarusianTranslation.locale )
        assertEquals( "Налады", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageChangesToChinese_chineseLanguageIsUsed() {
        settingsRepository.setLanguage( ChineseTranslation.locale )
        assertEquals( "设置", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageChangesToEnglish_englishLanguageIsUsed() {
        settingsRepository.setLanguage( EnglishTranslation.locale )
        assertEquals( "Settings", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageChangesToFrench_frenchLanguageIsUsed() {
        settingsRepository.setLanguage( FrenchTranslation.locale )
        assertEquals( "Paramètres", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageChangesToGerman_germanLanguageIsUsed() {
        settingsRepository.setLanguage( GermanTranslation.locale )
        assertEquals( "Einstellungen", settingsRepository.language.value.settings )
    }

    @Test
    fun whenLanguageIsSpanish_spanishLanguageIsUsed() {
        settingsRepository.setLanguage( SpanishTranslation.locale )
        assertEquals( "Configuración", settingsRepository.language.value.settings )
    }

    @Test
    fun whenNoFontHasBeenSet_productSansIsUsedAsDefault() {
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }
    @Test
    fun whenFontChangesToInter_interFontIsUsed() {
        settingsRepository.setFont( SupportedFonts.Inter.fontName )
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.Inter.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToPoppins_poppinsFontIsUsed() {
        settingsRepository.setFont( SupportedFonts.Poppins.fontName )
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.Poppins.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToDMSans_DMSansFontIsUsed() {
        settingsRepository.setFont( SupportedFonts.DMSans.fontName )
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.DMSans.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToRoboto_RobotoFontIsUsed() {
        settingsRepository.setFont( SupportedFonts.Roboto.fontName )
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.Roboto.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToProductSans_ProductSansFontIsUsed() {
        settingsRepository.setFont( SupportedFonts.ProductSans.fontName )
        val currentFont = settingsRepository.font.value
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }

}