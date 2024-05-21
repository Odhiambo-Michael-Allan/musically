package com.odesa.musicMatters.ui.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.SortPathsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.Path
import kotlin.io.path.pathString

class TreeScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TreeScreenUiState(
            tree = emptyMap(),
            songsCount = 0,
            isConstructingTree = musicServiceConnection.isInitializing.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
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
        viewModelScope.launch { observePlaylists() }
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
        musicServiceConnection.nowPlaying.collect {
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

    private suspend fun observePlaylists() {
        playlistRepository.playlists.collect {
            _uiState.value = _uiState.value.copy(
                playlists = fetchRequiredPlaylistsFrom( it )
            )
        }
    }

    private fun fetchRequiredPlaylistsFrom( playlists: List<Playlist> ) = playlists.filter {
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
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

    fun saveSongToPlaylist( playlist: Playlist, songs: List<Song> ) {
        viewModelScope.launch {
            songs.forEach {
                playlistRepository.addSongIdToPlaylist( it.id, playlist.id )
            }
        }
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch { playlistRepository.addToFavorites( songId ) }
    }

    fun playMedia(
        mediaItem: MediaItem,
    ) {
        viewModelScope.launch {
            val songs = musicServiceConnection.cachedSongs.value.map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = songs,
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

    fun searchSongsMatching( query: String ) = musicServiceConnection.searchSongsMatching( query )
    fun createPlaylist( title: String, songs: List<Song> ) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    songIds = songs.map { it.id }
                )
            )
        }
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
    val playlists: List<Playlist>,
)

val testPaths = listOf(
    "/storage/emulated/0/Music/Madeon/All My Friends/Madeon - All My Friends.mp3",
    "/storage/emulated/0/Music/Bea Miller/elated!/Bea Miller, Aminé - FEEL SOMETHING DIFFERENT.mp3",
    "/storage/emulated/0/Music/Tove Lo/Queen Of The Clouds/Tove Lo - Talking Body.mp3",
    "/storage/emulated/0/Music/Flume/Skin/Flume, Tove Lo - Say It.mp3",
    "/storage/emulated/0/Music/Sean Paul/I'm Still in Love with You/Sean Paul, Sasha - I'm Still in Love with You (feat. Sasha).mp3",
    "/storage/emulated/0/Music/Sean Paul/Tek Weh Yuh Heart/Sean Paul, Tory Lanez - Tek Weh Yuh Heart.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake, J Balvin, Tyga - Loco Contigo.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake - Magenta Riddim.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake, Eptic - SouthSide.mp3",
    "/storage/emulated/0/Music/Sean Paul/Calling On Me/Sean Paul, Tove Lo - Calling On Me.mp3"
)

val testTreeMap = mapOf<String, List<Song>>(
    testPaths[0] to testSongs.subList( 0, 5 ),
    testPaths[1] to testSongs.subList( 0, 7 ),
    testPaths[2] to testSongs.subList( 0, 19 ),
    testPaths[3] to testSongs.subList( 0, 4 ),
    testPaths[4] to testSongs.subList( 0, 11 ),
    testPaths[5] to testSongs.subList( 0, 3 )
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
    playlists = emptyList(),
)

fun Path.directoryName(): String {
    val indexOfSeparator = this.pathString.lastIndexOf( "/" )
    return this.pathString.substring( 0, indexOfSeparator )
}
fun SortPathsBy.label(language: Language ) = when ( this ) {
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