package com.odesa.musically.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.services.audio.Song
import com.odesa.musically.services.i18n.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongsViewModel(
    private val settingsRepository: SettingsRepository,
    private val songsRepository: SongsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            isLoadingSongs = songsRepository.isLoadingSongs.value,
            sortSongsInReverse = songsRepository.sortSongsInReverse.value,
            sortSongsBy = songsRepository.sortSongsBy.value,
            songs = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeIsLoadingSongsChange() }
        viewModelScope.launch { observeSortSongsInReverse() }
        viewModelScope.launch { observeSortSongsBy() }
        viewModelScope.launch { fetchSongs() }
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeIsLoadingSongsChange() {
        songsRepository.isLoadingSongs.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingSongs = it
            )
        }
    }

    private suspend fun observeSortSongsInReverse() {
        songsRepository.sortSongsInReverse.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsInReverse =  it
            )
        }
    }

    private suspend fun observeSortSongsBy() {
        songsRepository.sortSongsBy.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsBy = it
            )
        }
    }

    private suspend fun fetchSongs() {
        val songs = songsRepository.getSongs()
        _uiState.value = _uiState.value.copy(
            songs = songs
        )
    }

    fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        viewModelScope.launch { songsRepository.setSortSongsInReverse( sortSongsInReverse ) }
    }

    fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
        viewModelScope.launch { songsRepository.setSortSongsBy( sortSongsBy ) }
    }
}

data class SongsScreenUiState(
    val language: Language,
    val isLoadingSongs: Boolean,
    val sortSongsInReverse: Boolean,
    val sortSongsBy: SortSongsBy,
    val songs: List<Song>,
)

@Suppress( "UNCHECKED_CAST" )
class SongsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val songsRepository: SongsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SongsViewModel( settingsRepository, songsRepository ) as T )
}