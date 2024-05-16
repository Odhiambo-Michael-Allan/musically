package com.odesa.musicMatters.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistsScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtistsScreenUiState(
            artists = emptyList(),
            isLoadingArtists = true,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeArtists() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingArtists = it
            )
        }
    }

    private suspend fun observeArtists() {
        musicServiceConnection.cachedArtists.collect {
            _uiState.value = _uiState.value.copy(
                artists = it
            )
        }
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeThemeModeChange() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    fun playSongsByArtist( artist: Artist ) {
        viewModelScope.launch {
            val songsByArtist = musicServiceConnection.cachedSongs.value
                .filter { it.artists.contains( artist.name ) }
                .map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = songsByArtist.first(),
                mediaItems = songsByArtist,
                shuffle = settingsRepository.shuffle.value
            )
        }
    }
}

data class ArtistsScreenUiState(
    val artists: List<Artist>,
    val isLoadingArtists: Boolean,
    val language: Language,
    val themeMode: ThemeMode
)


@Suppress( "UNCHECKED_CAST" )
class ArtistsViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( ArtistsScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository
        ) as T )
}