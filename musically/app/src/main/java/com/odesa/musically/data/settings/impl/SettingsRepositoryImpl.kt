package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.ui.theme.ThemeMode

class SettingsRepositoryImpl(
    private val preferences: Preferences
) : SettingsRepository {

    override val language = preferences.language
    override val font = preferences.font
    override val fontScale = preferences.fontScale
    override val themeMode = preferences.themeMode
    override val useMaterialYou = preferences.useMaterialYou
    override val primaryColorName = preferences.primaryColorName
    override val homeTabs = preferences.homeTabs
    override val forYouContent = preferences.forYouContents
    override val homePageBottomBarLabelVisibility = preferences.homePageBottomBarLabelVisibility
    override val fadePlayback = preferences.fadePlayback
    override val fadePlaybackDuration = preferences.fadePlaybackDuration
    override val requireAudioFocus = preferences.requireAudioFocus
    override val ignoreAudioFocusLoss = preferences.ignoreAudioFocusLoss
    override val playOnHeadphonesConnect = preferences.playOnHeadphonesConnect
    override val pauseOnHeadphonesDisconnect = preferences.pauseOnHeadphonesDisconnect
    override val fastRewindDuration = preferences.fastRewindDuration
    override val fastForwardDuration = preferences.fastForwardDuration

    override fun setLanguage( localeCode: String ) {
        preferences.setLanguage( localeCode )
    }

    override fun setFont( fontName: String ) {
        preferences.setFont( fontName )
    }

    override fun setFontScale( fontScale: Float ) {
        preferences.setFontScale( fontScale )
    }

    override fun setThemeMode( themeMode: ThemeMode ) {
        preferences.setThemeMode( themeMode )
    }

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        preferences.setUseMaterialYou( useMaterialYou )
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        preferences.setPrimaryColorName( primaryColorName )
    }

    override fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        preferences.setHomeTabs( homeTabs )
    }

    override fun setForYouContents( forYouContents: Set<ForYou> ) {
        preferences.setForYouContents( forYouContents )
    }

    override fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility ) {
        preferences.setHomePageBottomBarLabelVisibility( value )
    }

    override fun setFadePlayback( fadePlayback: Boolean ) {
        preferences.setFadePlayback( fadePlayback )
    }

    override fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        preferences.setFadePlaybackDuration( fadePlaybackDuration )
    }

    override fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        preferences.setRequireAudioFocus( requireAudioFocus )
    }

    override fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        preferences.setIgnoreAudioFocusLoss( ignoreAudioFocusLoss )
    }

    override fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        preferences.setPlayOnHeadphonesConnect( playOnHeadphonesConnect )
    }

    override fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        preferences.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
    }

    override fun setFastRewindDuration( fastRewindDuration: Int ) {
        preferences.setFastRewindDuration( fastRewindDuration )
    }

    override fun setFastForwardDuration( fastForwardDuration: Int ) {
        preferences.setFastForwardDuration( fastForwardDuration )
    }
}