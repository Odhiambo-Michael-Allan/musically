package com.odesa.musicMatters.ui.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Genre
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_GENRES_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
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
        viewModelScope.launch { loadGenres() }
        viewModelScope.launch { observeLanguageChange() }
    }

    private suspend fun loadGenres() {
        val genreList = mutableListOf<Genre>()
        val genreMediaItems = musicServiceConnection.getChildren( MUSIC_MATTERS_GENRES_ROOT )
        val songMediaItems = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
        genreMediaItems.forEach { genreMediaItem ->
            var numberOfTracksInGenre = 0
            songMediaItems.forEach { songMediaItem ->
                if ( songMediaItem.mediaMetadata.genre.toString().lowercase() == genreMediaItem.mediaMetadata.title.toString().lowercase() )
                    numberOfTracksInGenre++
            }
            genreList.add(
                Genre( genreMediaItem.mediaMetadata.title.toString(), numberOfTracksInGenre )
            )
        }

        _uiState.value = _uiState.value.copy(
            genres = genreList,
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
    val genres: List<Genre>,
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