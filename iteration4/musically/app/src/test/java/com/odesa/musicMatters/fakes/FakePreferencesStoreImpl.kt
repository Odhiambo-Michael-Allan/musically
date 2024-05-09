package com.odesa.musicMatters.fakes

import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.PreferenceStore
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.ui.theme.ThemeMode

class FakePreferencesStoreImpl : PreferenceStore {

    private var language: String? = null
    private var fontName: String? = null
    private var fontScale: Float? = null
    private var themeMode: ThemeMode? = null
    private var useMaterialYou: Boolean? = null
    private var primaryColorName: String? = null
    private var homeTabs: Set<HomeTab>? = null
    private var homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility? = null
    private var fadePlayback: Boolean? = null
    private var fadePlaybackDuration: Float? = null
    private var requireAudioFocus: Boolean? = null
    private var ignoreAudioFocusLoss: Boolean? = null
    private var playOnHeadphonesConnect: Boolean? = null
    private var pauseOnHeadphonesDisconnect: Boolean? = null
    private var fastRewindDuration: Int? = null
    private var fastForwardDuration: Int? = null
    private var miniPlayerShowTrackControls: Boolean? = null
    private var miniPlayerShowSeekControls: Boolean? = null
    private var miniPlayerTextMarquee: Boolean? = null
    private var nowPlayingLyricsLayout: NowPlayingLyricsLayout? = null
    private var showNowPlayingAudioInformation: Boolean? = null
    private var showNowPlayingSeekControls: Boolean? = null
    private var sortSongsBy: SortSongsBy? = null
    private var sortSongsInReverse: Boolean? = null
    private var currentPlayingSpeed: Float? = null
    private var currentPlayingPitch: Float? = null
    private var pauseOnCurrentSongEnd: Boolean? = null
    private var currentLoopMode: LoopMode? = null
    private var shuffle: Boolean? = null
    private var showLyrics: Boolean? = null
    private var controlsLayoutIsDefault: Boolean? = null
    private var disabledTreePaths = listOf<String>()

    override suspend fun setLanguage( localeCode: String ) {
        language = localeCode
    }

    override fun getLanguage(): String {
        return language ?: SettingsDefaults.language.locale
    }

    override suspend fun setFontName( fontName: String ) {
        this.fontName = fontName
    }

    override fun getFontName() = this.fontName ?: SettingsDefaults.font.name
    override suspend fun setFontScale( fontScale: Float ) {
        this.fontScale = fontScale
    }

    override fun getFontScale() = fontScale ?: SettingsDefaults.fontScale

    override suspend fun setThemeMode( themeMode: ThemeMode ) {
        this.themeMode = themeMode
    }

    override fun getThemeMode() = themeMode ?: SettingsDefaults.themeMode
    override fun getUseMaterialYou() = useMaterialYou ?: true

    override suspend fun setUseMaterialYou( useMaterialYou: Boolean ) {
        this.useMaterialYou = useMaterialYou
    }

    override suspend fun setPrimaryColorName( primaryColorName: String ) {
        this.primaryColorName = primaryColorName
    }

    override fun getPrimaryColorName() = primaryColorName ?: SettingsDefaults.primaryColorName
    override fun getHomeTabs() = homeTabs ?: SettingsDefaults.homeTabs

