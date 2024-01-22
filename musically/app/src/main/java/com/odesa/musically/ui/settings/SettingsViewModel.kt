package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.appearance.fontScale.scalingPresets
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsScreenUiState(
            language = settingsRepository.language.value,
            font = settingsRepository.font.value,
            fontScale = settingsRepository.fontScale.value,
            themeMode = settingsRepository.themeMode.value,
            useMaterialYou = settingsRepository.useMaterialYou.value,
            primaryColorName = settingsRepository.primaryColorName.value,
            homeTabs = settingsRepository.homeTabs.value,
            forYouContent = settingsRepository.forYouContent.value,
            homePageBottomBarLabelVisibility = settingsRepository.homePageBottomBarLabelVisibility.value,
            fadePlayback = settingsRepository.fadePlayback.value,
            fadePlaybackDuration = settingsRepository.fadePlaybackDuration.value
        )
    )
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch { observeLanguage() }
        viewModelScope.launch { observeFont() }
        viewModelScope.launch { observeFontScale() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeUseMaterialYou() }
        viewModelScope.launch { observePrimaryColorName() }
        viewModelScope.launch { observeHomeTabs() }
        viewModelScope.launch { observeForYouContent() }
        viewModelScope.launch { observeHomePageBottomBarLabelVisibility() }
        viewModelScope.launch { observeFadePlayback() }
        viewModelScope.launch { observeFadePlaybackDuration() }
    }

    private suspend fun observeLanguage() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeFont() {
        settingsRepository.font.collect {
            _uiState.value = _uiState.value.copy(
                font = it
            )
        }
    }

    private suspend fun observeFontScale() {
        settingsRepository.fontScale.collect {
            _uiState.value = _uiState.value.copy(
                fontScale = it
            )
        }
    }

    private suspend fun observeThemeMode() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeUseMaterialYou() {
        settingsRepository.useMaterialYou.collect {
            _uiState.value = _uiState.value.copy(
                useMaterialYou = it
            )
        }
    }

    private suspend fun observePrimaryColorName() {
        settingsRepository.primaryColorName.collect {
            _uiState.value = _uiState.value.copy(
                primaryColorName = it
            )
        }
    }

    private suspend fun observeHomeTabs() {
        settingsRepository.homeTabs.collect {
            _uiState.value = _uiState.value.copy(
                homeTabs = it
            )
        }
    }

    private suspend fun observeForYouContent() {
        settingsRepository.forYouContent.collect {
            _uiState.value = _uiState.value.copy(
                forYouContent = it
            )
        }
    }

    private suspend fun observeHomePageBottomBarLabelVisibility() {
        settingsRepository.homePageBottomBarLabelVisibility.collect {
            _uiState.value = _uiState.value.copy(
                homePageBottomBarLabelVisibility = it
            )
        }
    }

    private suspend fun observeFadePlayback() {
        settingsRepository.fadePlayback.collect {
            _uiState.value = _uiState.value.copy(
                fadePlayback = it
            )
        }
    }

    private suspend fun observeFadePlaybackDuration() {
        settingsRepository.fadePlaybackDuration.collect {
            _uiState.value = _uiState.value.copy(
                fadePlaybackDuration = it
            )
        }
    }

    fun setLanguage( localeCode: String ) {
        settingsRepository.setLanguage( localeCode )
    }

    fun setFont( fontName: String ) {
        settingsRepository.setFont( fontName )
    }

    fun setFontScale( fontScale: String ) {
        val float = fontScale.toFloatOrNull()
        float?.let {
            if ( scalingPresets.contains( it ) ) settingsRepository.setFontScale( it )
        }
    }

    fun setThemeMode( themeMode: ThemeMode ) {
        settingsRepository.setThemeMode( themeMode )
    }

    fun setUseMaterialYou( useMaterialYou: Boolean ) {
        settingsRepository.setUseMaterialYou( useMaterialYou )
    }

    fun setPrimaryColorName( primaryColorName: String ) {
        settingsRepository.setPrimaryColorName( primaryColorName )
    }

    fun setHomeTabs( homeTabs: Set<HomeTab> ) {
        settingsRepository.setHomeTabs( homeTabs )
    }

    fun setForYouContent( forYouContent: Set<ForYou> ) {
        settingsRepository.setForYouContents( forYouContent )
    }

    fun setHomePageBottomBarLabelVisibility( value: HomePageBottomBarLabelVisibility ) {
        settingsRepository.setHomePageBottomBarLabelVisibility( value )
    }

    fun setFadePlayback( fadePlayback: Boolean ) {
        settingsRepository.setFadePlayback( fadePlayback )
    }

    fun setFadePlaybackDuration( fadePlaybackDuration: Float ) {
        settingsRepository.setFadePlaybackDuration( fadePlaybackDuration )
    }

}

@Suppress( "UNCHECKED_CAST" )
class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SettingsViewModel( settingsRepository ) as T )
}

data class SettingsScreenUiState(
    val language: Language,
    val font: MusicallyFont,
    val fontScale: Float,
    val themeMode: ThemeMode,
    val useMaterialYou: Boolean,
    val primaryColorName: String,
    val homeTabs: Set<HomeTab>,
    val forYouContent: Set<ForYou>,
    val homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility,
    val fadePlayback: Boolean,
    val fadePlaybackDuration: Float
)