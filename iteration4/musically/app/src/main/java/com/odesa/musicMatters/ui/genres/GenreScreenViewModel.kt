package com.odesa.musicMatters.ui.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_GENRES_ROOT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenreScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GenreScreenUiState(
            genres = emptyList(),
            language = settingsRepository.language.value,
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { fetchGenres() }
        viewModelScope.launch { observeLanguageChange() }
    }

    private suspend fun fetchGenres() {
        val genres = musicServiceConnection.getChildren( MUSIC_MATTERS_GENRES_ROOT )
        _uiState.value = _uiState.value.copy(
            genres = genres,
            isLoading = false
        )
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }
}

data class GenreScreenUiState(
    val genres: List<MediaItem>,
    val language: Language,
    val isLoading: Boolean,
)

@Suppress( "UNCHECKED_CAST" )
class GenreScreenViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( GenreScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository
        ) as T )
}