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
import com.odesa.musicMatters.core.data.preferences.SortAlbumsBy
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.AlbumGrid
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun AlbumsScreen(
    viewModel: AlbumsScreenViewModel,
    onAlbumClick: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit,
    onViewArtist: (String ) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    AlbumsScreenContent(
        uiState = uiState,
        onAlbumClick = onAlbumClick,
        onSettingsClicked = onSettingsClicked,
        onPlayAlbum = { viewModel.playSongsInAlbum( it ) },
        onNavigateToSearch = onNavigateToSearch,
        onViewArtist = onViewArtist,
        onPlayNext = { viewModel.playSongsInAlbumNext( it ) },
        onShufflePlay = {
            viewModel.shufflePlaySongsInAlbum( it )
        },
        onAddToQueue = { viewModel.addSongsInAlbumToQueue( it ) },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs -> viewModel.createPlaylist( title, songs ) },
        onAddSongsToPlaylist = { playlist, songs -> viewModel.addSongsToPlaylist( playlist, songs ) },
        onGetPlaylists = { uiState.playlists },
        onGetSongsInAlbum = { viewModel.getSongsInAlbum( it ) },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
    )
}

@Composable
fun AlbumsScreenContent(
    uiState: AlbumsScreenUiState,
    onAlbumClick: ( String ) -> Unit,
    onSettingsClicked: () -> Unit,
    onPlayAlbum: ( Album ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: (Album) -> Unit,
    onShufflePlay: ( Album ) -> Unit,
    onAddToQueue: ( Album ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
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
        uiState = testAlbumsScreenUiState,
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