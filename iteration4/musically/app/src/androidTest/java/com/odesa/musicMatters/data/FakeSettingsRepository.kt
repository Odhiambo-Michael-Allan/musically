package com.odesa.musicMatters.data

import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.ui.theme.SupportedFonts
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val _currentLanguage: MutableStateFlow<Language> = MutableStateFlow( English )
    override val language = _currentLanguage.asStateFlow()

    private val _currentFont = MutableStateFlow( SupportedFonts.ProductSans )
    override val font = _currentFont.asStateFlow()

    private val _fontScale = MutableStateFlow( 1.0f )
    override val fontScale = _fontScale.asStateFlow()

    private val _themeMode = MutableStateFlow( ThemeMode.SYSTEM )
    override val themeMode = _themeMode.asStateFlow()

    private val _useMaterialYou = MutableStateFlow( true )
    override val useMaterialYou = _useMaterialYou.asStateFlow()

    private val _primaryColorName = MutableStateFlow( SettingsDefaults.PRIMARY_COLOR_NAME )
    override val primaryColorName = _primaryColorName.asStateFlow()

    private val _homeTabs = MutableStateFlow( SettingsDefaults.homeTabs )
    override val homeTabs = _homeTabs.asStateFlow()

    private val _homePageBottomBarLabelVisibility = MutableStateFlow(
        SettingsDefaults.homePageBottomBarLabelVisibility
    )
    override val homePageBottomBarLabelVisibility = _homePageBottomBarLabelVisibility.asStateFlow()

    private val _fadePlayback = MutableStateFlow( SettingsDefaults.FADE_PLAYBACK )
    override val fadePlayback = _fadePlayback.asStateFlow()

    private val _fadePlaybackDuration = MutableStateFlow( SettingsDefaults.FADE_PLAYBACK_DURATION )
    override val fadePlaybackDuration = _fadePlaybackDuration.asStateFlow()

    private val _requireAudioFocus = MutableStateFlow( SettingsDefaults.REQUIRE_AUDIO_FOCUS )
    override val requireAudioFocus = _requireAudioFocus.asStateFlow()

    private val _ignoreAudioFocusLoss = MutableStateFlow( SettingsDefaults.IGNORE_AUDIO_FOCUS_LOSS )
    override val ignoreAudioFocusLoss = _ignoreAudioFocusLoss.asStateFlow()

    private val _playOnHeadphoneConnect = MutableStateFlow(
        SettingsDefaults.PLAY_ON_HEADPHONES_CONNECT
    )
    override val playOnHeadphonesConnect = _playOnHeadphoneConnect.asStateFlow()

    private val _pauseOnHeadphonesDisconnect = MutableStateFlow(
        SettingsDefaults.PAUSE_ON_HEADPHONES_DISCONNECT
    )
    override val pauseOnHeadphonesDisconnect = _pauseOnHeadphonesDisconnect.asStateFlow()

    private val _fastRewindDuration = MutableStateFlow( SettingsDefaults.FAST_REWIND_DURATION )
    override val fastRewindDuration = _fastRewindDuration.asStateFlow()

    private val _fastForwardDuration = MutableStateFlow( SettingsDefaults.FAST_FORWARD_DURATION )
    override val fastForwardDuration = _fastForwardDuration.asStateFlow()

    private val _miniPlayerShowTrackControls = MutableStateFlow(
        SettingsDefaults.MINI_PLAYER_SHOW_TRACK_CONTROLS
    )
    override val miniPlayerShowTrackControls = _miniPlayerShowTrackControls.asStateFlow()

    private val _miniPlayerShowSeekControls = MutableStateFlow(
        SettingsDefaults.MINI_PLAYERS_SHOW_SEEK_CONTROLS
    )
    override val miniPlayerShowSeekControls = _miniPlayerShowSeekControls.asStateFlow()

    private val _miniPlayerTextMarquee = MutableStateFlow( SettingsDefaults.MINI_PLAYER_TEXT_MARQUEE )
    override val miniPlayerTextMarquee = _miniPlayerTextMarquee.asStateFlow()

    private val _nowPlayingLyricsLayout = MutableStateFlow(
        SettingsDefaults.nowPlayingLyricsLayout
    )
    override val nowPlayingLyricsLayout = _nowPlayingLyricsLayout.asStateFlow()

    private val _showNowPlayingAudioInformation = MutableStateFlow(
        SettingsDefaults.SHOW_NOW_PLAYING_AUDIO_INFORMATION
    )
    override val showNowPlayingAudioInformation = _showNowPlayingAudioInformation.asStateFlow()

    private val _showNowPlayingSeekControls = MutableStateFlow(
        SettingsDefaults.SHOW_NOW_PLAYING_SEEK_CONTROLS
    )
    override val showNowPlayingSeekControls = _showNowPlayingSeekControls.asStateFlow()
    override val currentPlayingSpeed: StateFlow<Float>
        get() = TODO("Not yet implemented")
    override val currentPlayingPitch: StateFlow<Float>
        get() = TODO("Not yet implemented")
    override val currentLoopMode: StateFlow<LoopMode>
        get() = TODO("Not yet implemented")
    override val shuffle: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val showLyrics: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val controlsLayoutIsDefault: StateFlow<Boolean>
        get() = TODO("Not yet implemented")

    private val _currentlyDisabledTreePaths = MutableStateFlow( emptyList<String>() )
    override val currentlyDisabledTreePaths = _currentlyDisabledTreePaths.asStateFlow()


    override suspend fun setLanguage( localeCode: String ) {
        _currentLanguage.value = resolveLanguage( localeCode )
    }

    private fun resolveLanguage( language: String ) = when( language ) {
        Belarusian.locale -> Belarusian
        Chinese.locale -> Chinese
        French.locale -> French
        German.locale -> German
        Spanish.locale -> Spanish
        else -> English
    }

    override suspend fun setFont( fontName: String ) {
        _currentFont.value = resolveFont( fontName )
    }

    private fun resolveFont( fontName: String ) = when( fontName ) {
        SupportedFonts.DMSans.name -> SupportedFonts.DMSans
        SupportedFonts.Inter.name -> SupportedFonts.Inter
        SupportedFonts.Poppins.name -> SupportedFonts.Poppins
        SupportedFonts.Roboto.name -> SupportedFonts.Roboto
        else -> SupportedFonts.ProductSans
    }

    override suspend fun setFontScale( fontScale: Float ) {
        _fontScale.value = fontScale
    }

    override suspend fun setThemeMode( themeMode: ThemeMode ) {
        _themeMode.value = themeMode
    }

    override suspend fun setUseMaterialYou( useMaterialYou: Boolean ) {
        _useMaterialYou.value = useMaterialYou
    }

    override suspend fun setPrimaryColorName( primaryColorName: String ) {
        _primaryColorName.value = primaryColorName
    }

    override suspend fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        _homeTabs.value = homeTabs
    }

    override suspend fun setHomePageBottomBarLabelVisibility(
        value: HomePageBottomBarLabelVisibility
    ) {
        _homePageBottomBarLabelVisibility.value = value
    }

    override suspend fun setFadePlayback( fadePlayback: Boolean ) {
        _fadePlayback.value = fadePlayback
    }

    override suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        _fadePlaybackDuration.value = fadePlaybackDuration
    }

    override suspend fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        _requireAudioFocus.value = requireAudioFocus
    }

    override suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        _ignoreAudioFocusLoss.value = ignoreAudioFocusLoss
    }

    override suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        _playOnHeadphoneConnect.value = playOnHeadphonesConnect
    }

    override suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        _pauseOnHeadphonesDisconnect.value = pauseOnHeadphonesDisconnect
    }

    override suspend fun setFastRewindDuration( fastRewindDuration: Int ) {
        _fastRewindDuration.value = fastRewindDuration
    }

    override suspend  fun setFastForwardDuration( fastForwardDuration: Int ) {
        _fastForwardDuration.value = fastForwardDuration
    }

    override suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        _miniPlayerShowTrackControls.value = showTrackControls
    }

    override suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        _miniPlayerShowSeekControls.value = showSeekControls
    }

    override suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        _miniPlayerTextMarquee.value = textMarquee
    }

    override suspend fun setNowPlayingLyricsLayout(
        nowPlayingLyricsLayout: NowPlayingLyricsLayout
    ) {
        _nowPlayingLyricsLayout.value = nowPlayingLyricsLayout
    }

    override suspend fun setShowNowPlayingAudioInformation(
        showNowPlayingAudioInformation: Boolean
    ) {
        _showNowPlayingAudioInformation.value = showNowPlayingAudioInformation
    }

    override suspend fun setShowNowPlayingSeekControls(
        showNowPlayingSeekControls: Boolean
    ) {
        _showNowPlayingSeekControls.value = showNowPlayingSeekControls
    }

    override suspend fun setCurrentPlayingSpeed(currentPlayingSpeed: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentPlayingPitch(currentPlayingPitch: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentLoopMode(currentLoopMode: LoopMode) {
        TODO("Not yet implemented")
    }

    override suspend fun setShuffle(shuffle: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setShowLyrics(showLyrics: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setControlsLayoutIsDefault(controlsLayoutIsDefault: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentlyDisabledTreePaths(paths: List<String>) {
        TODO("Not yet implemented")
    }
}