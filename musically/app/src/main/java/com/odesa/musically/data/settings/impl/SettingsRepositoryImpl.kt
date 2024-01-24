package com.odesa.musically.data.settings.impl

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.NowPlayingControlsLayout
import com.odesa.musically.data.preferences.storage.NowPlayingLyricsLayout
import com.odesa.musically.data.preferences.storage.PreferenceStore
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryImpl( private val preferenceStore: PreferenceStore ) : SettingsRepository {

    private val _language = MutableStateFlow( getLanguage( preferenceStore.getLanguage() ) )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( getFont( preferenceStore.getFontName() ) )
    override val font = _font.asStateFlow()

    override val textDirection: StateFlow<TextDirection>
        get() = TODO("Not yet implemented")

    private val _fontScale = MutableStateFlow( preferenceStore.getFontScale() )
    override val fontScale = _fontScale.asStateFlow()

    private val _themeMode = MutableStateFlow( preferenceStore.getThemeMode() )
    override val themeMode = _themeMode.asStateFlow()

    private val _useMaterialYou = MutableStateFlow( preferenceStore.getUseMaterialYou() )
    override val useMaterialYou = _useMaterialYou.asStateFlow()

    private val _primaryColorName = MutableStateFlow( preferenceStore.getPrimaryColorName() )
    override val primaryColorName = _primaryColorName.asStateFlow()

    private val _homeTabs = MutableStateFlow( preferenceStore.getHomeTabs() )
    override val homeTabs = _homeTabs.asStateFlow()

    private val _forYouContents = MutableStateFlow( preferenceStore.getForYouContents() )
    override val forYouContents = _forYouContents.asStateFlow()

    private val _homePageBottomBarLabelVisibility = MutableStateFlow( preferenceStore.getHomePageBottomBarLabelVisibility() )
    override val homePageBottomBarLabelVisibility = _homePageBottomBarLabelVisibility.asStateFlow()

    private val _fadePlayback = MutableStateFlow( preferenceStore.getFadePlayback() )
    override val fadePlayback = _fadePlayback.asStateFlow()

    private val _fadePlaybackDuration = MutableStateFlow( preferenceStore.getFadePlaybackDuration() )
    override val fadePlaybackDuration = _fadePlaybackDuration.asStateFlow()

    private val _requireAudioFocus = MutableStateFlow( preferenceStore.getRequireAudioFocus() )
    override val requireAudioFocus = _requireAudioFocus.asStateFlow()

    private val _ignoreAudioFocusLoss = MutableStateFlow( preferenceStore.getIgnoreAudioFocusLoss() )
    override val ignoreAudioFocusLoss = _ignoreAudioFocusLoss.asStateFlow()

    private val _playOnHeadphonesConnect = MutableStateFlow( preferenceStore.getPlayOnHeadphonesConnect() )
    override val playOnHeadphonesConnect = _playOnHeadphonesConnect.asStateFlow()

    private val _pauseOnHeadphonesDisconnect = MutableStateFlow( preferenceStore.getPauseOnHeadphonesDisconnect() )
    override val pauseOnHeadphonesDisconnect = _pauseOnHeadphonesDisconnect.asStateFlow()

    private val _fastRewindDuration = MutableStateFlow( preferenceStore.getFastRewindDuration() )
    override val fastRewindDuration = _fastRewindDuration.asStateFlow()

    private val _fastForwardDuration = MutableStateFlow( preferenceStore.getFastForwardDuration() )
    override val fastForwardDuration = _fastForwardDuration.asStateFlow()

    private val _miniPlayerShowTrackControls = MutableStateFlow( preferenceStore.getMiniPlayerShowTrackControls() )
    override val miniPlayerShowTrackControls = _miniPlayerShowTrackControls.asStateFlow()

    private val _miniPlayerShowSeekControls = MutableStateFlow( preferenceStore.getMiniPlayerShowSeekControls() )
    override val miniPlayerShowSeekControls = _miniPlayerShowSeekControls.asStateFlow()

    private val _miniPlayerTextMarquee = MutableStateFlow( preferenceStore.getMiniPlayerTextMarquee() )
    override val miniPlayerTextMarquee = _miniPlayerTextMarquee.asStateFlow()

    private val _nowPlayingControlsLayout = MutableStateFlow( preferenceStore.getNowPlayingControlsLayout() )
    override val nowPlayingControlsLayout = _nowPlayingControlsLayout.asStateFlow()

    private val _nowPlayingLyricsLayout = MutableStateFlow( preferenceStore.getNowPlayingLyricsLayout() )
    override val nowPlayingLyricsLayout = _nowPlayingLyricsLayout.asStateFlow()

    private val _showNowPlayingAudioInformation = MutableStateFlow( preferenceStore.getShowNowPlayingAudioInformation() )
    override val showNowPlayingAudioInformation = _showNowPlayingAudioInformation.asStateFlow()

    private val _showNowPlayingSeekControls = MutableStateFlow( preferenceStore.getShowNowPlayingSeekControls() )
    override val showNowPlayingSeekControls = _showNowPlayingSeekControls.asStateFlow()


    private fun getLanguage( localeCode: String ) : Language {
        return when ( localeCode ) {
            Belarusian.locale -> Belarusian
            Chinese.locale -> Chinese
            French.locale -> French
            German.locale -> German
            Spanish.locale -> Spanish
            else -> English
        }
    }

    override suspend fun setLanguage( localeCode: String ) {
        preferenceStore.setLanguage( localeCode )
        _language.update {
            getLanguage( localeCode )
        }
    }

    override suspend fun setFont( fontName: String ) {
        preferenceStore.setFontName( fontName )
        _font.update {
            getFont( fontName )
        }
    }

    private fun getFont( fontName: String ): MusicallyFont {
        return when( fontName ) {
            SupportedFonts.DMSans.name -> SupportedFonts.DMSans
            SupportedFonts.Inter.name -> SupportedFonts.Inter
            SupportedFonts.Poppins.name -> SupportedFonts.Poppins
            SupportedFonts.Roboto.name -> SupportedFonts.Roboto
            else -> SupportedFonts.ProductSans
        }
    }

    override suspend fun setFontScale( fontScale: Float ) {
        preferenceStore.setFontScale( fontScale )
        _fontScale.update {
            fontScale
        }
    }


    override suspend fun setThemeMode( themeMode: ThemeMode ) {
        preferenceStore.setThemeMode( themeMode )
        _themeMode.update {
            themeMode
        }
    }

    override suspend fun setUseMaterialYou( useMaterialYou: Boolean ) {
        preferenceStore.setUseMaterialYou( useMaterialYou )
        _useMaterialYou.update {
            useMaterialYou
        }
    }

    override suspend fun setPrimaryColorName( primaryColorName: String ) {
        preferenceStore.setPrimaryColorName( primaryColorName )
        _primaryColorName.update {
            primaryColorName
        }
    }

    override suspend fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        preferenceStore.setHomeTabs( homeTabs )
        _homeTabs.update {
            homeTabs
        }
    }

    override suspend fun setForYouContents( forYouContents: Set<ForYou> ) {
        preferenceStore.setForYouContents( forYouContents )
        _forYouContents.update {
            forYouContents
        }
    }

    override suspend fun setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility ) {
        preferenceStore.setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility )
        _homePageBottomBarLabelVisibility.update {
            homePageBottomBarLabelVisibility
        }
    }

    override suspend fun setFadePlayback( fadePlayback: Boolean ) {
        preferenceStore.setFadePlayback( fadePlayback )
        _fadePlayback.update {
            fadePlayback
        }
    }

    override suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        preferenceStore.setFadePlaybackDuration( fadePlaybackDuration )
        _fadePlaybackDuration.update {
            fadePlaybackDuration
        }
    }

    override suspend fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        preferenceStore.setRequireAudioFocus( requireAudioFocus )
        _requireAudioFocus.update {
            requireAudioFocus
        }
    }

    override suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        preferenceStore.setIgnoreAudioFocusLoss( ignoreAudioFocusLoss )
        _ignoreAudioFocusLoss.update {
            ignoreAudioFocusLoss
        }
    }

    override suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        preferenceStore.setPlayOnHeadphonesConnect( playOnHeadphonesConnect )
        _playOnHeadphonesConnect.update {
            playOnHeadphonesConnect
        }
    }

    override suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        preferenceStore.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
        _pauseOnHeadphonesDisconnect.update {
            pauseOnHeadphonesDisconnect
        }
    }

    override suspend fun setFastRewindDuration( fastRewindDuration: Int ) {
        preferenceStore.setFastRewindDuration( fastRewindDuration )
        _fastRewindDuration.update {
            fastRewindDuration
        }
    }

    override suspend fun setFastForwardDuration( fastForwardDuration: Int ) {
        preferenceStore.setFastForwardDuration( fastForwardDuration )
        _fastForwardDuration.update {
            fastForwardDuration
        }
    }

    override suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        preferenceStore.setMiniPlayerShowTrackControls( showTrackControls )
        _miniPlayerShowTrackControls.update {
            showTrackControls
        }
    }

    override suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        preferenceStore.setMiniPlayerShowSeekControls( showSeekControls )
        _miniPlayerShowSeekControls.update {
            showSeekControls
        }
    }

    override suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        preferenceStore.setMiniPlayerTextMarquee( textMarquee )
        _miniPlayerTextMarquee.update {
            textMarquee
        }
    }

    override suspend fun setNowPlayingControlsLayout( nowPlayingControlsLayout: NowPlayingControlsLayout ) {
        preferenceStore.setNowPlayingControlsLayout( nowPlayingControlsLayout )
        _nowPlayingControlsLayout.update {
            nowPlayingControlsLayout
        }
    }

    override suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout ) {
        preferenceStore.setNowPlayingLyricsLayout( nowPlayingLyricsLayout )
        _nowPlayingLyricsLayout.update {
            nowPlayingLyricsLayout
        }
    }

    override suspend fun setShowNowPlayingAudioInformation(showNowPlayingAudioInformation: Boolean ) {
        preferenceStore.setShowNowPlayingAudioInformation( showNowPlayingAudioInformation )
        _showNowPlayingAudioInformation.update {
            showNowPlayingAudioInformation
        }
    }

    override suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        preferenceStore.setShowNowPlayingSeekControls( showNowPlayingSeekControls )
        _showNowPlayingSeekControls.update {
            showNowPlayingSeekControls
        }
    }

}
