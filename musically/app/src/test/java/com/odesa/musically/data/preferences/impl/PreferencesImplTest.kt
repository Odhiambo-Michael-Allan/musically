package com.odesa.musically.data.preferences.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.FakePreferencesStoreImpl
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
    fun whenLanguageIsSetToBelarusian_theBelarusianLanguageIsUsed() {
        preferences.setLanguage( BelarusianTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "Налады", currentLanguage.settings )
    }

    @Test
    fun whenLanguageIsSetToChinese_theChineseLanguageIsUsed() {
        preferences.setLanguage( ChineseTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "设置", currentLanguage.settings )
    }

    @Test
    fun whenLanguageIsSetToEnglish_theEnglishLanguageIsUsed() {
        preferences.setLanguage( EnglishTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "Settings", currentLanguage.settings )
    }

    @Test
    fun whenLanguageIsSetToFrench_theFrenchLanguageIsUsed() {
        preferences.setLanguage( FrenchTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "Paramètres", currentLanguage.settings )
    }

    @Test
    fun whenLanguageIsSetToGerman_theGermanLanguageIsUsed() {
        preferences.setLanguage( GermanTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "Einstellungen", currentLanguage.settings )
    }

    @Test
    fun whenLanguageIsSetToSpanish_theSpanishLanguageIsUsed() {
        preferences.setLanguage( SpanishTranslation.locale )
        val currentLanguage = preferences.language.value
        assertEquals( "Configuración", currentLanguage.settings )
    }

    @Test
    fun whenFontHasNotBeenSet_productSansIsUsedAsTheDefault() {
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToInter_interFontIsUsed() {
        preferences.setFont( SupportedFonts.Inter.fontName )
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.Inter.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToPoppins_poppinsFontIsUsed() {
        preferences.setFont( SupportedFonts.Poppins.fontName )
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.Poppins.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToDMSans_DMSansFontIsUsed() {
        preferences.setFont( SupportedFonts.DMSans.fontName )
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.DMSans.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToRoboto_robotoFontIsUsed() {
        preferences.setFont( SupportedFonts.Roboto.fontName )
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.Roboto.fontName, currentFont.fontName )
    }

    @Test
    fun whenFontChangesToProductSans_productSansFontIsUsed() {
        preferences.setFont( SupportedFonts.ProductSans.fontName )
        val currentFont = preferences.font.value
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }

}