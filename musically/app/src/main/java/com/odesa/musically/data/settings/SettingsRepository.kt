package com.odesa.musically.data.settings

import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val fontScale: StateFlow<Float>
    val themeMode: StateFlow<ThemeMode>
    val useMaterialYou: StateFlow<Boolean>
    val primaryColorName: StateFlow<String>
    val homeTabs: StateFlow<Set<HomeTab>>
    val forYouContent: StateFlow<Set<ForYou>>
    val homePageBottomBarLabelVisibility: StateFlow<HomePageBottomBarLabelVisibility>
    val fadePlayback: StateFlow<Boolean>
    val fadePlaybackDuration: StateFlow<Float>
    val requireAudioFocus: StateFlow<Boolean>
    val ignoreAudioFocusLoss: StateFlow<Boolean>
    val playOnHeadphonesConnect: StateFlow<Boolean>
    val pauseOnHeadphonesDisconnect: StateFlow<Boolean>
    val fastRewindDuration: StateFlow<Int>
    val fastForwardDuration: StateFlow<Int>
    val miniPlayerShowTrackControls: StateFlow<Boolean>
    val miniPlayerShowSeekControls: StateFlow<Boolean>
    val miniPlayerTextMarquee: StateFlow<Boolean>

    suspend fun setLanguage( localeCode: String )
    suspend fun setFont( fontName: String )
    suspend fun setFontScale( fontScale: Float )
    suspend fun setThemeMode( themeMode: ThemeMode )
    suspend fun setUseMaterialYou( useMaterialYou: Boolean )
    suspend fun setPrimaryColorName( primaryColorName: String )
    suspend fun setHomeTabs( homeTabs: Set<HomeTab> )
    suspend fun setForYouContents( forYouContents: Set<ForYou> )
    suspend fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility )
    suspend fun setFadePlayback( fadePlayback: Boolean )
    suspend fun setFadePlaybackDuration( fadePlaybackDuration: Float )
    suspend fun setRequireAudioFocus( requireAudioFocus: Boolean )
    suspend fun setIgnoreAudioFocusLoss( ignoreAudioFocusLoss: Boolean )
    suspend fun setPlayOnHeadphonesConnect( playOnHeadphonesConnect: Boolean )
    suspend fun setPauseOnHeadphonesDisconnect( pauseOnHeadphonesDisconnect: Boolean )
    suspend fun setFastRewindDuration( fastRewindDuration: Int )
    suspend fun setFastForwardDuration( fastForwardDuration: Int )
    suspend fun setMiniPlayerShowTrackControls( showTrackControls: Boolean )
    suspend fun setMiniPlayerShowSeekControls( showSeekControls: Boolean )
    suspend fun setMiniPlayerTextMarquee( textMarquee: Boolean )
}