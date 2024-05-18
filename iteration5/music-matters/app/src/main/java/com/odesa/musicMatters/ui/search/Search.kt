package com.odesa.musicMatters.ui.search

import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Genre
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.testGenres
import com.odesa.musicMatters.ui.theme.MusicMattersTheme
import com.odesa.musicMatters.ui.theme.isLight
import com.odesa.musicMatters.utils.subListNonStrict
import java.util.UUID

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    initialSearchFilter: SearchFilter?,
    onArtistClick: ( Artist ) -> Unit,
    onAlbumClick: ( Album ) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onPlaylistClick: (Playlist ) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchScreenContent(
        uiState = uiState,
        initialSearchFilter = initialSearchFilter,
        onGetSong = { viewModel.getSongWithId( it ) },
        onGetAlbum = { viewModel.getAlbumWithName( it ) },
        onGetArtist = { viewModel.getArtistWithName( it ) },
        onGetGenre = { viewModel.getGenreWithName( it ) },
        onGetPlaylist = { viewModel.getPlaylistWithId( it ) },
        onGetPlaylistArtworkUri = { viewModel.getPlaylistArtworkUri( it ) },
        onSearch = { query, filter -> viewModel.search( query, filter ) },
        onSongClick = {
            viewModel.addSongToSearchHistory( it )
            viewModel.playMedia( it.mediaItem )
        },
        onAlbumClick = {
            viewModel.addAlbumToSearchHistory( it )
            onAlbumClick( it )
        },
        onArtistClick = {
            viewModel.addArtistToSearchHistory( it )
            onArtistClick( it )
        },
        onGenreClick = {
            viewModel.addGenreToSearchHistory( it )
            onGenreClick( it )
        },
        onPlaylistClick = {
            viewModel.addPlaylistToSearchHistory( it )
            onPlaylistClick( it )
        },
        onClearSearchHistory = { viewModel.clearSearchHistory() },
        onDeleteSearchHistoryItem = { viewModel.deleteSearchHistoryItem( it ) },
        onNavigateBack = onNavigateBack
    )

}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    initialSearchFilter: SearchFilter?,
    onGetSong: ( String ) -> Song?,
    onGetAlbum: ( String ) -> Album?,
    onGetArtist: ( String ) -> Artist?,
    onGetGenre: ( String ) -> Genre?,
    onGetPlaylist: ( String ) -> Playlist?,
    onGetPlaylistArtworkUri: ( Playlist ) -> Uri?,
    onSearch: ( String, SearchFilter? ) -> Unit,
    onSongClick: ( Song ) -> Unit,
    onArtistClick: ( Artist ) -> Unit,
    onAlbumClick: ( Album ) -> Unit,
    onGenreClick: ( Genre ) -> Unit,
    onPlaylistClick: ( Playlist ) -> Unit,
    onClearSearchHistory: () -> Unit,
    onDeleteSearchHistoryItem: ( SearchHistoryItem ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    var currentSearchQuery by rememberSaveable { mutableStateOf( "" ) }
    var currentSearchFilter by rememberSaveable { mutableStateOf( initialSearchFilter ) }

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark
    
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = currentSearchQuery,
            language = uiState.language,
            currentSearchFilter = currentSearchFilter,
            onSearchQueryChange = {
                currentSearchQuery = it
                onSearch( currentSearchQuery, currentSearchFilter )
            },
            onFilterChange = {
                currentSearchFilter = it
                onSearch( currentSearchQuery, currentSearchFilter )
            },
            onClearSearch = { currentSearchQuery = "" },
            onNavigateBack = onNavigateBack
        )
        if ( currentSearchQuery.isNotEmpty() ) {
            SearchResultsList(
                isSearching = uiState.isSearching,
                currentSearchFilter = currentSearchFilter,
                searchResults = uiState.currentSearchResults,
                language = uiState.language,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                onSongClick = onSongClick,
                onArtistClick = onArtistClick,
                onAlbumClick = onAlbumClick,
                onGenreClick = onGenreClick,
                onPlaylistClick = onPlaylistClick,
                onGetPlaylistArtworkUri = onGetPlaylistArtworkUri,
            )
        }
        else {
            SearchHistoryList(
                searchHistoryItems = uiState.searchHistoryItems,
                isLoadingSearchHistory = uiState.isLoadingSearchHistory,
                language = uiState.language,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                onGetSong = onGetSong,
                onGetAlbum = onGetAlbum,
                onGetArtist = onGetArtist,
                onGetGenre = onGetGenre,
                onGetPlaylist = onGetPlaylist,
                onGetPlaylistArtworkUri = onGetPlaylistArtworkUri,
                onSongClick = onSongClick,
                onAlbumClick = onAlbumClick,
                onArtistClick = onArtistClick,
                onGenreClick = onGenreClick,
                onPlaylistClick = onPlaylistClick,
                onClearSearchHistory = onClearSearchHistory,
                onDeleteSearchHistoryItem = onDeleteSearchHistoryItem
            )
        }
    }
}

@Preview( showSystemUi = true )
@PreviewScreenSizes
@Composable
fun SearchScreenContentPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = true
    ) {
        SearchScreenContent(
            uiState = SearchScreenUiState(
                isLoadingSearchHistory = false,
                language = English,
                isSearching = false,
                currentSearchResults = SearchResults(
                    matchingSongs = testSongs.subListNonStrict( 2 ),
                    matchingAlbums = testAlbums.subListNonStrict( 2 ),
                    matchingGenres = testGenres.subListNonStrict( 2 ),
                    matchingArtists = testArtists.subListNonStrict( 2 ),
                    matchingPlaylists = testPlaylists.subListNonStrict( 2 )
                ),
                themeMode = SettingsDefaults.themeMode,
                searchHistoryItems = testSearchHistoryItems,
                currentlyPlayingSongId = testSongs.first().id
            ),
            initialSearchFilter = null,
            onSearch = { _, _ -> },
            onGetSong = { testSongs.first() },
            onGetAlbum = { testAlbums.first() },
            onGetArtist = { testArtists.first() },
            onGetGenre = { testGenres.first() },
            onGetPlaylist = { testPlaylists.first() },
            onGetPlaylistArtworkUri = { null },
            onGenreClick = {},
            onArtistClick = {},
            onPlaylistClick = {},
            onAlbumClick = {},
            onSongClick = {},
            onNavigateBack = {},
            onClearSearchHistory = {},
            onDeleteSearchHistoryItem = {}
        )
    }
}

internal fun createImageRequest( context: Context, artworkUri: Uri?, @DrawableRes fallbackResourceId: Int ) =
    ImageRequest.Builder( context ).apply{
        data( artworkUri )
        placeholder( fallbackResourceId )
        fallback( fallbackResourceId )
        error( fallbackResourceId )
        crossfade( true )
        build()
    }.build()





val testSearchHistoryItems = listOf(
    SearchHistoryItem(
        id = UUID.randomUUID().toString(),
        category = SearchFilter.SONG
    ),
    SearchHistoryItem(
        id = testAlbums.first().name,
        category = SearchFilter.ALBUM
    ),
    SearchHistoryItem(
        id = testArtists.first().name,
        category = SearchFilter.ARTIST
    ),
    SearchHistoryItem(
        id = testGenres.first().name,
        category = SearchFilter.GENRE
    ),
    SearchHistoryItem(
        id = testPlaylists.first().id,
        category = SearchFilter.PLAYLIST
    )
)
