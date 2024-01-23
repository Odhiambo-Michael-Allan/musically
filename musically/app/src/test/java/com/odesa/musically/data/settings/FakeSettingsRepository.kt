package com.odesa.musically.data.settings

import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _primaryColorName = MutableStateFlow( SettingsDefaults.primaryColorName )
    override val primaryColorName = _primaryColorName.asStateFlow()

    private val _homeTabs = MutableStateFlow( SettingsDefaults.homeTabs )
    override val homeTabs = _homeTabs.asStateFlow()

    private val _forYouContents = MutableStateFlow( SettingsDefaults.forYouContents )
    override val forYouContent = _forYouContents.asStateFlow()

    private val _homePageBottomBarLabelVisibility = MutableStateFlow( SettingsDefaults.homePageBottomBarLabelVisibility )
    override val homePageBottomBarLabelVisibility = _homePageBottomBarLabelVisibility.asStateFlow()

    private val _fadePlayback = MutableStateFlow( SettingsDefaults.fadePlayback )
    override val fadePlayback = _fadePlayback.asStateFlow()

    private val _fadePlaybackDuration = MutableStateFlow( SettingsDefaults.fadePlaybackDuration )
    override val fadePlaybackDuration = _fadePlaybackDuration.asStateFlow()

    private val _requireAudioFocus = MutableStateFlow( SettingsDefaults.requireAudioFocus )
    override val requireAudioFocus = _requireAudioFocus.asStateFlow()

    private val _ignoreAudioFocusLoss = MutableStateFlow( SettingsDefaults.ignoreAudioFocusLoss )
    override val ignoreAudioFocusLoss = _ignoreAudioFocusLoss.asStateFlow()

    private val _playOnHeadphoneConnect = MutableStateFlow( SettingsDefaults.playOnHeadphonesConnect )
    override val playOnHeadphonesConnect = _playOnHeadphoneConnect.asStateFlow()

    private val _pauseOnHeadphonesDisconnect = MutableStateFlow( SettingsDefaults.pauseOnHeadphonesDisconnect )
    override val pauseOnHeadphonesDisconnect = _pauseOnHeadphonesDisconnect.asStateFlow()

    private val _fastRewindDuration = MutableStateFlow( SettingsDefaults.fastRewindDuration )
    override val fastRewindDuration = _fastRewindDuration.asStateFlow()

    private val _fastForwardDuration = MutableStateFlow( SettingsDefaults.fastForwardDuration )
    override val fastForwardDuration = _fastForwardDuration.asStateFlow()

    private val _miniPlayerShowTrackControls = MutableStateFlow( SettingsDefaults.miniPlayerShowTrackControls )
    override val miniPlayerShowTrackControls = _miniPlayerShowTrackControls.asStateFlow()

    private val _miniPlayerShowSeekControls = MutableStateFlow( SettingsDefaults.miniPlayerShowSeekControls )
    override val miniPlayerShowSeekControls = _miniPlayerShowSeekControls.asStateFlow()

    private val _miniPlayerTextMarquee = MutableStateFlow( SettingsDefaults.miniPlayerTextMarquee )
    override val miniPlayerTextMarquee = _miniPlayerTextMarquee.asStateFlow()


    override fun setLanguage( localeCode: String ) {
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

    override fun setFont( fontName: String ) {
        _currentFont.value = resolveFont( fontName )
    }

    private fun resolveFont( fontName: String ) = when( fontName ) {
        SupportedFonts.DMSans.name -> SupportedFonts.DMSans
        SupportedFonts.Inter.name -> SupportedFonts.Inter
        SupportedFonts.Poppins.name -> SupportedFonts.Poppins
        SupportedFonts.Roboto.name -> SupportedFonts.Roboto
        else -> SupportedFonts.ProductSans
    }

    override fun setFontScale( fontScale: Float ) {
        _fontScale.value = fontScale
    }

    override fun setThemeMode( themeMode: ThemeMode ) {
        _themeMode.value = themeMode
    }

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        _useMaterialYou.value = useMaterialYou
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        _primaryColorName.value = primaryColorName
    }

    override fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        _homeTabs.value = homeTabs
    }

    override fun setForYouContents( forYouContents: Set<ForYou> ) {
        _forYouContents.value = forYouContents
    }

    override fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility ) {
        _homePageBottomBarLabelVisibility.value = value
    }

    override fun setFadePlayback( fadePlayback: Boolean ) {
        _fadePlayback.value = fadePlayback
    }

    override fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        _fadePlaybackDuration.value = fadePlaybackDuration
    }

    override fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        _requireAudioFocus.value = requireAudioFocus
    }

    override fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        _ignoreAudioFocusLoss.value = ignoreAudioFocusLoss
    }

    override fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        _playOnHeadphoneConnect.value = playOnHeadphonesConnect
    }

    override fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        _pauseOnHeadphonesDisconnect.value = pauseOnHeadphonesDisconnect
    }

    override fun setFastRewindDuration( fastRewindDuration: Int ) {
        _fastRewindDuration.value = fastRewindDuration
    }

    override fun setFastForwardDuration( fastForwardDuration: Int ) {
        _fastForwardDuration.value = fastForwardDuration
    }

    override fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        _miniPlayerShowTrackControls.value = showTrackControls
    }

    override fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        _miniPlayerShowSeekControls.value = showSeekControls
    }

    override fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        _miniPlayerTextMarquee.value = textMarquee
    }
}