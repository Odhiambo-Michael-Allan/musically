package com.odesa.musicMatters.ui.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.SortAlbumsBy
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.ui.components.AlbumGrid
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun AlbumsScreen(
    viewModel: AlbumsScreenViewModel,
    onAlbumClick: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val albumScreenUiState by viewModel.uiState.collectAsState()

    AlbumsScreenContent(
        uiState = albumScreenUiState,
        onAlbumClick = onAlbumClick,
        onSettingsClicked = onSettingsClicked,
        onPlayAlbum = { viewModel.playAlbum( it ) },
        onNavigateToSearch = onNavigateToSearch,
        onViewArtist = {},
        onPlayNext = {},
        onShufflePlay = {},
        onAddToQueue = {},
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onAddSongsToPlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInAlbum = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
    )
}

@Composable
fun AlbumsScreenContent(
    uiState: AlbumsScreenUiState,
    onAlbumClick: ( String ) -> Unit,
    onSettingsClicked: () -> Unit,
    onPlayAlbum: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: ( Album ) -> Unit,
    onShufflePlay: ( Album ) -> Unit,
    onAddToQueue: ( Album ) -> Unit,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = onNavigateToSearch,
            title = uiState.language.albums,
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingAlbums,
            loading = uiState.language.loading
        ) {
            AlbumGrid(
                albums = uiState.albums,
                language = uiState.language,
                sortType = SortAlbumsBy.ALBUM_NAME,
                sortReverse = false,
                fallbackResourceId = fallbackResourceId,
                onSortReverseChange = {},
                onSortTypeChange = {},
                onAlbumClick = onAlbumClick,
                onPlayAlbum = onPlayAlbum,
                onViewArtist = onViewArtist,
                onPlayNext = onPlayNext,
                onShufflePlay = onShufflePlay,
                onAddToQueue = onAddToQueue,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onCreatePlaylist = onCreatePlaylist,
                onGetPlaylists = onGetPlaylists,
                onGetSongsInAlbum = onGetSongsInAlbum,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun AlbumsScreenContentPreview() {
    AlbumsScreenContent(
        uiState = AlbumsScreenUiState(
            albums = testAlbums,
            isLoadingAlbums = false,
            language = English,
            themeMode = SettingsDefaults.themeMode
        ),
        onAlbumClick = {},
        onSettingsClicked = {},
        onPlayAlbum = {},
        onNavigateToSearch = {},
        onViewArtist = {},
        onPlayNext = {},
        onShufflePlay = {},
        onAddToQueue = {},
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onAddSongsToPlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInAlbum = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
    )
}