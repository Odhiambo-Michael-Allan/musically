package com.odesa.musically.data.preferences.impl

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.PreferenceStore
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

class PreferencesImpl( private val preferenceStore: PreferenceStore ) : Preferences {

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

    override fun setLanguage( localeCode: String ) {
        preferenceStore.setLanguage( localeCode )
        _language.update {
            getLanguage( localeCode )
        }
    }

    override fun setFont( fontName: String ) {
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

    override fun setFontScale( fontScale: Float ) {
        preferenceStore.setFontScale( fontScale )
        _fontScale.update {
            fontScale
        }
    }


    override fun setThemeMode( themeMode: ThemeMode ) {
        preferenceStore.setThemeMode( themeMode )
        _themeMode.update {
            themeMode
        }
    }

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        preferenceStore.setUseMaterialYou( useMaterialYou )
        _useMaterialYou.update {
            useMaterialYou
        }
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        preferenceStore.setPrimaryColorName( primaryColorName )
        _primaryColorName.update {
            primaryColorName
        }
    }

    override fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        preferenceStore.setHomeTabs( homeTabs )
        _homeTabs.update {
            homeTabs
        }
    }

    override fun setForYouContents( forYouContents: Set<ForYou> ) {
        preferenceStore.setForYouContents( forYouContents )
        _forYouContents.update {
            forYouContents
        }
    }

    override fun setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility ) {
        preferenceStore.setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility )
        _homePageBottomBarLabelVisibility.update {
            homePageBottomBarLabelVisibility
        }
    }

    override fun setFadePlayback( fadePlayback: Boolean ) {
        preferenceStore.setFadePlayback( fadePlayback )
        _fadePlayback.update {
            fadePlayback
        }
    }

    override fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        preferenceStore.setFadePlaybackDuration( fadePlaybackDuration )
        _fadePlaybackDuration.update {
            fadePlaybackDuration
        }
    }

    override fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        preferenceStore.setRequireAudioFocus( requireAudioFocus )
        _requireAudioFocus.update {
            requireAudioFocus
        }
    }

    override fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        preferenceStore.setIgnoreAudioFocusLoss( ignoreAudioFocusLoss )
        _ignoreAudioFocusLoss.update {
            ignoreAudioFocusLoss
        }
    }

    override fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        preferenceStore.setPlayOnHeadphonesConnect( playOnHeadphonesConnect )
        _playOnHeadphonesConnect.update {
            playOnHeadphonesConnect
        }
    }

    override fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        preferenceStore.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
        _pauseOnHeadphonesDisconnect.update {
            pauseOnHeadphonesDisconnect
        }
    }

    override fun setFastRewindDuration( fastRewindDuration: Int ) {
        preferenceStore.setFastRewindDuration( fastRewindDuration )
        _fastRewindDuration.update {
            fastRewindDuration
        }
    }

    override fun setFastForwardDuration( fastForwardDuration: Int ) {
        preferenceStore.setFastForwardDuration( fastForwardDuration )
        _fastForwardDuration.update {
            fastForwardDuration
        }
    }

    override fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        preferenceStore.setMiniPlayerShowTrackControls( showTrackControls )
        _miniPlayerShowTrackControls.update {
            showTrackControls
        }
    }

    override fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        preferenceStore.setMiniPlayerShowSeekControls( showSeekControls )
        _miniPlayerShowSeekControls.update {
            showSeekControls
        }
    }

    override fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        preferenceStore.setMiniPlayerTextMarquee( textMarquee )
        _miniPlayerTextMarquee.update {
            textMarquee
        }
    }

}