    override suspend fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        this.homeTabs = homeTabs
    }

    override fun getHomePageBottomBarLabelVisibility() = homePageBottomBarLabelVisibility
        ?: SettingsDefaults.homePageBottomBarLabelVisibility

    override suspend fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility) {
        homePageBottomBarLabelVisibility = value
    }

    override fun getFadePlayback() = fadePlayback ?: SettingsDefaults.FADE_PLAYBACK

    override suspend fun setFadePlayback( fadePlayback: Boolean ) {
        this.fadePlayback = fadePlayback
    }

    override fun getFadePlaybackDuration() = fadePlaybackDuration ?: SettingsDefaults.FADE_PLAYBACK_DURATION

    override suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        this.fadePlaybackDuration = fadePlaybackDuration
    }

    override fun getRequireAudioFocus() = requireAudioFocus ?: SettingsDefaults.REQUIRE_AUDIO_FOCUS

    override suspend fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        this.requireAudioFocus = requireAudioFocus
    }

    override fun getIgnoreAudioFocusLoss() = ignoreAudioFocusLoss
        ?: SettingsDefaults.IGNORE_AUDIO_FOCUS_LOSS

    override suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        this.ignoreAudioFocusLoss = ignoreAudioFocusLoss
    }

    override fun getPlayOnHeadphonesConnect() = playOnHeadphonesConnect
        ?: SettingsDefaults.PLAY_ON_HEADPHONES_CONNECT

    override suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        this.playOnHeadphonesConnect = playOnHeadphonesConnect
    }

    override fun getPauseOnHeadphonesDisconnect() = pauseOnHeadphonesDisconnect
        ?: SettingsDefaults.PAUSE_ON_HEADPHONES_DISCONNECT

    override suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        this.pauseOnHeadphonesDisconnect = pauseOnHeadphonesDisconnect
    }

    override fun getFastRewindDuration() = fastRewindDuration
        ?: SettingsDefaults.FAST_REWIND_DURATION

    override suspend fun setFastRewindDuration( fastRewindDuration: Int ) {
        this.fastRewindDuration = fastRewindDuration
    }

    override fun getFastForwardDuration() = fastForwardDuration
        ?: SettingsDefaults.FAST_FORWARD_DURATION

    override suspend fun setFastForwardDuration( fastForwardDuration: Int ) {
        this.fastForwardDuration = fastForwardDuration
    }

    override fun getMiniPlayerShowTrackControls() = miniPlayerShowTrackControls
        ?: SettingsDefaults.MINI_PLAYER_SHOW_TRACK_CONTROLS

    override suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        miniPlayerShowTrackControls = showTrackControls
    }

    override fun getMiniPlayerShowSeekControls() = miniPlayerShowSeekControls
        ?: SettingsDefaults.MINI_PLAYERS_SHOW_SEEK_CONTROLS

    override suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        miniPlayerShowSeekControls = showSeekControls
    }

    override fun getMiniPlayerTextMarquee() = miniPlayerTextMarquee
        ?: SettingsDefaults.MINI_PLAYER_TEXT_MARQUEE

    override suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        miniPlayerTextMarquee = textMarquee
    }

    override fun getControlsLayoutIsDefault() = controlsLayoutIsDefault
        ?: SettingsDefaults.CONTROLS_LAYOUT_IS_DEFAULT

    override suspend fun setControlsLayoutIsDefault( controlsLayoutIsDefault: Boolean ) {
        this.controlsLayoutIsDefault = controlsLayoutIsDefault
    }

    override fun getNowPlayingLyricsLayout() = nowPlayingLyricsLayout
        ?: SettingsDefaults.nowPlayingLyricsLayout

    override suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout) {
        this.nowPlayingLyricsLayout = nowPlayingLyricsLayout
    }

    override fun getShowNowPlayingAudioInformation() = showNowPlayingAudioInformation
        ?: SettingsDefaults.SHOW_NOW_PLAYING_AUDIO_INFORMATION

    override suspend fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean ) {
        this.showNowPlayingAudioInformation = showNowPlayingAudioInformation
    }

    override fun getShowNowPlayingSeekControls() = showNowPlayingSeekControls
        ?: SettingsDefaults.SHOW_NOW_PLAYING_SEEK_CONTROLS

    override suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        this.showNowPlayingSeekControls = showNowPlayingSeekControls
    }

    override suspend fun setSortSongsBy (sortSongsBy: SortSongsBy) {
        this.sortSongsBy = sortSongsBy
    }

    override fun getSortSongsBy() = sortSongsBy ?: SettingsDefaults.sortSongsBy

    override suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        this.sortSongsInReverse = sortSongsInReverse
    }

    override fun getSortSongsInReverse() = sortSongsInReverse ?: SettingsDefaults.SORT_SONGS_IN_REVERSE

    override suspend fun setCurrentPlayingSpeed( currentPlayingSpeed: Float ) {
        this.currentPlayingSpeed = currentPlayingSpeed
    }

    override fun getCurrentPlayingSpeed() = currentPlayingSpeed
        ?: SettingsDefaults.CURRENT_PLAYING_SPEED

    override suspend fun setCurrentPlayingPitch( currentPlayingPitch: Float ) {
        this.currentPlayingPitch = currentPlayingPitch
    }

    override fun getCurrentPlayingPitch() = currentPlayingPitch
        ?: SettingsDefaults.CURRENT_PLAYING_PITCH

    override suspend fun setCurrentLoopMode( loopMode: LoopMode) {
        this.currentLoopMode = loopMode
    }

    override fun getCurrentLoopMode() = currentLoopMode ?: SettingsDefaults.loopMode

    override suspend fun setShuffle( shuffle: Boolean ) {
        this.shuffle = shuffle
    }

    override fun getShuffle() = shuffle ?: SettingsDefaults.SHUFFLE

    override suspend fun setShowLyrics( showLyrics: Boolean ) {
        this.showLyrics = showLyrics
    }

    override fun getShowLyrics() = showLyrics ?: SettingsDefaults.SHOW_LYRICS

    override suspend fun setCurrentlyDisabledTreePaths(paths: List<String> ) {
        this.disabledTreePaths = paths
    }

    override fun getCurrentlyDisabledTreePaths() = this.disabledTreePaths
}