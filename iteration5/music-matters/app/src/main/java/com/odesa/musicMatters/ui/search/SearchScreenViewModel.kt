package com.odesa.musicMatters.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.data.search.SearchHistoryRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Genre
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import com.odesa.musicMatters.utils.FuzzySearchOption
import com.odesa.musicMatters.utils.FuzzySearcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel (
    val musicServiceConnection: MusicServiceConnection,
    val playlistRepository: PlaylistRepository,
    val settingsRepository: SettingsRepository,
    val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val albumsFuzzySearcher = FuzzySearcher<String>(
        options = listOf(
            FuzzySearchOption( { v -> compareString( getAlbumWithName( v )?.name ?: "" ) }, 3 ),
            FuzzySearchOption( { v -> compareCollection( getAlbumWithName( v )?.artists ?: listOf() ) } )
        )
    )
    private val artistFuzzySearcher = FuzzySearcher<String>(
        options = listOf(
            FuzzySearchOption( { v -> compareString( getArtistWithName( v )?.name ?: "" ) } )
        )
    )
    private val genreFuzzySearcher = FuzzySearcher<String>(
        options = listOf(
            FuzzySearchOption( { v -> compareString( getGenreWithName( v )?.name ?: "" ) } )
        )
    )
    private val playlistFuzzySearcher = FuzzySearcher<String>(
        options = listOf(
            FuzzySearchOption( { v -> compareString( getPlaylistWithId( v )?.title ?: "" ) } )
        )
    )

    private val _uiState = MutableStateFlow(
        SearchScreenUiState(
            currentSearchQuery = "",
            isSearching = false,
            searchHistoryItems = searchHistoryRepository.searchHistory.value,
            language = settingsRepository.language.value,
            currentSearchFilter = null,
            currentSearchResults = emptySearchResults,
            themeMode = settingsRepository.themeMode.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeSearchHistory() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
    }

    private suspend fun observeSearchHistory() {
        searchHistoryRepository.searchHistory.collect {
            _uiState.value = _uiState.value.copy(
                searchHistoryItems = it
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

    fun saveSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        viewModelScope.launch { searchHistoryRepository.saveSearchHistoryItem( searchHistoryItem ) }
    }

    fun deleteSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        viewModelScope.launch { searchHistoryRepository.deleteSearchHistoryItem( searchHistoryItem ) }
    }

    fun getAlbumWithName( name: String ) = musicServiceConnection.cachedAlbums.value.find { it.name == name }
    fun getArtistWithName( name: String ) = musicServiceConnection.cachedArtists.value.find { it.name == name }
    fun getGenreWithName( name: String ) = musicServiceConnection.cachedGenres.value.find { it.name == name }
    fun getPlaylistWithId( id: String ) = playlistRepository.playlists.value.find { it.id == id }

    fun search( searchQuery: String, searchFilter: SearchFilter? ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSearching = true
            )
            val songs = mutableListOf<Song>()
            val albums = mutableListOf<Album>()
            val artists = mutableListOf<Artist>()
            val genreList = mutableListOf<Genre>()
            val playlists = mutableListOf<Playlist>()
            if ( searchQuery.isNotEmpty() ) {
                searchFilter?.let {
                    when ( it ) {
                        SearchFilter.SONG -> {
                            songs.addAll( musicServiceConnection.searchSongsMatching( searchQuery ) )
                        }
                        SearchFilter.ALBUM -> {
                            albumsFuzzySearcher.search(
                                terms = searchQuery,
                                entities = musicServiceConnection.cachedAlbums.value.map { album -> album.name }
                            ).map { fuzzySearchResult -> getAlbumWithName( fuzzySearchResult.entity ) }
                        }
                        SearchFilter.ARTIST -> {
                            artistFuzzySearcher.search(
                                terms = searchQuery,
                                entities = musicServiceConnection.cachedArtists.value.map { artist -> artist.name }
                            ).map { fuzzySearchResult -> getArtistWithName( fuzzySearchResult.entity ) }
                        }
                        SearchFilter.GENRE -> {
                            genreList.addAll(
                                genreFuzzySearcher.search(
                                    terms = searchQuery,
                                    entities = musicServiceConnection.cachedGenres.value.map { genre -> genre.name }
                                ).mapNotNull { fuzzySearchResult -> getGenreWithName( fuzzySearchResult.entity ) }
                            )
                        }
                        SearchFilter.PLAYLIST -> {
                            playlists.addAll(
                                playlistFuzzySearcher.search(
                                    terms = searchQuery,
                                    entities = playlistRepository.playlists.value.map { playlist -> playlist.id }
                                ).mapNotNull { fuzzySearchResult -> getPlaylistWithId( fuzzySearchResult.entity ) }
                            )
                        }
                    }
                } ?: run {
                    songs.addAll( musicServiceConnection.searchSongsMatching( searchQuery ) )
                    albums.addAll(
                        albumsFuzzySearcher.search(
                            terms = searchQuery,
                            entities = musicServiceConnection.cachedAlbums.value.map { it.name }
                        ).mapNotNull { getAlbumWithName( it.entity ) }
                    )
                    artists.addAll(
                        artistFuzzySearcher.search(
                            terms = searchQuery,
                            entities = musicServiceConnection.cachedArtists.value.map { it.name }
                        ).mapNotNull { getArtistWithName( it.entity ) }
                    )
                    val genres = genreFuzzySearcher.search(
                        terms = searchQuery,
                        entities = musicServiceConnection.cachedGenres.value.map { it.name }
                    ).mapNotNull { getGenreWithName( it.entity ) }
                    genreList.addAll( genres )
                    playlists.addAll(
                        playlistFuzzySearcher.search(
                            terms = searchQuery,
                            entities = playlistRepository.playlists.value.map { it.id }
                        ).mapNotNull { getPlaylistWithId( it.entity ) }
                    )
                }
            }
            val searchResults = SearchResults(
                matchingSongs = songs,
                matchingAlbums = albums,
                matchingArtists = artists,
                matchingGenres = genreList,
                matchingPlaylists = playlists
            )
            _uiState.value = _uiState.value.copy(
                currentSearchResults = searchResults,
                isSearching = false
            )
        }
    }

    fun getSongWithId( id: String ) = musicServiceConnection.cachedSongs.value.find { it.id == id }

    fun getPlaylistArtworkUri( playlist: Playlist ) = musicServiceConnection.cachedSongs.value
        .filter { playlist.songIds.contains( it.id ) }.firstOrNull { it.artworkUri != null }
        ?.artworkUri

    fun addSongToSearchHistory( song: Song ) {
        viewModelScope.launch {
            searchHistoryRepository.saveSearchHistoryItem(
                SearchHistoryItem(
                    id = song.id,
                    category = SearchFilter.SONG
                )
            )
        }
    }

    fun addAlbumToSearchHistory( album: Album ) {
        viewModelScope.launch {
            searchHistoryRepository.saveSearchHistoryItem(
                SearchHistoryItem(
                    id = album.name,
                    category = SearchFilter.ALBUM
                )
            )
        }
    }

    fun addArtistToSearchHistory( artist: Artist ) {
         viewModelScope.launch {
             searchHistoryRepository.saveSearchHistoryItem(
                 SearchHistoryItem(
                     id = artist.name,
                     category = SearchFilter.ARTIST
                 )
             )
         }
     }

    fun addGenreToSearchHistory( genre: Genre ) {
        viewModelScope.launch {
            searchHistoryRepository.saveSearchHistoryItem(
                SearchHistoryItem(
                    id = genre.name,
                    category = SearchFilter.GENRE
                )
            )
        }
    }

    fun addPlaylistToSearchHistory( playlist: Playlist ) {
        viewModelScope.launch {
            searchHistoryRepository.saveSearchHistoryItem(
                SearchHistoryItem(
                    id = playlist.id,
                    category = SearchFilter.PLAYLIST
                )
            )
        }
    }

    fun playMedia(
        mediaItem: MediaItem,
    ) {
        viewModelScope.launch {
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = listOf( mediaItem ),
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

}

@Suppress( "UNCHECKED_CAST" )
class SearchScreenViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SearchScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
            searchHistoryRepository = searchHistoryRepository
        ) as T )
}

data class SearchScreenUiState(
    val currentSearchQuery: String,
    val isSearching: Boolean,
    val searchHistoryItems: List<SearchHistoryItem>,
    val language: Language,
    val currentSearchFilter: SearchFilter?,
    val currentSearchResults: SearchResults,
    val themeMode: ThemeMode,
)

enum class SearchFilter {
    SONG,
    ALBUM,
    ARTIST,
    GENRE,
    PLAYLIST,
}

data class SearchResults(
    val matchingSongs: List<Song>,
    val matchingArtists: List<Artist>,
    val matchingAlbums: List<Album>,
    val matchingGenres: List<Genre>,
    val matchingPlaylists: List<Playlist>,
)

val emptySearchResults = SearchResults(
    matchingSongs = emptyList(),
    matchingAlbums = emptyList(),
    matchingArtists = emptyList(),
    matchingGenres = emptyList(),
    matchingPlaylists = emptyList()
)