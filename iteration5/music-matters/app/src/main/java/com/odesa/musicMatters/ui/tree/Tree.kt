package com.odesa.musicMatters.ui.tree

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.components.TreeSongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun TreeScreen(
    viewModel: TreeScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: (MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    TreeScreenContent(
        uiState = uiState,
        togglePath = { viewModel.togglePath( it ) },
        onPlaySong = { viewModel.playMedia( it.mediaItem ) },
        onAddSongToPlaylist = { playlist, song ->
            viewModel.saveSongToPlaylist( playlist, song )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs ->
            viewModel.createPlaylist( title, songs )
        },
        onSettingsClicked = onSettingsClicked,
        onPlayNext = onPlayNext,
        onViewArtist = onViewArtist,
        onViewAlbum = onViewAlbum,
        onFavorite = { viewModel.addToFavorites( it ) },
        onAddToQueue = onAddToQueue,
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onNavigateToSearch = onNavigateToSearch,
    )
}

@Composable
fun TreeScreenContent(
    uiState: TreeScreenUiState,
    togglePath: ( String ) -> Unit,
    onPlaySong: ( Song ) -> Unit,
    onAddSongToPlaylist: ( Playlist, Song ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
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
                onAddSongToPlaylist = onAddSongToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
                onAddToQueue = onAddToQueue,
                onFavorite = onFavorite,
                onViewArtist = onViewArtist,
                onPlayNext = onPlayNext,
                onViewAlbum = onViewAlbum,
                onShareSong = onShareSong,
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
        onAddSongToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onAddToQueue = {},
        onFavorite = {},
        onPlayNext = {},
        onShareSong = {},
        onViewAlbum = {},
        onViewArtist = {},
        onNavigateToSearch = {}
    )
}