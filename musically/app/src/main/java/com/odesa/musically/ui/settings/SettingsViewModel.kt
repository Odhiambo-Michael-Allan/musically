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

    private val language = settingsRepository.language
    private val font = settingsRepository.font
    private val fontScale = settingsRepository.fontScale
    private val themeMode = settingsRepository.themeMode
    private val useMaterialYou = settingsRepository.useMaterialYou
    private val primaryColorName = settingsRepository.primaryColorName
    private val homeTabs = settingsRepository.homeTabs
    private val forYouContent = settingsRepository.forYouContent
    private val homePageBottomBarLabelVisibility = settingsRepository.homePageBottomBarLabelVisibility


    private val _uiState = MutableStateFlow(
        SettingsScreenUiState(
            language = language.value,
            font = font.value,
            fontScale = fontScale.value,
            themeMode = themeMode.value,
            useMaterialYou = useMaterialYou.value,
            primaryColorName = primaryColorName.value,
            homeTabs = homeTabs.value,
            forYouContent = forYouContent.value,
            homePageBottomBarLabelVisibility = homePageBottomBarLabelVisibility.value
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
    }

    private suspend fun observeLanguage() {
        language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeFont() {
        font.collect {
            _uiState.value = _uiState.value.copy(
                font = it
            )
        }
    }

    private suspend fun observeFontScale() {
        fontScale.collect {
            _uiState.value = _uiState.value.copy(
                fontScale = it
            )
        }
    }

    private suspend fun observeThemeMode() {
        themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeUseMaterialYou() {
        useMaterialYou.collect {
            _uiState.value = _uiState.value.copy(
                useMaterialYou = it
            )
        }
    }

    private suspend fun observePrimaryColorName() {
        primaryColorName.collect {
            _uiState.value = _uiState.value.copy(
                primaryColorName = it
            )
        }
    }

    private suspend fun observeHomeTabs() {
        homeTabs.collect {
            _uiState.value = _uiState.value.copy(
                homeTabs = it
            )
        }
    }

    private suspend fun observeForYouContent() {
        forYouContent.collect {
            _uiState.value = _uiState.value.copy(
                forYouContent = it
            )
        }
    }

    private suspend fun observeHomePageBottomBarLabelVisibility() {
        homePageBottomBarLabelVisibility.collect {
            _uiState.value = _uiState.value.copy(
                homePageBottomBarLabelVisibility = it
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
    val homePageBottomBarLabelVisibility: HomePageBottomBarLabelVisibility
)