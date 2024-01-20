package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.fontScale.scalingPresets
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ) : ViewModel() {

    private val _language = settingsRepository.language
    private val _font = settingsRepository.font
    private val _fontScale = settingsRepository.fontScale
    private val _themeMode = settingsRepository.themeMode
    private val _useMaterialYou = settingsRepository.useMaterialYou

    private val _uiState = MutableStateFlow(
        SettingsScreenUiState(
            language = _language.value,
            font = _font.value,
            fontScale = _fontScale.value,
            themeMode = _themeMode.value,
            useMaterialYou = _useMaterialYou.value
        )
    )
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch { observeLanguage() }
        viewModelScope.launch { observeFont() }
        viewModelScope.launch { observeFontScale() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeUseMaterialYou() }
    }

    private suspend fun observeLanguage() {
        _language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeFont() {
        _font.collect {
            _uiState.value = _uiState.value.copy(
                font = it
            )
        }
    }

    private suspend fun observeFontScale() {
        _fontScale.collect {
            _uiState.value = _uiState.value.copy(
                fontScale = it
            )
        }
    }

    private suspend fun observeThemeMode() {
        _themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeUseMaterialYou() {
        _useMaterialYou.collect {
            _uiState.value = _uiState.value.copy(
                useMaterialYou = it
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
    val useMaterialYou: Boolean
)