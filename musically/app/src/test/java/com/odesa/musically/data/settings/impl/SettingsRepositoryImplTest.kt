package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.FakePreferences
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.ui.settings.appearance.fontScale.scalingPresets
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.MusicallyTypography
import com.odesa.musically.ui.theme.PrimaryThemeColors
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
        MusicallyTypography.all.values.forEach { changeFontTo( it ) }
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
        scalingPresets.forEach { changeFontScaleTo( it ) }
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
        ThemeMode.entries.forEach { changeThemeModeTo( it ) }
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) {
        preferences.setThemeMode( themeMode )
        assertEquals( themeMode.name, settingsRepository.themeMode.value.name )
    }

    @Test
    fun testMaterialYouIsUsedByDefault() {
        assertTrue( settingsRepository.useMaterialYou.value )
    }

    @Test
    fun testUseMaterialYouChange() {
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }
    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) {
        preferences.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, settingsRepository.useMaterialYou.value )
    }

    @Test
    fun testDefaultPrimaryColorNameIsBlue() {
        assertEquals( PrimaryThemeColors.Blue.name, settingsRepository.primaryColorName.value )
    }

    @Test
    fun testPrimaryColorChange() {
        PrimaryThemeColors.entries.forEach {
            changePrimaryColorTo( it.name )
        }
    }

    private fun changePrimaryColorTo( primaryColorName: String ) {
        preferences.setPrimaryColorName( primaryColorName )
        assertEquals( primaryColorName, settingsRepository.primaryColorName.value )
    }

    @Test
    fun testDefaultHomeTabsAreCorrectlySet() {
        assertEquals( 5, settingsRepository.homeTabs.value.size )
    }

    @Test
    fun testHomeTabsChange() {
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums,
            HomeTab.AlbumArtists ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums,
            HomeTab.AlbumArtists, HomeTab.Genres ) )
    }

    private fun changeHomeTabsTo( homeTabs: Set<HomeTab> ) {
        preferences.setHomeTabs( homeTabs )
        assertEquals( homeTabs.size, settingsRepository.homeTabs.value.size )
    }

    @Test
    fun testDefaultForYouContentsAreSetCorrectly() {
        assertEquals( 2, settingsRepository.forYouContent.value.size )
    }

    @Test
    fun testForYouContentsChange() {
        changeForYouContentsTo( setOf( ForYou.Albums ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists, ForYou.AlbumArtists ) )
    }

    private fun changeForYouContentsTo( forYouContents: Set<ForYou> ) {
        preferences.setForYouContents( forYouContents )
        assertEquals( forYouContents.size, settingsRepository.forYouContent.value.size )
    }

    @Test
    fun testDefaultHomePageBottomBarLabelVisibilityIsSetCorrectly() {
        assertEquals( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
            settingsRepository.homePageBottomBarLabelVisibility.value )
    }

    @Test
    fun testHomePageBottomBarLabelVisibilityChange() {
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.INVISIBLE )
    }

    private fun changeHomePageBottomBarLabelVisibilityTo( value: HomePageBottomBarLabelVisibility ) {
        preferences.setHomePageBottomBarLabelVisibility( value )
        assertEquals( value, settingsRepository.homePageBottomBarLabelVisibility.value )
    }

    @Test
    fun testDefaultFadePlaybackSettingValueIsSetCorrectly() {
        assertFalse( settingsRepository.fadePlayback.value )
    }

    @Test
    fun testFadePlaybackSettingChange() {
        preferences.setFadePlayback( true )
        assertTrue( settingsRepository.fadePlayback.value )
        preferences.setFadePlayback( false )
        assertFalse( settingsRepository.fadePlayback.value )
    }

    @Test
    fun testDefaultFadePlaybackIsSetCorrectly() {
        assertEquals( SettingsDefaults.fadePlaybackDuration, settingsRepository.fadePlaybackDuration.value )
    }

    @Test
    fun testFadePlaybackDurationChange() {
        for ( duration in 1..100 ) {
            preferences.setFadePlaybackDuration( duration.toFloat() )
            assertEquals( duration.toFloat(), settingsRepository.fadePlaybackDuration.value )
        }
    }

    @Test
    fun testDefaultRequireAudioFocusSettingIsSetCorrectly() {
        assertTrue( settingsRepository.requireAudioFocus.value )
    }

    @Test
    fun testRequireAudioFocusSettingChange() {
        preferences.setRequireAudioFocus( false )
        assertFalse( settingsRepository.requireAudioFocus.value )
        preferences.setRequireAudioFocus( true )
        assertTrue( settingsRepository.requireAudioFocus.value )
    }

    @Test
    fun testDefaultIgnoreAudioFocusLossIsSetCorrectly() {
        assertFalse( settingsRepository.ignoreAudioFocusLoss.value )
    }

    @Test
    fun testIgnoreAudioFocusLossSettingChange() {
        preferences.setIgnoreAudioFocusLoss( false )
        assertFalse( settingsRepository.ignoreAudioFocusLoss.value )
        preferences.setIgnoreAudioFocusLoss( true )
        assertTrue( settingsRepository.ignoreAudioFocusLoss.value )
    }

    @Test
    fun testDefaultPlayOnHeadphonesConnectSettingIsSetCorrectly() {
        assertFalse( settingsRepository.playOnHeadphonesConnect.value )
    }

    @Test
    fun testPlayOnHeadphonesConnectSettingChange() {
        preferences.setPlayOnHeadphonesConnect( true )
        assertTrue( settingsRepository.playOnHeadphonesConnect.value )
        preferences.setPlayOnHeadphonesConnect( false )
        assertFalse( settingsRepository.playOnHeadphonesConnect.value )
    }

}