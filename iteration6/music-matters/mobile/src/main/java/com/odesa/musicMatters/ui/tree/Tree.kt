package com.odesa.musicMatters.ui.tree

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.components.TreeSongList
import com.odesa.musicMatters.ui.utils.displayToastWithMessage

@Composable
fun TreeScreen(
    viewModel: TreeScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    TreeScreenContent(
        uiState = uiState,
        togglePath = { viewModel.togglePath( it ) },
        onPlaySong = { viewModel.playSelectedSong( selectedSong =  it ) },
        onAddSongsToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist( playlist, songs )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs ->
            viewModel.createPlaylist( title, songs )
        },
        onSettingsClicked = onSettingsClicked,
        onPlayNext = {
            viewModel.playSongNext( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} will play next"
            )
        },
        onViewArtist = onViewArtist,
        onViewAlbum = onViewAlbum,
        onFavorite = { viewModel.addToFavorites( it ) },
        onAddToQueue = {
            viewModel.addSongToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} added to queue"
            )
        },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onNavigateToSearch = onNavigateToSearch,
        onGetPlaylists = { uiState.playlists }
    )
}

@Composable
fun TreeScreenContent(
    uiState: TreeScreenUiState,
    togglePath: ( String ) -> Unit,
    onPlaySong: (Song) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit,
    onGetPlaylists: () -> List<Playlist>
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column {
        TopAppBar(
            onNavigationIconClicked = onNavigateToSearch,
            title = uiState.language.tree,
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked,
        )
        LoaderScaffold(
            isLoading = uiState.isConstructingTree,
            loading = uiState.language.loading
        ) {
            TreeSongList(
                uiState = uiState,
                togglePath = togglePath,
                onPlaySong = onPlaySong,
                fallbackResourceId = fallbackResourceId,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
                onAddToQueue = onAddToQueue,
                onFavorite = onFavorite,
                onViewArtist = onViewArtist,
                onPlayNext = onPlayNext,
                onViewAlbum = onViewAlbum,
                onShareSong = onShareSong,
                onGetPlaylists = onGetPlaylists
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun TreeScreenContentPreview() {
    TreeScreenContent(
        uiState = testTreeScreenUiState,
        togglePath = {},
        onPlaySong = {},
        onSettingsClicked = {},
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onAddToQueue = {},
        onFavorite = {},
        onPlayNext = {},
        onShareSong = {},
        onViewAlbum = {},
        onViewArtist = {},
        onNavigateToSearch = {},
        onGetPlaylists = { emptyList() }
    )
}