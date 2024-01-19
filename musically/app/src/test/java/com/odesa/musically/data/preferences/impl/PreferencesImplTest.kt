package com.odesa.musically.data.preferences.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.FakePreferencesStoreImpl
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
        testLanguage( BelarusianTranslation, "Налады" )
    }

    @Test
    fun whenLanguageIsSetToChinese_theChineseLanguageIsUsed() {
        testLanguage( ChineseTranslation, "设置" )
    }

    @Test
    fun whenLanguageIsSetToEnglish_theEnglishLanguageIsUsed() {
        testLanguage( EnglishTranslation, "Settings" )
    }

    @Test
    fun whenLanguageIsSetToFrench_theFrenchLanguageIsUsed() {
        testLanguage( FrenchTranslation, "Paramètres" )
    }

    @Test
    fun whenLanguageIsSetToGerman_theGermanLanguageIsUsed() {
        testLanguage( GermanTranslation, "Einstellungen" )
    }

    @Test
    fun whenLanguageIsSetToSpanish_theSpanishLanguageIsUsed() {
        testLanguage( SpanishTranslation, "Configuración" )
    }

    private fun testLanguage( languageToTest: Translation, testString: String ) {
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
    fun whenFontChangesToInter_interFontIsUsed() {
        testFont( SupportedFonts.Inter )
    }

    @Test
    fun whenFontChangesToPoppins_poppinsFontIsUsed() {
        testFont( SupportedFonts.Poppins )
    }

    @Test
    fun whenFontChangesToDMSans_DMSansFontIsUsed() {
        testFont( SupportedFonts.DMSans )
    }

    @Test
    fun whenFontChangesToRoboto_robotoFontIsUsed() {
        testFont( SupportedFonts.Roboto )
    }

    @Test
    fun whenFontChangesToProductSans_productSansFontIsUsed() {
        testFont( SupportedFonts.ProductSans )
    }

    private fun testFont( expectedFont: MusicallyFont ) {
        preferences.setFont( expectedFont.name )
        val currentFont = preferences.font.value
        assertEquals( expectedFont.name, currentFont.name )
    }

    @Test
    fun whenNoFontScaleHasBeenSet_oneIsUsedAsTheDefault() {
        val currentFontScale = preferences.fontScale.value
        assertEquals( 1.0f, currentFontScale )
    }

    @Test
    fun whenFontScaleIsSetToQuarter_quarterFontScaleIsUsed() {
        testFontScale( 0.25f )
    }

    @Test
    fun whenFontScaleIsSetToHalf_halfFontScaleIsUsed() {
        testFontScale( 0.5f )
    }

    @Test
    fun whenFontScaleIsSetToThreeQuarter_threeQuarterFontScaleIsUsed() {
        testFontScale( 0.75f )
    }

    @Test
    fun whenFontScaleIsSetToOne_oneFontScaleIsUsed() {
        testFontScale( 1.0f )
    }

    @Test
    fun whenFontScaleIsSetToOneAndQuarter_oneAndQuarterFontScaleIsUsed() {
        testFontScale( 1.25f )
    }

    @Test
    fun whenFontScaleIsSetToOneAndHalf_oneAndHalfFontScaleIsUsed() {
        testFontScale( 1.5f )
    }

    @Test
    fun whenFontScaleIsSetToOneAndThreeQuarter_oneAndThreeQuarterFontScaleIsUsed() {
        testFontScale( 1.75f )
    }

    @Test
    fun whenFontScaleIsSetToTwo_twoFontScaleIsUsed() {
        testFontScale( 2.0f )
    }

    @Test
    fun whenFontScaleIsSetToTwoAndQuarter_twoAndQuarterFontScaleIsUsed() {
        testFontScale( 2.25f )
    }

    @Test
    fun whenFontScaleIsSetToTwoAndHalf_twoAndHalfFontScaleIsUsed() {
        testFontScale( 2.5f )
    }

    @Test
    fun whenFontScaleIsSetToTwoAndThreeQuarter_twoAndThreeQuarterFontScaleIsUsed() {
        testFontScale( 2.75f )
    }

    @Test
    fun whenFontScaleIsSetToThree_threeFontScaleIsUsed() {
        testFontScale( 3.0f )
    }

    private fun testFontScale( expectedFontScale: Float ) {
        preferences.setFontScale( expectedFontScale )
        assertEquals( expectedFontScale, preferences.fontScale.value )
    }

}