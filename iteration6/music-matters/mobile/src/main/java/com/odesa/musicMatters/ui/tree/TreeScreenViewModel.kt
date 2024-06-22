package com.odesa.musicMatters.ui.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.SortPathsBy
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.datatesting.tree.testTreeMap
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

class TreeScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    settingsRepository = settingsRepository,
    playlistRepository = playlistRepository
) {

    private val _uiState = MutableStateFlow(
        TreeScreenUiState(
            tree = emptyMap(),
            songsCount = 0,
            isConstructingTree = musicServiceConnection.isInitializing.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlayingMediaItem.value.mediaId,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            favoriteSongIds = playlistRepository.favoritesPlaylist.value.songIds,
            disabledTreePaths = emptyList(),
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeSongs() }
        viewModelScope.launch { observeCurrentlyPlayingSongId() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        viewModelScope.launch { observeFavoriteSongsPlaylist() }
        viewModelScope.launch { observeDisabledDirectoryNames() }
        addOnPlaylistsChangeListener {
            _uiState.value = _uiState.value.copy( playlists = it )
        }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isConstructingTree = it
            )
        }
    }

    private suspend fun observeSongs() {
        musicServiceConnection.cachedSongs.collect {
            val tree = mutableMapOf<String, MutableList<Song>>()
            val songs = musicServiceConnection.cachedSongs.value
            songs.forEach {
                val directoryName = Path( it.path ).directoryName()
                if ( !tree.containsKey( directoryName ) )
                    tree[ directoryName ] = mutableListOf()
                tree[ directoryName ]!!.add( it )
            }
            _uiState.value = _uiState.value.copy(
                tree = tree,
                songsCount = songs.size
            )
        }
    }

    private suspend fun observeCurrentlyPlayingSongId() {
        musicServiceConnection.nowPlayingMediaItem.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongId = it.mediaId
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

    private suspend fun observeFavoriteSongsPlaylist() {
        playlistRepository.favoritesPlaylist.collect {
            _uiState.value = _uiState.value.copy(
                favoriteSongIds = it.songIds
            )
        }
    }

    private suspend fun observeDisabledDirectoryNames() {
        settingsRepository.currentlyDisabledTreePaths.collect {
            _uiState.value = _uiState.value.copy(
                disabledTreePaths = it
            )
        }
    }

    fun togglePath( path: String ) {
        viewModelScope.launch {
            val currentlyDisabledPaths = uiState.value.disabledTreePaths.toMutableList()
            if ( currentlyDisabledPaths.contains( path ) )
                currentlyDisabledPaths.remove( path )
            else
                currentlyDisabledPaths.add( path )
            settingsRepository.setCurrentlyDisabledTreePaths( currentlyDisabledPaths )
            _uiState.value = _uiState.value.copy(
                disabledTreePaths = currentlyDisabledPaths
            )
        }
    }

    fun playSelectedSong(
        selectedSong: Song
    ) {
        val songs = mutableListOf<Song>()
        uiState.value.tree.values.forEach { list ->
            list.forEach { song -> songs.add( song ) }
        }
        playSongs(
            selectedSong = selectedSong,
            songsInPlaylist = songs
        )
    }

}

data class TreeScreenUiState(
    val tree: Map<String, List<Song>>,
    val songsCount: Int,
    val isConstructingTree: Boolean,
    val currentlyPlayingSongId: String,
    val language: Language,
    val themeMode: ThemeMode,
    val favoriteSongIds: List<String>,
    val disabledTreePaths: List<String>,
    val playlists: List<Playlist>
)

val testTreeScreenUiState = TreeScreenUiState(
    tree = testTreeMap,
    songsCount = 150,
    isConstructingTree = false,
    currentlyPlayingSongId = testSongs.first().id,
    language = English,
    favoriteSongIds = emptyList(),
    themeMode = SettingsDefaults.themeMode,
    disabledTreePaths = emptyList(),
    playlists = emptyList()
)

fun Path.directoryName(): String {
    val indexOfSeparator = this.pathString.lastIndexOf( "/" )
    return this.pathString.substring( 0, indexOfSeparator )
}

fun SortPathsBy.sortPathsByLabel( language: Language ) = when ( this ) {
    SortPathsBy.CUSTOM -> language.custom
    SortPathsBy.NAME -> language.name
}

@Suppress( "UNCHECKED_CAST" )
class TreeViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( TreeScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        ) as T )
}