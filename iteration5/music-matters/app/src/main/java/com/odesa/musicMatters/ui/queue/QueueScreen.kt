package com.odesa.musicMatters.ui.queue

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.NewPlaylistDialog
import com.odesa.musicMatters.ui.components.QueueScreenTopAppBar
import com.odesa.musicMatters.ui.components.emptyQueueScreenUiState
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun QueueScreen(
    viewModel: QueueScreenViewModel,
    onPlayNext: (MediaItem ) -> Unit,
    onViewAlbum: (String ) -> Unit,
    onViewArtist: (String ) -> Unit,
    onAddToQueue: (MediaItem ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onBackArrowClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    QueueScreenContent(
        uiState = uiState,
        onBackArrowClick = onBackArrowClick,
        onCreatePlaylist = { title, songs -> viewModel.createPlaylist( title, songs ) },
        onClearClick = { viewModel.clearQueue() },
        onFavorite = { viewModel.addToFavorites( it ) },
        playSong = {
            viewModel.playMedia(
                it.mediaItem,
            )
        },
        onMoveSong = { from, to -> viewModel.moveSong( from, to ) },
        onPlayNext = onPlayNext,
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onAddToQueue = onAddToQueue,
        onAddSongToPlaylist = { playlist, song ->
            viewModel.addSongToPlaylist( playlist, song )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) }
    )
}

@Composable
fun QueueScreenContent(
    uiState: QueueScreenUiState,
    onBackArrowClick: () -> Unit,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onClearClick: () -> Unit,
    onFavorite: ( String ) -> Unit,
    playSong: ( Song ) -> Unit,
    onMoveSong: ( Int, Int ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onAddSongToPlaylist: ( Playlist, Song ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    var showSaveDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QueueScreenTopAppBar(
            onBackArrowClick = onBackArrowClick,
            onSaveClick = {
                showSaveDialog = !showSaveDialog
            },
            onClearClick = onClearClick
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            QueueList(
                uiState = uiState,
                fallbackResourceId = fallbackResourceId,
                isFavorite = { uiState.favoriteSongIds.contains(it) },
                onFavorite = onFavorite,
                playSong = playSong,
                onMove = onMoveSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onShareSong = onShareSong,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onAddSongToPlaylist = onAddSongToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
            )
        }

        if ( showSaveDialog ) {
            NewPlaylistDialog(
                language = uiState.language,
                fallbackResourceId = fallbackResourceId,
                initialSongsToAdd = uiState.songsInQueue,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onConfirmation = { title, songs ->
                    onCreatePlaylist( title, songs )
                    showSaveDialog = false
                },
                onDismissRequest = { showSaveDialog = false }
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun QueueScreenContentPreview() {
    QueueScreenContent(
        uiState = emptyQueueScreenUiState,
        onBackArrowClick = {},
        onCreatePlaylist = { _, _ -> },
        onClearClick = {},
        onFavorite = {},
        playSong = {},
        onMoveSong = { _, _ -> {} },
        onPlayNext = {},
        onAddToQueue = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onAddSongToPlaylist = { _, _, -> },
        onSearchSongsMatchingQuery = { emptyList() }
    )
}

