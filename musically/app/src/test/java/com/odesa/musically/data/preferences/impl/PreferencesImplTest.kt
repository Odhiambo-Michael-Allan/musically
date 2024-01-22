package com.odesa.musically.data.preferences.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.FakePreferencesStoreImpl
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
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

    private fun testLanguage( languageToTest: Language, testString: String ) {
        preferences.setLanguage( languageToTest.locale )
        val currentLanguage = preferences.language.value
        assertEquals( testString, currentLanguage.settings )
        assertEquals( languageToTest.locale, preferenceStore.getLanguage() )
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
        assertEquals( font.name, preferenceStore.getFontName() )
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
        assertEquals( fontScale, preferenceStore.getFontScale() )
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
        assertEquals( useMaterialYou, preferenceStore.getUseMaterialYou() )
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
        assertEquals( primaryColorName, preferenceStore.getPrimaryColorName() )
    }

    @Test
    fun testDefaultHomeTabsAreSetCorrectly() {
        val homeTabs = preferences.homeTabs.value
        assertEquals( 5, homeTabs.size )
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
        assertEquals( homeTabs.size, preferences.homeTabs.value.size )
        assertEquals( homeTabs.size, preferenceStore.getHomeTabs().size )
    }

    @Test
    fun testDefaultForYouContentsAreSetCorrectly() {
        assertEquals( 2, preferences.forYouContents.value.size )
    }

    @Test
    fun testForYouContentsChange() {
        changeForYouContentsTo( setOf( ForYou.Albums ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists, ForYou.AlbumArtists ) )
    }

    private fun changeForYouContentsTo( forYouContents: Set<ForYou> ) {
        preferences.setForYouContents( forYouContents )
        assertEquals( forYouContents.size, preferences.forYouContents.value.size )
        assertEquals( forYouContents.size, preferenceStore.getForYouContents().size )
    }

    @Test
    fun testDefaultHomePageBottomBarLabelVisibilityIsSetCorrectly() {
        assertEquals( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
            preferences.homePageBottomBarLabelVisibility.value )
    }

    @Test
    fun testHomePageBottomBarLabelVisibilityChange() {
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.INVISIBLE )
    }

    private fun changeHomePageBottomBarLabelVisibilityTo( value: HomePageBottomBarLabelVisibility ) {
        preferences.setHomePageBottomBarLabelVisibility( value )
        assertEquals( value, preferences.homePageBottomBarLabelVisibility.value )
        assertEquals( value, preferenceStore.getHomePageBottomBarLabelVisibility() )
    }

    @Test
    fun testDefaultFadePlaybackSettingIsSetCorrectly() {
        assertFalse( preferences.fadePlayback.value )
    }

    @Test
    fun testFadePlaybackSettingChange() {
        preferences.setFadePlayback( true )
        assertTrue( preferenceStore.getFadePlayback() )
        assertTrue( preferences.fadePlayback.value )
        preferences.setFadePlayback( false )
        assertFalse( preferenceStore.getFadePlayback() )
        assertFalse( preferences.fadePlayback.value )
    }

    @Test
    fun testDefaultFadePlaybackDurationIsSetCorrectly() {
        assertEquals( SettingsDefaults.fadePlaybackDuration, preferences.fadePlaybackDuration.value )
    }

    @Test
    fun testFadePlaybackDurationChange() {
        for ( duration in 1..100 ) {
            preferences.setFadePlaybackDuration( duration.toFloat() )
            assertEquals( duration.toFloat(), preferenceStore.getFadePlaybackDuration() )
            assertEquals( duration.toFloat(), preferences.fadePlaybackDuration.value )
        }
    }

    @Test
    fun testDefaultRequireAudioFocusSettingIsSetCorrectly() {
        assertTrue( preferences.requireAudioFocus.value )
    }

    @Test
    fun testRequireAudioFocusSettingChange() {
        preferences.setRequireAudioFocus( false )
        assertFalse( preferenceStore.getRequireAudioFocus() )
        assertFalse( preferences.requireAudioFocus.value )
        preferences.setRequireAudioFocus( true )
        assertTrue( preferenceStore.getRequireAudioFocus() )
        assertTrue( preferences.requireAudioFocus.value )
    }
}