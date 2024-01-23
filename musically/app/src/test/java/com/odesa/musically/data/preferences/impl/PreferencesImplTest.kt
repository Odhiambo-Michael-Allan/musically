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
    fun testLanguageChange() {
        assertEquals( "Settings", preferences.language.value.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( languageToTest: Language, testString: String ) {
        preferences.setLanguage( languageToTest.locale )
        val currentLanguage = preferences.language.value
        assertEquals( testString, currentLanguage.settings )
        assertEquals( languageToTest.locale, preferenceStore.getLanguage() )
    }

    @Test
    fun testFontChange() {
        assertEquals( SupportedFonts.ProductSans.name, preferences.font.value.name )
        MusicallyTypography.all.values.forEach { changeFontTo( it ) }
    }

    private fun changeFontTo( font: MusicallyFont ) {
        preferences.setFont( font.name )
        val currentFont = preferences.font.value
        assertEquals( font.name, currentFont.name )
        assertEquals( font.name, preferenceStore.getFontName() )
    }

    @Test
    fun testFontScaleChange() {
        assertEquals( SettingsDefaults.fontScale, preferences.fontScale.value )
        scalingPresets.forEach { changeFontScaleTo( it ) }
    }

    private fun changeFontScaleTo( fontScale: Float ) {
        preferences.setFontScale( fontScale )
        assertEquals( fontScale, preferences.fontScale.value )
        assertEquals( fontScale, preferenceStore.getFontScale() )
    }

    @Test
    fun testThemeModeChange() {
        assertEquals( ThemeMode.SYSTEM.name, preferences.themeMode.value.name )
        ThemeMode.entries.forEach {
            changeThemeModeTo( it )
        }
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) {
        preferences.setThemeMode( themeMode )
        assertEquals( themeMode.name, preferences.themeMode.value.name )
    }

    @Test
    fun testUseMaterialYouChange() {
        assertTrue( preferences.useMaterialYou.value )
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }

    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) {
        preferences.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, preferences.useMaterialYou.value )
        assertEquals( useMaterialYou, preferenceStore.getUseMaterialYou() )
    }

    @Test
    fun testPrimaryColorChange() {
        assertEquals( PrimaryThemeColors.Blue.name, preferences.primaryColorName.value )
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
    fun testHomeTabsChange() {
        assertEquals( 5, preferences.homeTabs.value.size )
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
    fun testForYouContentsChange() {
        assertEquals( 2, preferences.forYouContents.value.size )
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
    fun testHomePageBottomBarLabelVisibilityChange() {
        assertEquals( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
            preferences.homePageBottomBarLabelVisibility.value )
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
    fun testFadePlaybackSettingChange() {
        assertFalse( preferences.fadePlayback.value )
        preferences.setFadePlayback( true )
        assertTrue( preferenceStore.getFadePlayback() )
        assertTrue( preferences.fadePlayback.value )
        preferences.setFadePlayback( false )
        assertFalse( preferenceStore.getFadePlayback() )
        assertFalse( preferences.fadePlayback.value )
    }

    @Test
    fun testFadePlaybackDurationChange() {
        assertEquals( SettingsDefaults.fadePlaybackDuration, preferences.fadePlaybackDuration.value )
        for ( duration in 1..100 ) {
            preferences.setFadePlaybackDuration( duration.toFloat() )
            assertEquals( duration.toFloat(), preferenceStore.getFadePlaybackDuration() )
            assertEquals( duration.toFloat(), preferences.fadePlaybackDuration.value )
        }
    }

    @Test
    fun testRequireAudioFocusSettingChange() {
        assertTrue( preferences.requireAudioFocus.value )
        preferences.setRequireAudioFocus( false )
        assertFalse( preferenceStore.getRequireAudioFocus() )
        assertFalse( preferences.requireAudioFocus.value )
        preferences.setRequireAudioFocus( true )
        assertTrue( preferenceStore.getRequireAudioFocus() )
        assertTrue( preferences.requireAudioFocus.value )
    }

    @Test
    fun testIgnoreAudioFocusSettingChange() {
        assertFalse( preferences.ignoreAudioFocusLoss.value )
        preferences.setIgnoreAudioFocusLoss( true )
        assertTrue( preferenceStore.getIgnoreAudioFocusLoss() )
        assertTrue( preferences.ignoreAudioFocusLoss.value )
        preferences.setIgnoreAudioFocusLoss( false )
        assertFalse( preferenceStore.getIgnoreAudioFocusLoss() )
        assertFalse( preferences.ignoreAudioFocusLoss.value )
    }

    @Test
    fun testPlayOnHeadphonesConnectSettingChange() {
        assertFalse( preferences.playOnHeadphonesConnect.value )
        preferences.setPlayOnHeadphonesConnect( true )
        assertTrue( preferenceStore.getPlayOnHeadphonesConnect() )
        assertTrue( preferences.playOnHeadphonesConnect.value )
        preferences.setPlayOnHeadphonesConnect( false )
        assertFalse( preferenceStore.getPlayOnHeadphonesConnect() )
        assertFalse( preferences.playOnHeadphonesConnect.value )
    }

    @Test
    fun testPauseOnHeadphonesDisconnectSettingChange() {
        assertTrue( preferences.pauseOnHeadphonesDisconnect.value )
        preferences.setPauseOnHeadphonesDisconnect( false )
        assertFalse( preferenceStore.getPauseOnHeadphonesDisconnect() )
        assertFalse( preferences.pauseOnHeadphonesDisconnect.value )
        preferences.setPauseOnHeadphonesDisconnect( true )
        assertTrue( preferenceStore.getPauseOnHeadphonesDisconnect() )
        assertTrue( preferences.pauseOnHeadphonesDisconnect.value )
    }

    @Test
    fun testFastRewindDurationSettingChange() {
        assertEquals( SettingsDefaults.fastRewindDuration, preferences.fastRewindDuration.value )
        for ( duration in 3 .. 60 ) {
            preferences.setFastRewindDuration( duration )
            assertEquals( duration, preferenceStore.getFastRewindDuration() )
            assertEquals( duration, preferences.fastRewindDuration.value )
        }
    }

    @Test
    fun testFastForwardDurationSettingChange() {
        assertEquals( SettingsDefaults.fastForwardDuration, preferences.fastForwardDuration.value )
        for ( duration in 3 .. 60 ) {
            preferences.setFastForwardDuration( duration )
            assertEquals( duration, preferenceStore.getFastForwardDuration() )
            assertEquals( duration, preferences.fastForwardDuration.value )
        }
    }
}