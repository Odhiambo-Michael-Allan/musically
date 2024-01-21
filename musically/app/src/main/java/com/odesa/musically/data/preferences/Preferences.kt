package com.odesa.musically.data.preferences

import androidx.compose.ui.text.style.TextDirection
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface Preferences {
    val language: StateFlow<Language>
    val font: StateFlow<MusicallyFont>
    val textDirection: StateFlow<TextDirection>
    val fontScale: StateFlow<Float>
    val themeMode: StateFlow<ThemeMode>
    val useMaterialYou: StateFlow<Boolean>
    val primaryColorName: StateFlow<String>
    val homeTabs: StateFlow<Set<HomeTab>>
    val forYouContents: StateFlow<Set<ForYou>>

    fun setLanguage( localeCode: String )
    fun setFont( fontName: String )
    fun setFontScale( fontScale: Float )
    fun setThemeMode( themeMode: ThemeMode )
    fun setUseMaterialYou( useMaterialYou: Boolean )
    fun setPrimaryColorName( primaryColorName: String )
    fun setHomeTabs( homeTabs: Set<HomeTab> )
    fun setForYouContents( forYouContents: Set<ForYou> )

}