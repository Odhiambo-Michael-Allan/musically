package com.odesa.musicMatters.data.settings.impl

import com.odesa.musicMatters.data.preferences.ForYou
import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.PreferenceStore
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.preferences.impl.allowedSpeedPitchValues
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakePreferencesStoreImpl
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.ui.settings.appearance.scalingPresets
import com.odesa.musicMatters.ui.theme.MusicallyFont
import com.odesa.musicMatters.ui.theme.MusicallyTypography
import com.odesa.musicMatters.ui.theme.PrimaryThemeColors
import com.odesa.musicMatters.ui.theme.SupportedFonts
import com.odesa.musicMatters.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private lateinit var preferenceStore: PreferenceStore
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        preferenceStore = FakePreferencesStoreImpl()
        settingsRepository = SettingsRepositoryImpl( preferenceStore )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", settingsRepository.language.value.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        assertEquals( language.locale, preferenceStore.getLanguage() )
        val currentLanguage = settingsRepository.language.value
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testFontChange() {
        assertEquals( SupportedFonts.ProductSans.name, settingsRepository.font.value.name )
        MusicallyTypography.all.values.forEach { changeFontTo( it ) }
    }

    private fun changeFontTo( font: MusicallyFont ) = runTest {
        settingsRepository.setFont( font.name )
        assertEquals( font.name, preferenceStore.getFontName() )
        val currentFont = settingsRepository.font.value
        assertEquals( font.name, currentFont.name )
    }


    @Test
    fun testFontScaleChange() {
        assertEquals( SettingsDefaults.fontScale, settingsRepository.fontScale.value )
        scalingPresets.forEach { changeFontScaleTo( it ) }
    }

    private fun changeFontScaleTo( fontScale: Float ) = runTest {
        settingsRepository.setFontScale( fontScale )
        assertEquals( fontScale, preferenceStore.getFontScale() )
        assertEquals( fontScale, settingsRepository.fontScale.value )
    }

    @Test
    fun testThemeModeChange() {
        assertEquals( ThemeMode.SYSTEM.name, settingsRepository.themeMode.value.name )
        ThemeMode.entries.forEach { changeThemeModeTo( it ) }
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) = runTest {
        settingsRepository.setThemeMode( themeMode )
        assertEquals( themeMode, preferenceStore.getThemeMode() )
        assertEquals( themeMode, settingsRepository.themeMode.value )
    }

    @Test
    fun testUseMaterialYouChange() {
        assertTrue( settingsRepository.useMaterialYou.value )
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }
    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) = runTest {
        settingsRepository.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, preferenceStore.getUseMaterialYou() )
        assertEquals( useMaterialYou, settingsRepository.useMaterialYou.value )
    }

    @Test
    fun testPrimaryColorChange() {
        assertEquals( SettingsDefaults.primaryColorName, settingsRepository.primaryColorName.value )
        PrimaryThemeColors.entries.forEach {
            changePrimaryColorTo( it.name )
        }
    }

    private fun changePrimaryColorTo( primaryColorName: String ) = runTest {
        settingsRepository.setPrimaryColorName( primaryColorName )
        assertEquals( primaryColorName, preferenceStore.getPrimaryColorName() )
        assertEquals( primaryColorName, settingsRepository.primaryColorName.value )
    }

    @Test
    fun testHomeTabsChange() {
        assertEquals( 5, settingsRepository.homeTabs.value.size )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums, HomeTab.Genres ) )
    }

    private fun changeHomeTabsTo( homeTabs: Set<HomeTab> ) = runTest {
        settingsRepository.setHomeTabs( homeTabs )
        assertEquals( homeTabs.size, preferenceStore.getHomeTabs().size )
        assertEquals( homeTabs.size, settingsRepository.homeTabs.value.size )
    }

    @Test
    fun testForYouContentsChange() {
        assertEquals( 2, settingsRepository.forYouContents.value.size )
        changeForYouContentsTo( setOf( ForYou.Albums ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists, ForYou.AlbumArtists ) )
    }

    private fun changeForYouContentsTo( forYouContents: Set<ForYou> ) = runTest {
        settingsRepository.setForYouContents( forYouContents )
        assertEquals( forYouContents.size, preferenceStore.getForYouContents().size )
        assertEquals( forYouContents.size, settingsRepository.forYouContents.value.size )
    }

    @Test
    fun testHomePageBottomBarLabelVisibilityChange() {
        assertEquals( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
            settingsRepository.homePageBottomBarLabelVisibility.value )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.INVISIBLE )
    }

    private fun changeHomePageBottomBarLabelVisibilityTo(
        value: HomePageBottomBarLabelVisibility
    ) = runTest {
        settingsRepository.setHomePageBottomBarLabelVisibility( value )
        assertEquals( value, preferenceStore.getHomePageBottomBarLabelVisibility() )
        assertEquals( value, settingsRepository.homePageBottomBarLabelVisibility.value )
    }

    @Test
    fun testFadePlaybackSettingChange() = runTest {
        assertFalse( settingsRepository.fadePlayback.value )
        settingsRepository.setFadePlayback( true )
        assertTrue( preferenceStore.getFadePlayback() )
        assertTrue( settingsRepository.fadePlayback.value )
        settingsRepository.setFadePlayback( false )
        assertFalse( preferenceStore.getFadePlayback() )
        assertFalse( settingsRepository.fadePlayback.value )
    }

    @Test
    fun testFadePlaybackDurationChange() = runTest {
        assertEquals( SettingsDefaults.fadePlaybackDuration,
            settingsRepository.fadePlaybackDuration.value )
        for ( duration in 1..100 ) {
            settingsRepository.setFadePlaybackDuration( duration.toFloat() )
            assertEquals( duration.toFloat(), preferenceStore.getFadePlaybackDuration() )
            assertEquals( duration.toFloat(), settingsRepository.fadePlaybackDuration.value )
        }
    }

    @Test
    fun testRequireAudioFocusSettingChange() = runTest {
        assertTrue( settingsRepository.requireAudioFocus.value )
        settingsRepository.setRequireAudioFocus( false )
        assertFalse( preferenceStore.getRequireAudioFocus() )
        assertFalse( settingsRepository.requireAudioFocus.value )
        settingsRepository.setRequireAudioFocus( true )
        assertTrue( preferenceStore.getRequireAudioFocus() )
        assertTrue( settingsRepository.requireAudioFocus.value )
    }

    @Test
    fun testIgnoreAudioFocusLossSettingChange() = runTest {
        assertFalse( settingsRepository.ignoreAudioFocusLoss.value )
        settingsRepository.setIgnoreAudioFocusLoss( false )
        assertFalse( preferenceStore.getIgnoreAudioFocusLoss() )
        assertFalse( settingsRepository.ignoreAudioFocusLoss.value )
        settingsRepository.setIgnoreAudioFocusLoss( true )
        assertTrue( preferenceStore.getIgnoreAudioFocusLoss() )
        assertTrue( settingsRepository.ignoreAudioFocusLoss.value )
    }

    @Test
    fun testPlayOnHeadphonesConnectSettingChange() = runTest {
        assertFalse( settingsRepository.playOnHeadphonesConnect.value )
        settingsRepository.setPlayOnHeadphonesConnect( true )
        assertTrue( preferenceStore.getPlayOnHeadphonesConnect() )
        assertTrue( settingsRepository.playOnHeadphonesConnect.value )
        settingsRepository.setPlayOnHeadphonesConnect( false )
        assertFalse( preferenceStore.getPlayOnHeadphonesConnect() )
        assertFalse( settingsRepository.playOnHeadphonesConnect.value )
    }

    @Test
    fun testPauseOnHeadphonesDisconnectSettingChange() = runTest {
        assertTrue( settingsRepository.pauseOnHeadphonesDisconnect.value )
        settingsRepository.setPauseOnHeadphonesDisconnect( false )
        assertFalse( preferenceStore.getPauseOnHeadphonesDisconnect() )
        assertFalse( settingsRepository.pauseOnHeadphonesDisconnect.value )
        settingsRepository.setPauseOnHeadphonesDisconnect( true )
        assertTrue( preferenceStore.getPauseOnHeadphonesDisconnect() )
        assertTrue( settingsRepository.pauseOnHeadphonesDisconnect.value )
    }

    @Test
    fun testFastRewindDurationSettingChange() = runTest {
        assertEquals( SettingsDefaults.fastRewindDuration,
            settingsRepository.fastRewindDuration.value )
        for ( duration in 3..60 ) {
            settingsRepository.setFastRewindDuration( duration )
            assertEquals( duration, preferenceStore.getFastRewindDuration() )
            assertEquals( duration, settingsRepository.fastRewindDuration.value )
        }
    }

    @Test
    fun testFastForwardDurationSettingChange() = runTest {
        assertEquals( SettingsDefaults.fastForwardDuration,
            settingsRepository.fastForwardDuration.value )
        for ( duration in 3..60 ) {
            settingsRepository.setFastForwardDuration( duration )
            assertEquals( duration, preferenceStore.getFastForwardDuration() )
            assertEquals( duration, settingsRepository.fastForwardDuration.value )
        }
    }

    @Test
    fun testMiniPlayerShowTrackControlsSettingChange() = runTest {
        assertTrue( settingsRepository.miniPlayerShowTrackControls.value )
        settingsRepository.setMiniPlayerShowTrackControls( false )
        assertFalse( preferenceStore.getMiniPlayerShowTrackControls() )
        assertFalse( settingsRepository.miniPlayerShowTrackControls.value )
        settingsRepository.setMiniPlayerShowTrackControls( true )
        assertTrue( preferenceStore.getMiniPlayerShowTrackControls() )
        assertTrue( settingsRepository.miniPlayerShowTrackControls.value )
    }

    @Test
    fun testMiniPlayerShowSeekControlsSettingChange() = runTest {
        assertFalse( settingsRepository.miniPlayerShowSeekControls.value )
        settingsRepository.setMiniPlayerShowSeekControls( true )
        assertTrue( preferenceStore.getMiniPlayerShowSeekControls() )
        assertTrue( settingsRepository.miniPlayerShowSeekControls.value )
        settingsRepository.setMiniPlayerShowSeekControls( false )
        assertFalse( preferenceStore.getMiniPlayerShowSeekControls() )
        assertFalse( settingsRepository.miniPlayerShowSeekControls.value )
    }

    @Test
    fun testMiniPlayerTextMarqueeSettingChange() = runTest {
        assertTrue( settingsRepository.miniPlayerTextMarquee.value )
        settingsRepository.setMiniPlayerTextMarquee( false )
        assertFalse( preferenceStore.getMiniPlayerTextMarquee() )
        assertFalse( settingsRepository.miniPlayerTextMarquee.value )
        settingsRepository.setMiniPlayerTextMarquee( true )
        assertTrue( preferenceStore.getMiniPlayerTextMarquee() )
        assertTrue( settingsRepository.miniPlayerTextMarquee.value)
    }

    @Test
    fun testNowPlayingControlsLayoutSettingChange() = runTest {
        assertEquals( SettingsDefaults.controlsLayoutIsDefault,
            settingsRepository.controlsLayoutIsDefault.value )
        settingsRepository.setControlsLayoutIsDefault( false )
        assertFalse( preferenceStore.getControlsLayoutIsDefault() )
        assertFalse( settingsRepository.controlsLayoutIsDefault.value )
        settingsRepository.setControlsLayoutIsDefault( true )
        assertTrue( preferenceStore.getControlsLayoutIsDefault() )
        assertTrue( settingsRepository.controlsLayoutIsDefault.value )
    }

    @Test
    fun testNowPlayingLyricsLayoutSettingChange() = runTest {
        assertEquals( SettingsDefaults.nowPlayingLyricsLayout,
            settingsRepository.nowPlayingLyricsLayout.value )
        NowPlayingLyricsLayout.entries.forEach {
            settingsRepository.setNowPlayingLyricsLayout( it )
            assertEquals( it, preferenceStore.getNowPlayingLyricsLayout() )
            assertEquals( it, settingsRepository.nowPlayingLyricsLayout.value )
        }
    }

    @Test
    fun testShowNowPlayingAudioInformationSettingChange() = runTest {
        assertTrue( settingsRepository.showNowPlayingAudioInformation.value )
        listOf( true, false ).forEach {
            settingsRepository.setShowNowPlayingAudioInformation( it )
            assertEquals( it, preferenceStore.getShowNowPlayingAudioInformation() )
            assertEquals( it, settingsRepository.showNowPlayingAudioInformation.value )
        }
    }

    @Test
    fun testShowNowPlayingSeekControlsSettingChange() = runTest {
        assertFalse( settingsRepository.showNowPlayingSeekControls.value )
        listOf( true, false ).forEach {
            settingsRepository.setShowNowPlayingSeekControls( it )
            assertEquals( it, preferenceStore.getShowNowPlayingSeekControls() )
            assertEquals( it, settingsRepository.showNowPlayingSeekControls.value )
        }
    }

    @Test
    fun testShowLyricsSettingChange() = runTest {
        assertFalse( settingsRepository.showLyrics.value )
        listOf( true, false ).forEach {
            settingsRepository.setShowLyrics( it )
            assertEquals( it, preferenceStore.getShowLyrics() )
            assertEquals( it, settingsRepository.showLyrics.value )
        }
    }

    @Test
    fun testShuffleChange() = runTest {
        assertFalse( settingsRepository.shuffle.value )
        listOf( true, false ).forEach {
            settingsRepository.setShuffle( it )
            assertEquals( it, preferenceStore.getShuffle() )
            assertEquals( it, settingsRepository.shuffle.value )
        }
    }

    @Test
    fun testLoopModeChange() = runTest {
        assertEquals( SettingsDefaults.loopMode, settingsRepository.currentLoopMode.value )
        LoopMode.entries.forEach {
            settingsRepository.setCurrentLoopMode( it )
            assertEquals( it, preferenceStore.getCurrentLoopMode() )
            assertEquals( it, settingsRepository.currentLoopMode.value )
        }
    }

    @Test
    fun testCurrentPlayingSpeedChange() = runTest {
        assertEquals( SettingsDefaults.currentPlayingSpeed,
            settingsRepository.currentPlayingSpeed.value )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingSpeed( it )
            assertEquals( it, preferenceStore.getCurrentPlayingSpeed() )
            assertEquals( it, settingsRepository.currentPlayingSpeed.value )
        }
    }

    @Test
    fun testCurrentPlayingPitchChange() = runTest {
        assertEquals( SettingsDefaults.currentPlayingPitch,
            settingsRepository.currentPlayingPitch.value )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingPitch( it )
            assertEquals( it, preferenceStore.getCurrentPlayingPitch() )
            assertEquals( it, settingsRepository.currentPlayingPitch.value )
        }
    }

}