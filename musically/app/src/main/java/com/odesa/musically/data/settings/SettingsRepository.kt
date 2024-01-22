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

    fun setLanguage( localeCode: String )
    fun setFont( fontName: String )
    fun setFontScale( fontScale: Float )
    fun setThemeMode( themeMode: ThemeMode )
    fun setUseMaterialYou( useMaterialYou: Boolean )
    fun setPrimaryColorName( primaryColorName: String )
    fun setHomeTabs( homeTabs: Set<HomeTab> )
    fun setForYouContents( forYouContents: Set<ForYou> )
    fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility )
    fun setFadePlayback( fadePlayback: Boolean )
    fun setFadePlaybackDuration( fadePlaybackDuration: Float )
}