package com.odesa.musically.data.settings

import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.data.storage.preferences.NowPlayingControlsLayout
import com.odesa.musically.data.storage.preferences.NowPlayingLyricsLayout
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.LoopMode
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val fontScale: StateFlow<Float>
    val themeMode: StateFlow<ThemeMode>
    val useMaterialYou: StateFlow<Boolean>
    val primaryColorName: StateFlow<String>
    val homeTabs: StateFlow<Set<HomeTab>>
    val forYouContents: StateFlow<Set<ForYou>>
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
    val nowPlayingControlsLayout: StateFlow<NowPlayingControlsLayout>
    val nowPlayingLyricsLayout: StateFlow<NowPlayingLyricsLayout>
    val showNowPlayingAudioInformation: StateFlow<Boolean>
    val showNowPlayingSeekControls: StateFlow<Boolean>

    val currentPlayingSpeed: StateFlow<Float>
    val currentPlayingPitch: StateFlow<Float>
    val pauseOnCurrentSongEnd: StateFlow<Boolean>
    val currentLoopMode: StateFlow<LoopMode>
    val shuffle: StateFlow<Boolean>
    val showLyrics: StateFlow<Boolean>
    val controlsLayoutIsDefault: StateFlow<Boolean>

    suspend fun setLanguage( localeCode: String )
    suspend fun setFont( fontName: String )
    suspend fun setFontScale( fontScale: Float )
    suspend fun setThemeMode( themeMode: ThemeMode )
    suspend fun setUseMaterialYou( useMaterialYou: Boolean )
    suspend fun setPrimaryColorName( primaryColorName: String )
    suspend fun setHomeTabs( homeTabs: Set<HomeTab> )
    suspend fun setForYouContents( forYouContents: Set<ForYou> )
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
    suspend fun setNowPlayingControlsLayout( nowPlayingControlsLayout: NowPlayingControlsLayout)
    suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout)
    suspend fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean )
    suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean )
    suspend fun setCurrentPlayingSpeed( currentPlayingSpeed: Float )
    suspend fun setCurrentPlayingPitch( currentPlayingPitch: Float )
    suspend fun setPauseOnCurrentSongEnd( pauseOnCurrentSongEnd: Boolean )
    suspend fun setCurrentLoopMode( currentLoopMode: LoopMode )
    suspend fun setShuffle( shuffle: Boolean )
    suspend fun setShowLyrics( showLyrics: Boolean )
    suspend fun setControlsLayoutIsDefault( controlsLayoutIsDefault: Boolean )

}