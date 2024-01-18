package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ) : ViewModel() {

    private val _language = settingsRepository.language
    private val _font = settingsRepository.font

    private val _uiState = MutableStateFlow(
        SettingsScreenUiState(
            language = _language.value,
            font = _font.value
        )
    )
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch { observeLanguage() }
        viewModelScope.launch { observeFont() }
    }

    private suspend fun observeLanguage() {
        _language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
            println( "Setting language in settings view model to ${it.locale}" )
            println( "Font is: ${_uiState.value.font.fontName}" )
        }
    }

    private suspend fun observeFont() {
        _font.collect {
            _uiState.value = _uiState.value.copy(
                font = it
            )
            println( "Setting font in settings view model to: ${it.fontName}" )
            println( "Language is: ${_uiState.value.language.locale}" )
        }
    }

    fun setLanguage( localeCode: String ) {
        settingsRepository.setLanguage( localeCode )
    }

    fun setFont( fontName: String ) {
        settingsRepository.setFont( fontName )
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
    val language: Translation,
    val font: MusicallyFont
)