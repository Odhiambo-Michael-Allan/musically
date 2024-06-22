package com.odesa.musicMatters.data.settings.impl


import com.odesa.musicMatters.core.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.core.data.preferences.LoopMode
import com.odesa.musicMatters.core.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.core.data.preferences.PreferenceStore
import com.odesa.musicMatters.core.data.preferences.SortAlbumsBy
import com.odesa.musicMatters.core.data.preferences.SortArtistsBy
import com.odesa.musicMatters.core.data.preferences.SortGenresBy
import com.odesa.musicMatters.core.data.preferences.SortPathsBy
import com.odesa.musicMatters.core.data.preferences.SortPlaylistsBy
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.data.preferences.allowedSpeedPitchValues
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.data.settings.impl.SettingsRepositoryImpl
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.datatesting.store.FakePreferencesStoreImpl
import com.odesa.musicMatters.core.designsystem.theme.MusicallyFont
import com.odesa.musicMatters.core.designsystem.theme.MusicallyTypography
import com.odesa.musicMatters.core.designsystem.theme.PrimaryThemeColors
import com.odesa.musicMatters.core.designsystem.theme.SupportedFonts
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.Belarusian
import com.odesa.musicMatters.core.i8n.Chinese
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.French
import com.odesa.musicMatters.core.i8n.German
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.i8n.Spanish
import com.odesa.musicMatters.ui.settings.appearance.scalingPresets
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
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

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
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

    private fun changeFontTo( font: MusicallyFont) = runTest {
        settingsRepository.setFont( font.name )
        assertEquals( font.name, preferenceStore.getFontName() )
        val currentFont = settingsRepository.font.value
        assertEquals( font.name, currentFont.name )
    }


    @Test
    fun testFontScaleChange() {
        assertEquals( SettingsDefaults.FONT_SCALE, settingsRepository.fontScale.value )
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
        assertEquals( SettingsDefaults.PRIMARY_COLOR_NAME, settingsRepository.primaryColorName.value )
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
        assertEquals( SettingsDefaults.FADE_PLAYBACK_DURATION,
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
        assertEquals( SettingsDefaults.FAST_REWIND_DURATION,
            settingsRepository.fastRewindDuration.value )
        for ( duration in 3..60 ) {
            settingsRepository.setFastRewindDuration( duration )
            assertEquals( duration, preferenceStore.getFastRewindDuration() )
            assertEquals( duration, settingsRepository.fastRewindDuration.value )
        }
    }

    @Test
    fun testFastForwardDurationSettingChange() = runTest {
        assertEquals( SettingsDefaults.FAST_FORWARD_DURATION,
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
        assertEquals( SettingsDefaults.CONTROLS_LAYOUT_IS_DEFAULT,
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
        assertFalse( settingsRepository.showNowPlayingAudioInformation.value )
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
        assertEquals( SettingsDefaults.CURRENT_PLAYING_SPEED,
            settingsRepository.currentPlaybackSpeed.value )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingSpeed( it )
            assertEquals( it, preferenceStore.getCurrentPlayingSpeed() )
            assertEquals( it, settingsRepository.currentPlaybackSpeed.value )
        }
    }

    @Test
    fun testCurrentPlayingPitchChange() = runTest {
        assertEquals( SettingsDefaults.CURRENT_PLAYING_PITCH,
            settingsRepository.currentPlaybackPitch.value )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingPitch( it )
            assertEquals( it, preferenceStore.getCurrentPlayingPitch() )
            assertEquals( it, settingsRepository.currentPlaybackPitch.value )
        }
    }

    @Test
    fun testCurrentlyDisabledTreePathsChange() = runTest {
        assertEquals( 0, settingsRepository.currentlyDisabledTreePaths.value.size )
        settingsRepository.setCurrentlyDisabledTreePaths(
            listOf( "path-1", "path-2", "path-3", "path-4", "path-5" )
        )
        assertEquals( 5, preferenceStore.getCurrentlyDisabledTreePaths().size )
        assertEquals( 5, settingsRepository.currentlyDisabledTreePaths.value.size )
    }

    @Test
    fun testSortSongsByValueIsSetCorrectly() = runTest {
        assertEquals( SortSongsBy.TITLE, settingsRepository.sortSongsBy.value )
        SortSongsBy.entries.forEach {
            settingsRepository.setSortSongsBy( it )
            assertEquals( it, preferenceStore.getSortSongsBy() )
            assertEquals( it, settingsRepository.sortSongsBy.value )
        }
    }

    @Test
    fun testSortSongsInReverseIsCorrectlySet() = runTest {
        assertFalse( settingsRepository.sortSongsInReverse.value )
        settingsRepository.setSortSongsInReverse( true )
        assertTrue( preferenceStore.getSortSongsInReverse() )
        assertTrue( settingsRepository.sortSongsInReverse.value )
        settingsRepository.setSortSongsInReverse( false )
        assertFalse( preferenceStore.getSortSongsInReverse() )
        assertFalse( settingsRepository.sortSongsInReverse.value )
    }

    @Test
    fun testSortArtistsByValueIsSetCorrectly() = runTest {
        assertEquals( SortArtistsBy.ARTIST_NAME, settingsRepository.sortArtistsBy.value )
        SortArtistsBy.entries.forEach {
            settingsRepository.setSortArtistsBy( it )
            assertEquals( it, preferenceStore.getSortArtistsBy() )
            assertEquals( it, settingsRepository.sortArtistsBy.value )
        }
    }

    @Test
    fun testSortArtistsInReverseIsCorrectlySet() = runTest {
        assertEquals(
            SettingsDefaults.SORT_ARTISTS_IN_REVERSE,
            settingsRepository.sortArtistsInReverse.value
        )
        listOf( true, false ).forEach {
            settingsRepository.setSortArtistsInReverseTo( it )
            assertEquals( it, preferenceStore.getSortArtistsInReverse() )
            assertEquals( it, settingsRepository.sortArtistsInReverse.value )
        }

    }

    @Test
    fun testSortGenresByValueIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.sortGenresBy,
            settingsRepository.sortGenresBy.value
        )
        SortGenresBy.entries.forEach {
            settingsRepository.setSortGenresBy( it )
            assertEquals( it, preferenceStore.getSortGenresBy() )
            assertEquals( it, settingsRepository.sortGenresBy.value )
        }
    }

    @Test
    fun testSortGenresInReverseIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.SORT_GENRES_IN_REVERSE,
            settingsRepository.sortGenresInReverse.value
        )
        listOf( true, false ).forEach {
            settingsRepository.setSortGenresInReverse( it )
            assertEquals( it, preferenceStore.getSortGenresInReverse() )
            assertEquals( it, settingsRepository.sortGenresInReverse.value )
        }
    }

    @Test
    fun testSortPlaylistsByValueIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.sortPlaylistsBy,
            settingsRepository.sortPlaylistsBy.value
        )
        SortPlaylistsBy.entries.forEach {
            settingsRepository.setSortPlaylistsBy( it )
            assertEquals( it, preferenceStore.getSortPlaylistsBy() )
            assertEquals( it, settingsRepository.sortPlaylistsBy.value )
        }
    }

    @Test
    fun testSortPlaylistsInReverseIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.SORT_PLAYLISTS_IN_REVERSE,
            settingsRepository.sortPlaylistsInReverse.value
        )
        listOf( true, false ).forEach {
            settingsRepository.setSortPlaylistsInReverse( it )
            assertEquals( it, preferenceStore.getSortPlaylistsInReverse() )
            assertEquals( it, settingsRepository.sortPlaylistsInReverse.value )
        }
    }

    @Test
    fun testSortAlbumsByValueIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.sortAlbumsBy,
            settingsRepository.sortAlbumsBy.value
        )
        SortAlbumsBy.entries.forEach {
            settingsRepository.setSortAlbumsBy( it )
            assertEquals( it, preferenceStore.getSortAlbumsBy() )
            assertEquals( it, settingsRepository.sortAlbumsBy.value )
        }
    }

    @Test
    fun testSortAlbumsInReverseIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.SORT_ALBUMS_IN_REVERSE,
            settingsRepository.sortAlbumsInReverse.value
        )
        listOf( true, false ).forEach {
            settingsRepository.setSortAlbumsInReverse( it )
            assertEquals( it, preferenceStore.getSortAlbumsInReverse() )
            assertEquals( it, settingsRepository.sortAlbumsInReverse.value )
        }
    }

    @Test
    fun testSortPathsByIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.sortPathsBy,
            settingsRepository.sortPathsBy.value
        )
        SortPathsBy.entries.forEach {
            settingsRepository.setSortPathsBy( it )
            assertEquals( it, preferenceStore.getSortPathsBy() )
            assertEquals( it, settingsRepository.sortPathsBy.value )
        }
    }

    @Test
    fun testSortPathsInReverseIsSetCorrectly() = runTest {
        assertEquals(
            SettingsDefaults.SORT_PATHS_IN_REVERSE,
            settingsRepository.sortPathsInReverse.value
        )
        listOf( true, false ).forEach {
            settingsRepository.setSortPathsInReverse( it )
            assertEquals( it, preferenceStore.getSortPathsInReverse() )
            assertEquals( it, settingsRepository.sortPathsInReverse.value )
        }
    }

    @Test
    fun testCurrentlyPlayingSongIdIsSavedCorrectly() = runTest {
        assertTrue( settingsRepository.currentlyPlayingSongId.value.isEmpty() )
        testSongs.map { it.id }.forEach { id ->
            settingsRepository.saveCurrentlyPlayingSongId( id )
            assertEquals( id, preferenceStore.getCurrentlyPlayingSongId() )
            assertEquals( id, settingsRepository.currentlyPlayingSongId.value )
        }
    }

}