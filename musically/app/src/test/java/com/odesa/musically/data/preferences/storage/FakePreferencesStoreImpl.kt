package com.odesa.musically.data.preferences.storage

import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.data.storage.preferences.NowPlayingControlsLayout
import com.odesa.musically.data.storage.preferences.NowPlayingLyricsLayout
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.ui.theme.ThemeMode

class FakePreferencesStoreImpl : PreferenceStore {

    private var language: String? = null
    private var fontName: String? = null
    private var fontScale: Float? = null
    private var themeMode: ThemeMode? = null
    private var useMaterialYou: Boolean? = null
    private var primaryColorName: String? = null
    private var homeTabs: Set<HomeTab>? = null
    private var forYouContents: Set<ForYou>? = null
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
    private var nowPlayingControlsLayout: NowPlayingControlsLayout? = null
    private var nowPlayingLyricsLayout: NowPlayingLyricsLayout? = null
    private var showNowPlayingAudioInformation: Boolean? = null
    private var showNowPlayingSeekControls: Boolean? = null
    private var sortSongsBy: SortSongsBy? = null
    private var sortSongsInReverse: Boolean? = null

    override fun setLanguage( localeCode: String ) {
        language = localeCode
    }

    override fun getLanguage(): String {
        return language ?: SettingsDefaults.language.locale
    }

    override fun setFontName( fontName: String ) {
        this.fontName = fontName
    }

    override fun getFontName() = this.fontName ?: SettingsDefaults.font.name
    override fun setFontScale( fontScale: Float ) {
        this.fontScale = fontScale
    }

    override fun getFontScale() = fontScale ?: SettingsDefaults.fontScale

    override fun setThemeMode( themeMode: ThemeMode ) {
        this.themeMode = themeMode
    }

    override fun getThemeMode() = themeMode ?: SettingsDefaults.themeMode
    override fun getUseMaterialYou() = useMaterialYou ?: true

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        this.useMaterialYou = useMaterialYou
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        this.primaryColorName = primaryColorName
    }

    override fun getPrimaryColorName() = primaryColorName ?: SettingsDefaults.primaryColorName
    override fun getHomeTabs() = homeTabs ?: SettingsDefaults.homeTabs

    override fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        this.homeTabs = homeTabs
    }

    override fun getForYouContents() = forYouContents ?: SettingsDefaults.forYouContents

    override fun setForYouContents( forYouContents: Set<ForYou> ) {
        this.forYouContents = forYouContents
    }

    override fun getHomePageBottomBarLabelVisibility() = homePageBottomBarLabelVisibility
        ?: SettingsDefaults.homePageBottomBarLabelVisibility

    override fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility) {
        homePageBottomBarLabelVisibility = value
    }

    override fun getFadePlayback() = fadePlayback ?: SettingsDefaults.fadePlayback

    override fun setFadePlayback( fadePlayback: Boolean ) {
        this.fadePlayback = fadePlayback
    }

    override fun getFadePlaybackDuration() = fadePlaybackDuration ?: SettingsDefaults.fadePlaybackDuration

    override fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        this.fadePlaybackDuration = fadePlaybackDuration
    }

    override fun getRequireAudioFocus() = requireAudioFocus ?: SettingsDefaults.requireAudioFocus

    override fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        this.requireAudioFocus = requireAudioFocus
    }

    override fun getIgnoreAudioFocusLoss() = ignoreAudioFocusLoss
        ?: SettingsDefaults.ignoreAudioFocusLoss

    override fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        this.ignoreAudioFocusLoss = ignoreAudioFocusLoss
    }

    override fun getPlayOnHeadphonesConnect() = playOnHeadphonesConnect
        ?: SettingsDefaults.playOnHeadphonesConnect

    override fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        this.playOnHeadphonesConnect = playOnHeadphonesConnect
    }

    override fun getPauseOnHeadphonesDisconnect() = pauseOnHeadphonesDisconnect
        ?: SettingsDefaults.pauseOnHeadphonesDisconnect

    override fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        this.pauseOnHeadphonesDisconnect = pauseOnHeadphonesDisconnect
    }

    override fun getFastRewindDuration() = fastRewindDuration
        ?: SettingsDefaults.fastRewindDuration

    override fun setFastRewindDuration( fastRewindDuration: Int ) {
        this.fastRewindDuration = fastRewindDuration
    }

    override fun getFastForwardDuration() = fastForwardDuration
        ?: SettingsDefaults.fastForwardDuration

    override fun setFastForwardDuration( fastForwardDuration: Int ) {
        this.fastForwardDuration = fastForwardDuration
    }

    override fun getMiniPlayerShowTrackControls() = miniPlayerShowTrackControls
        ?: SettingsDefaults.miniPlayerShowTrackControls

    override fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        miniPlayerShowTrackControls = showTrackControls
    }

    override fun getMiniPlayerShowSeekControls() = miniPlayerShowSeekControls
        ?: SettingsDefaults.miniPlayerShowSeekControls

    override fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        miniPlayerShowSeekControls = showSeekControls
    }

    override fun getMiniPlayerTextMarquee() = miniPlayerTextMarquee
        ?: SettingsDefaults.miniPlayerTextMarquee

    override fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        miniPlayerTextMarquee = textMarquee
    }

    override fun getNowPlayingControlsLayout() = nowPlayingControlsLayout
        ?: SettingsDefaults.nowPlayingControlsLayout

    override fun setNowPlayingControlsLayout( nowPlayingControlsLayout: NowPlayingControlsLayout) {
        this.nowPlayingControlsLayout = nowPlayingControlsLayout
    }

    override fun getNowPlayingLyricsLayout() = nowPlayingLyricsLayout
        ?: SettingsDefaults.nowPlayingLyricsLayout

    override fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout) {
        this.nowPlayingLyricsLayout = nowPlayingLyricsLayout
    }

    override fun getShowNowPlayingAudioInformation() = showNowPlayingAudioInformation
        ?: SettingsDefaults.showNowPlayingAudioInformation

    override fun setShowNowPlayingAudioInformation( showNowPlayingAudioInformation: Boolean ) {
        this.showNowPlayingAudioInformation = showNowPlayingAudioInformation
    }

    override fun getShowNowPlayingSeekControls() = showNowPlayingSeekControls
        ?: SettingsDefaults.showNowPlayingSeekControls

    override fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        this.showNowPlayingSeekControls = showNowPlayingSeekControls
    }

    override fun setSortSongsBy (sortSongsBy: SortSongsBy ) {
        this.sortSongsBy = sortSongsBy
    }

    override fun getSortSongsBy() = sortSongsBy ?: SettingsDefaults.sortSongsBy

    override fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        this.sortSongsInReverse = sortSongsInReverse
    }

    override fun getSortSongsInReverse() = sortSongsInReverse ?: SettingsDefaults.sortSongsInReverse
}