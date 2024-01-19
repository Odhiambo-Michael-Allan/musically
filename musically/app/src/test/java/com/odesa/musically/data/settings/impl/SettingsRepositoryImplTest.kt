package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.FakePreferences
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
        changeLanguageTo( BelarusianTranslation, "Налады" )
        changeLanguageTo( ChineseTranslation, "设置" )
        changeLanguageTo( EnglishTranslation, "Settings" )
        changeLanguageTo( FrenchTranslation, "Paramètres" )
        changeLanguageTo( GermanTranslation, "Einstellungen" )
        changeLanguageTo( SpanishTranslation, "Configuración" )
    }

    private fun changeLanguageTo( language: Translation, testString: String ) {
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

}