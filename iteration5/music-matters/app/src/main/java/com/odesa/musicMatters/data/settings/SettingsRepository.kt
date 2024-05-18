package com.odesa.musicMatters.data.settings

import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.ui.theme.MusicallyFont
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val fontScale: StateFlow<Float>
    val themeMode: StateFlow<ThemeMode>
    val useMaterialYou: StateFlow<Boolean>
    val primaryColorName: StateFlow<String>
    val homeTabs: StateFlow<Set<HomeTab>>
    val homePageBottomBarLabelVisibility: StateFlow<HomePageBottomBarLabelVisibility>
    val fadePlayback: StateFlow<Boolean>
    val fadePlaybackDuration: StateFlow<Float>
    val requireAudioFocus: StateFlow<Boolean>
    val ignoreAudioFocusLoss: StateFlow<Boolean>
    val playOnHeadphonesConnect: StateFlow<Boolean>
    val pauseOnHeadphonesDisconnect: StateFlow<Boolean>
    val fastRewindDuration: StateFlow<Int>
    val fastForwardDuration: StateFlow<Int>
    val miniPlayerShowTrackControls: StateFlow<Boolean>
    val miniPlayerShowSeekControls: StateFlow<Boolean>
    val miniPlayerTextMarquee: StateFlow<Boolean>
    val nowPlayingLyricsLayout: StateFlow<NowPlayingLyricsLayout>
    val showNowPlayingAudioInformation: StateFlow<Boolean>
    val showNowPlayingSeekControls: StateFlow<Boolean>

    val currentPlayingSpeed: StateFlow<Float>
    val currentPlayingPitch: StateFlow<Float>
    val currentLoopMode: StateFlow<LoopMode>
    val shuffle: StateFlow<Boolean>
    val showLyrics: StateFlow<Boolean>
    val controlsLayoutIsDefault: StateFlow<Boolean>

    val currentlyDisabledTreePaths: StateFlow<List<String>>
    val currentSortSongsBy: StateFlow<SortSongsBy>
    val sortSongsInReverse: StateFlow<Boolean>

    suspend fun setLanguage( localeCode: String )
    suspend fun setFont( fontName: String )
    suspend fun setFontScale( fontScale: Float )
    suspend fun setThemeMode( themeMode: ThemeMode )
    suspend fun setUseMaterialYou( useMaterialYou: Boolean )
    suspend fun setPrimaryColorName( primaryColorName: String )
    suspend fun setHomeTabs( homeTabs: Set<HomeTab> )
    suspend fun setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility)
    suspend fun setFadePlayback( fadePlayback: Boolean )
    suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float )
    suspend fun setRequireAudioFocus( requireAudioFocus: Boolean )
    suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean )
    suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean )
    suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean )
    suspend fun setFastRewindDuration( fastRewindDuration: Int )
    suspend fun setFastForwardDuration( fastForwardDuration: Int )
    suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean )
    suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean )
    suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean )
    suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout)
    suspend fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean )
    suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean )
    suspend fun setCurrentPlayingSpeed( currentPlayingSpeed: Float )
    suspend fun setCurrentPlayingPitch( currentPlayingPitch: Float )
    suspend fun setCurrentLoopMode( currentLoopMode: LoopMode)
    suspend fun setShuffle( shuffle: Boolean )
    suspend fun setShowLyrics( showLyrics: Boolean )
    suspend fun setControlsLayoutIsDefault( controlsLayoutIsDefault: Boolean )
    suspend fun setCurrentlyDisabledTreePaths( paths: List<String> )
    suspend fun setCurrentSortSongsByValueTo( newSortSongsBy: SortSongsBy )
    suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean )
}