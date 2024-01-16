package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( settingsRepository: SettingsRepository ) : ViewModel() {

    private val _language = settingsRepository.currentLanguage

    private val _uiState = MutableStateFlow( SettingsScreenUiState( language = _language.value ) )
    val uiState: StateFlow<SettingsScreenUiState> = _uiState


    init {
        viewModelScope.launch {
            _language.collect {
                _uiState.value = uiState.value.copy(
                    language = it
                )
            }
        }
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
    val language: Translation
)