package com.odesa.musicMatters.data.settings.impl

import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.NowPlayingLyricsLayout
import com.odesa.musicMatters.data.preferences.PreferenceStore
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.ui.theme.MusicallyFont
import com.odesa.musicMatters.ui.theme.SupportedFonts
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl( private val preferenceStore: PreferenceStore) : SettingsRepository {

    private val _language = MutableStateFlow( getLanguage( preferenceStore.getLanguage() ) )
    override val language = _language.asStateFlow()

    private val _font = MutableStateFlow( getFont( preferenceStore.getFontName() ) )
    override val font = _font.asStateFlow()

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

    private val _nowPlayingLyricsLayout = MutableStateFlow( preferenceStore.getNowPlayingLyricsLayout() )
    override val nowPlayingLyricsLayout = _nowPlayingLyricsLayout.asStateFlow()

    private val _showNowPlayingAudioInformation = MutableStateFlow( preferenceStore.getShowNowPlayingAudioInformation() )
    override val showNowPlayingAudioInformation = _showNowPlayingAudioInformation.asStateFlow()

    private val _showNowPlayingSeekControls = MutableStateFlow( preferenceStore.getShowNowPlayingSeekControls() )
    override val showNowPlayingSeekControls = _showNowPlayingSeekControls.asStateFlow()

    private val _currentPlayingSpeed = MutableStateFlow( preferenceStore.getCurrentPlayingSpeed() )
    override val currentPlayingSpeed = _currentPlayingSpeed.asStateFlow()

    private val _currentPlayingPitch = MutableStateFlow( preferenceStore.getCurrentPlayingPitch() )
    override val currentPlayingPitch = _currentPlayingPitch.asStateFlow()

    private val _currentLoopMode = MutableStateFlow( preferenceStore.getCurrentLoopMode() )
    override val currentLoopMode = _currentLoopMode.asStateFlow()

    private val _shuffle = MutableStateFlow( preferenceStore.getShuffle() )
    override val shuffle = _shuffle.asStateFlow()

    private val _showLyrics = MutableStateFlow( preferenceStore.getShowLyrics() )
    override val showLyrics = _showLyrics.asStateFlow()

    private val _controlsLayoutIsDefault = MutableStateFlow( preferenceStore.getControlsLayoutIsDefault() )
    override val controlsLayoutIsDefault = _controlsLayoutIsDefault.asStateFlow()

    private val _currentlyDisabledTreePaths = MutableStateFlow( preferenceStore.getCurrentlyDisabledTreePaths() )
    override val currentlyDisabledTreePaths = _currentlyDisabledTreePaths.asStateFlow()

    private val _currentSortSongsBy = MutableStateFlow( preferenceStore.getSortSongsBy() )
    override val currentSortSongsBy = _currentSortSongsBy.asStateFlow()

    private val _sortSongsInReverse = MutableStateFlow( preferenceStore.getSortSongsInReverse() )
    override val sortSongsInReverse = _sortSongsInReverse.asStateFlow()


    override suspend fun setLanguage( localeCode: String ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setLanguage( localeCode )
            _language.update {
                getLanguage( localeCode )
            }
        }
    }

    override suspend fun setFont( fontName: String ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setFontName( fontName )
            _font.update {
                getFont( fontName )
            }
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
        withContext( Dispatchers.IO ) {
            preferenceStore.setFontScale( fontScale )
            _fontScale.update {
                fontScale
            }
        }
    }


    override suspend fun setThemeMode( themeMode: ThemeMode ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setThemeMode( themeMode )
            _themeMode.update {
                themeMode
            }
        }
    }

    override suspend fun setUseMaterialYou( useMaterialYou: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setUseMaterialYou( useMaterialYou )
            _useMaterialYou.update {
                useMaterialYou
            }
        }
    }

    override suspend fun setPrimaryColorName( primaryColorName: String ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setPrimaryColorName( primaryColorName )
            _primaryColorName.update {
                primaryColorName
            }
        }
    }

    override suspend fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setHomeTabs( homeTabs )
            _homeTabs.update {
                homeTabs
            }
        }
    }

    override suspend fun setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setHomePageBottomBarLabelVisibility( homePageBottomBarLabelVisibility )
            _homePageBottomBarLabelVisibility.update {
                homePageBottomBarLabelVisibility
            }
        }
    }

    override suspend fun setFadePlayback( fadePlayback: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setFadePlayback( fadePlayback )
            _fadePlayback.update {
                fadePlayback
            }
        }
    }

    override suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setFadePlaybackDuration( fadePlaybackDuration )
            _fadePlaybackDuration.update {
                fadePlaybackDuration
            }
        }
    }

    override suspend fun setRequireAudioFocus( requireAudioFocus: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setRequireAudioFocus( requireAudioFocus )
            _requireAudioFocus.update {
                requireAudioFocus
            }
        }
    }

    override suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setIgnoreAudioFocusLoss( ignoreAudioFocusLoss )
            _ignoreAudioFocusLoss.update {
                ignoreAudioFocusLoss
            }
        }
    }

    override suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setPlayOnHeadphonesConnect( playOnHeadphonesConnect )
            _playOnHeadphonesConnect.update {
                playOnHeadphonesConnect
            }
        }
    }

    override suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect )
            _pauseOnHeadphonesDisconnect.update {
                pauseOnHeadphonesDisconnect
            }
        }
    }

    override suspend fun setFastRewindDuration( fastRewindDuration: Int ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setFastRewindDuration( fastRewindDuration )
            _fastRewindDuration.update {
                fastRewindDuration
            }
        }
    }

    override suspend fun setFastForwardDuration( fastForwardDuration: Int ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setFastForwardDuration( fastForwardDuration )
            _fastForwardDuration.update {
                fastForwardDuration
            }
        }
    }

    override suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setMiniPlayerShowTrackControls( showTrackControls )
            _miniPlayerShowTrackControls.update {
                showTrackControls
            }
        }
    }

    override suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setMiniPlayerShowSeekControls( showSeekControls )
            _miniPlayerShowSeekControls.update {
                showSeekControls
            }
        }
    }

    override suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setMiniPlayerTextMarquee( textMarquee )
            _miniPlayerTextMarquee.update {
                textMarquee
            }
        }
    }

    override suspend fun setNowPlayingLyricsLayout( nowPlayingLyricsLayout: NowPlayingLyricsLayout) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setNowPlayingLyricsLayout( nowPlayingLyricsLayout )
            _nowPlayingLyricsLayout.update {
                nowPlayingLyricsLayout
            }
        }
    }

    override suspend fun setShowNowPlayingAudioInformation(showNowPlayingAudioInformation: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setShowNowPlayingAudioInformation( showNowPlayingAudioInformation )
            _showNowPlayingAudioInformation.update {
                showNowPlayingAudioInformation
            }
        }
    }

    override suspend fun setShowNowPlayingSeekControls( showNowPlayingSeekControls: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setShowNowPlayingSeekControls( showNowPlayingSeekControls )
            _showNowPlayingSeekControls.update {
                showNowPlayingSeekControls
            }
        }
    }

    override suspend fun setCurrentPlayingSpeed( currentPlayingSpeed: Float ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setCurrentPlayingSpeed( currentPlayingSpeed )
            _currentPlayingSpeed.update {
                currentPlayingSpeed
            }
        }
    }

    override suspend fun setCurrentPlayingPitch( currentPlayingPitch: Float ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setCurrentPlayingPitch( currentPlayingPitch )
            _currentPlayingPitch.update {
                currentPlayingPitch
            }
        }
    }

    override suspend fun setCurrentLoopMode( currentLoopMode: LoopMode) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setCurrentLoopMode( currentLoopMode )
            _currentLoopMode.update {
                currentLoopMode
            }
        }
    }

    override suspend fun setShuffle( shuffle: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setShuffle( shuffle )
            _shuffle.update {
                shuffle
            }
        }
    }

    override suspend fun setShowLyrics( showLyrics: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setShowLyrics( showLyrics )
            _showLyrics.update {
                showLyrics
            }
        }
    }

    override suspend fun setControlsLayoutIsDefault( controlsLayoutIsDefault: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setControlsLayoutIsDefault( controlsLayoutIsDefault )
            _controlsLayoutIsDefault.update {
                controlsLayoutIsDefault
            }
        }
    }

    override suspend fun setCurrentlyDisabledTreePaths( paths: List<String> ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setCurrentlyDisabledTreePaths( paths )
            _currentlyDisabledTreePaths.update {
                paths
            }
        }
    }

    override suspend fun setCurrentSortSongsByValueTo( newSortSongsBy: SortSongsBy ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setSortSongsBy( newSortSongsBy )
            _currentSortSongsBy.value = newSortSongsBy
        }
    }

    override suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        withContext( Dispatchers.IO ) {
            preferenceStore.setSortSongsInReverse( sortSongsInReverse )
            _sortSongsInReverse.value = sortSongsInReverse
        }
    }

}

fun getLanguage( localeCode: String ) : Language {
    return when ( localeCode ) {
        Belarusian.locale -> Belarusian
        Chinese.locale -> Chinese
        French.locale -> French
        German.locale -> German
        Spanish.locale -> Spanish
        else -> English
    }
}
