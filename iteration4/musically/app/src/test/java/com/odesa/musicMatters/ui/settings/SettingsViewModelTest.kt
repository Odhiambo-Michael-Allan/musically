package com.odesa.musicMatters.ui.settings

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.preferences.ForYou
import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
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
import org.junit.Rule
import org.junit.Test


class SettingsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        settingsViewModel = SettingsViewModel( settingsRepository )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", settingsViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testFontChange() = runTest {
        assertEquals( SupportedFonts.ProductSans.name, settingsViewModel.uiState.value.font.name )
        MusicallyTypography.all.values.forEach { changeFontTo( it ) }
    }


    private fun changeFontTo( font: MusicallyFont ) = runTest {
        settingsRepository.setFont( font.name )
        val currentFont = settingsViewModel.uiState.value.font
        assertEquals( font.name, currentFont.name )
    }

    @Test
    fun testFontScaleChange() = runTest {
        assertEquals( 1.0f, settingsViewModel.uiState.value.fontScale )
        scalingPresets.forEach { changeFontScaleTo( it )  }
    }

    private fun changeFontScaleTo( fontScale: Float ) = runTest {
        settingsRepository.setFontScale( fontScale )
        val currentFontScale = settingsViewModel.uiState.value.fontScale
        assertEquals( fontScale, currentFontScale )
    }

    @Test
    fun whenValidFontScaleStringIsProvided_settingsViewModelCorrectlyParsesIt() {
        settingsViewModel.setFontScale( "1.25" )
        assertEquals( "1.25", settingsViewModel.uiState.value.fontScale.toString() )
    }

    @Test
    fun whenInvalidFontScaleIsProvided_settingsViewModelCorrectlyIdentifiesIt() {
        settingsViewModel.setFontScale( "1245323" )
        assertEquals( "1.0", settingsViewModel.uiState.value.fontScale.toString() )
    }

    @Test
    fun testThemeModeChange() {
        assertEquals( ThemeMode.SYSTEM.name, settingsViewModel.uiState.value.themeMode.name )
        ThemeMode.entries.forEach { changeThemeModeTo( it ) }
    }

    private fun changeThemeModeTo( themeMode: ThemeMode ) = runTest {
        settingsRepository.setThemeMode( themeMode )
        assertEquals( themeMode.name, settingsViewModel.uiState.value.themeMode.name )
    }

    @Test
    fun testUseMaterialYouChange() {
        assertTrue( settingsViewModel.uiState.value.useMaterialYou )
        changeUseMaterialYouTo( true )
        changeUseMaterialYouTo( false )
    }

    private fun changeUseMaterialYouTo( useMaterialYou: Boolean ) = runTest {
        settingsRepository.setUseMaterialYou( useMaterialYou )
        assertEquals( useMaterialYou, settingsViewModel.uiState.value.useMaterialYou )
    }

    @Test
    fun testPrimaryColorNameChange() {
        assertEquals( SettingsDefaults.primaryColorName, settingsViewModel.uiState.value.primaryColorName )
        PrimaryThemeColors.entries.forEach {
            changePrimaryColorTo( it.name )
        }
    }

    private fun changePrimaryColorTo( primaryColorName: String ) = runTest {
        settingsRepository.setPrimaryColorName( primaryColorName )
        assertEquals( primaryColorName, settingsViewModel.uiState.value.primaryColorName )
    }

    @Test
    fun testHomeTabsChange() {
        assertEquals( 5, settingsViewModel.uiState.value.homeTabs.size )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums ) )
        changeHomeTabsTo( setOf( HomeTab.ForYou, HomeTab.Songs, HomeTab.Albums, HomeTab.Genres ) )
    }

    private fun changeHomeTabsTo( homeTabs: Set<HomeTab> ) = runTest {
        settingsRepository.setHomeTabs( homeTabs )
        val currentHomeTabsSize = settingsViewModel.uiState.value.homeTabs.size
        assertEquals( homeTabs.size, currentHomeTabsSize )
    }

    @Test
    fun testForYouContentsChange() {
        assertEquals( 2, settingsViewModel.uiState.value.forYouContent.size )
        changeForYouContentsTo( setOf( ForYou.Albums ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists ) )
        changeForYouContentsTo( setOf( ForYou.Albums, ForYou.Artists, ForYou.AlbumArtists ) )
    }

    private fun changeForYouContentsTo( forYouContents: Set<ForYou> ) = runTest {
        settingsRepository.setForYouContents( forYouContents )
        assertEquals( forYouContents.size, settingsViewModel.uiState.value.forYouContent.size )
    }

    @Test
    fun testHomePageBottomBarLabelVisibilityChange() {
        assertEquals( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
            settingsViewModel.uiState.value.homePageBottomBarLabelVisibility )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE )
        changeHomePageBottomBarLabelVisibilityTo( HomePageBottomBarLabelVisibility.INVISIBLE )
    }

    private fun changeHomePageBottomBarLabelVisibilityTo(
        value: HomePageBottomBarLabelVisibility
    ) = runTest {
        settingsRepository.setHomePageBottomBarLabelVisibility( value )
        assertEquals( value, settingsViewModel.uiState.value.homePageBottomBarLabelVisibility )
    }

    @Test
    fun testFadePlaybackSettingChange() = runTest {
        assertFalse( settingsViewModel.uiState.value.fadePlayback )
        settingsRepository.setFadePlayback( true )
        assertTrue( settingsViewModel.uiState.value.fadePlayback )
        settingsRepository.setFadePlayback( false )
        assertFalse( settingsViewModel.uiState.value.fadePlayback )
    }

    @Test
    fun testFadePlaybackDurationChange() = runTest {
        assertEquals( SettingsDefaults.fadePlaybackDuration,
            settingsViewModel.uiState.value.fadePlaybackDuration )
        for ( duration in 1..100 ) {
            settingsRepository.setFadePlaybackDuration( duration.toFloat() )
            assertEquals( duration.toFloat(), settingsViewModel.uiState.value.fadePlaybackDuration )
        }
    }


    @Test
    fun testRequireAudioFocusSettingChange() = runTest {
        assertTrue( settingsViewModel.uiState.value.requireAudioFocus )
        settingsRepository.setRequireAudioFocus( false )
        assertFalse( settingsViewModel.uiState.value.requireAudioFocus )
        settingsRepository.setRequireAudioFocus( true )
        assertTrue( settingsViewModel.uiState.value.requireAudioFocus )
    }

    @Test
    fun testIgnoreAudioFocusLossSettingChange() = runTest {
        assertFalse( settingsViewModel.uiState.value.ignoreAudioFocusLoss )
        settingsRepository.setIgnoreAudioFocusLoss( true )
        assertTrue( settingsViewModel.uiState.value.ignoreAudioFocusLoss )
        settingsRepository.setIgnoreAudioFocusLoss( false )
        assertFalse( settingsViewModel.uiState.value.ignoreAudioFocusLoss )
    }

    @Test
    fun testPlayOnHeadphonesConnectSettingChange() = runTest {
        assertFalse( settingsViewModel.uiState.value.playOnHeadphonesConnect )
        settingsRepository.setPlayOnHeadphonesConnect( true )
        assertTrue( settingsViewModel.uiState.value.playOnHeadphonesConnect )
        settingsRepository.setPlayOnHeadphonesConnect( false )
        assertFalse( settingsViewModel.uiState.value.playOnHeadphonesConnect )
    }

    @Test
    fun testPauseOnHeadphonesDisconnectSettingChange() = runTest {
        assertTrue( settingsViewModel.uiState.value.pauseOnHeadphonesDisconnect )
        settingsRepository.setPauseOnHeadphonesDisconnect( false )
        assertFalse( settingsViewModel.uiState.value.pauseOnHeadphonesDisconnect )
        settingsRepository.setPauseOnHeadphonesDisconnect( true )
        assertTrue( settingsViewModel.uiState.value.pauseOnHeadphonesDisconnect )
    }

    @Test
    fun testFastRewindDurationSettingChange() = runTest {
        assertEquals( SettingsDefaults.fastRewindDuration,
            settingsViewModel.uiState.value.fastRewindDuration )
        for ( duration in 3 .. 60 ) {
            settingsRepository.setFastRewindDuration( duration )
            assertEquals( duration, settingsViewModel.uiState.value.fastRewindDuration )
        }
    }

    @Test
    fun testFastForwardDurationSettingChange() = runTest {
        assertEquals( SettingsDefaults.fastForwardDuration,
            settingsViewModel.uiState.value.fastForwardDuration )
        for ( duration in 3 .. 60 ) {
            settingsRepository.setFastForwardDuration( duration )
            assertEquals( duration, settingsViewModel.uiState.value.fastForwardDuration )
        }
    }

    @Test
    fun testMiniPlayerShowTrackControlsSettingChange() = runTest {
        assertTrue( settingsViewModel.uiState.value.miniPlayerShowTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( false )
        assertFalse( settingsViewModel.uiState.value.miniPlayerShowTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( true )
        assertTrue( settingsViewModel.uiState.value.miniPlayerShowTrackControls )
    }

    @Test
    fun testMiniPlayerShowSeekControlsSettingChange() = runTest {
        assertFalse( settingsViewModel.uiState.value.miniPlayerShowSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( true )
        assertTrue( settingsViewModel.uiState.value.miniPlayerShowSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( false )
        assertFalse( settingsViewModel.uiState.value.miniPlayerShowSeekControls )
    }

    @Test
    fun testMiniPlayerTextMarqueeSettingChange() = runTest {
        assertTrue( settingsViewModel.uiState.value.miniPlayerTextMarquee )
        settingsRepository.setMiniPlayerTextMarquee( false )
        assertFalse( settingsViewModel.uiState.value.miniPlayerTextMarquee )
        settingsRepository.setMiniPlayerTextMarquee( true )
        assertTrue( settingsViewModel.uiState.value.miniPlayerTextMarquee )
    }

    @Test
    fun testNowPlayingControlsLayoutSettingChange() = runTest {
        assertEquals( SettingsDefaults.controlsLayoutIsDefault,
            settingsViewModel.uiState.value.controlsLayoutIsDefault )
        listOf( true, false ).forEach {
            settingsRepository.setControlsLayoutIsDefault( it )
            assertEquals( it, settingsViewModel.uiState.value.controlsLayoutIsDefault )
        }
    }

    @Test
    fun testNowPlayingLyricsLayoutSettingChange() = runTest {
        assertEquals( SettingsDefaults.nowPlayingLyricsLayout,
            settingsViewModel.uiState.value.nowPlayingLyricsLayout )
        NowPlayingLyricsLayout.entries.forEach {
            settingsRepository.setNowPlayingLyricsLayout( it )
            assertEquals( it, settingsViewModel.uiState.value.nowPlayingLyricsLayout )
        }
    }

    @Test
    fun testShowNowPlayingAudioInformationSettingChange() = runTest {
        assertEquals( SettingsDefaults.showNowPlayingAudioInformation,
            settingsViewModel.uiState.value.showNowPlayingAudioInformation )
        listOf( true, false ).forEach {
            settingsRepository.setShowNowPlayingAudioInformation( it )
            assertEquals( it, settingsViewModel.uiState.value.showNowPlayingAudioInformation )
        }
    }

    @Test
    fun testShowNowPlayingSeekControlsSettingChange() = runTest {
        assertEquals( SettingsDefaults.showNowPlayingSeekControls,
            settingsViewModel.uiState.value.showNowPlayingSeekControls )
        listOf( true, false ).forEach {
            settingsRepository.setShowNowPlayingSeekControls( it )
            assertEquals( it, settingsViewModel.uiState.value.showNowPlayingSeekControls )
        }
    }

}