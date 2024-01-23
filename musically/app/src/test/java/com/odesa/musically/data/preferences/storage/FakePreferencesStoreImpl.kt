package com.odesa.musically.data.preferences.storage

import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
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

    override fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility ) {
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
}