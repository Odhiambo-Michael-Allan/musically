package com.odesa.musically.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicallyAppViewModel( settingsRepository: SettingsRepository ) : ViewModel() {

    private val font = settingsRepository.font

    private val _themeState = MutableStateFlow(
        ThemeState(
            font = font.value
        )
    )
    val uiState = _themeState.asStateFlow()

    init {
        viewModelScope.launch { observeFont() }
    }

    private suspend fun observeFont() {
        font.collect {
            _themeState.value = _themeState.value.copy(
                font = it
            )
        }
    }

}

@Suppress( "UNCHECKED_CAST" )
class MusicallyAppViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( MusicallyAppViewModel( settingsRepository ) as T )
}

data class ThemeState(
    val font: MusicallyFont,
//    val textDirection: String
)