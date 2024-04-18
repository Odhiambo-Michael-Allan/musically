package com.odesa.musicMatters.ui.genre

import androidx.lifecycle.ViewModel
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode

class GenreScreenViewModel : ViewModel() {}

data class GenreScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val songs: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: Set<String>,
    val isLoading: Boolean
)

val testGenreScreenUiState = GenreScreenUiState(
    language = SettingsDefaults.language,
    themeMode = SettingsDefaults.themeMode,
    songs = testSongs,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = emptySet(),
    isLoading = false
)