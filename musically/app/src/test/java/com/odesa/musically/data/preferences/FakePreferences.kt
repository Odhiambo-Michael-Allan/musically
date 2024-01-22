package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePreferences : Preferences {

    private val _language = MutableStateFlow( getLanguage() )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( SupportedFonts.ProductSans )
    override val font = _font.asStateFlow()
    override val textDirection: StateFlow<TextDirection>
        get() = TODO("Not yet implemented")

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
    override val forYouContents = _forYouContents.asStateFlow()

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

    private val _playOnHeadphonesConnect = MutableStateFlow( SettingsDefaults.playOnHeadphonesConnect )
    override val playOnHeadphonesConnect = _playOnHeadphonesConnect.asStateFlow()

    override fun setLanguage( localeCode: String ) {
        when ( localeCode ) {
            Belarusian.locale -> _language.value = Belarusian
            Chinese.locale -> _language.value = Chinese
            French.locale -> _language.value = French
            German.locale -> _language.value = German
            Spanish.locale -> _language.value = Spanish
            else -> _language.value = English
        }
    }

    private fun getLanguage() : Language {
        return English
    }

    override fun setFont( fontName: String ) {
        when ( fontName ) {
            SupportedFonts.Inter.name -> _font.value = SupportedFonts.Inter
            SupportedFonts.ProductSans.name -> _font.value = SupportedFonts.ProductSans
            SupportedFonts.DMSans.name -> _font.value = SupportedFonts.DMSans
            SupportedFonts.Roboto.name -> _font.value = SupportedFonts.Roboto
            else -> _font.value = SupportedFonts.Poppins
        }
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

    override fun setHomePageBottomBarLabelVisibility(
        homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility
    ) {
        _homePageBottomBarLabelVisibility.value = homePageBottomBarLabelVisibility
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
        _playOnHeadphonesConnect.value = playOnHeadphonesConnect
    }

}