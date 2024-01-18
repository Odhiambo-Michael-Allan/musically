package com.odesa.musically.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository ) : ViewModel() {

    private val _language = settingsRepository.language

    private val _uiState = MutableStateFlow( SettingsScreenUiState( language = _language.value ) )
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _language.collect {
                _uiState.value = uiState.value.copy(
                    language = it
                )
            }
        }
    }

    fun setLanguage( localeCode: String ) {
        settingsRepository.setLanguage( localeCode )
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
//    val font: Font
)