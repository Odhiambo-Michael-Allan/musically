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
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.NewPlaylistDialog
import com.odesa.musicMatters.ui.components.QueueScreenTopAppBar
import com.odesa.musicMatters.ui.utils.displayToastWithMessage

@Composable
fun QueueScreen(
    viewModel: QueueScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
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
            viewModel.playSongs(
                selectedSong = it,
                songsInPlaylist = uiState.songsInQueue
            )
        },
        onMoveSong = { from, to -> viewModel.moveSong( from, to ) },
        onPlayNext = {
            viewModel.playSongNext( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} will play next"
            )
        },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onAddToQueue = {
            viewModel.addSongToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} added to queue"
            )
        },
        onAddSongsToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist( playlist, songs )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onGetPlaylists = { uiState.playlists }
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
    onPlayNext: ( Song ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>
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
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
                onGetPlaylists = { onGetPlaylists() }
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
        uiState = testQueueScreenUiState,
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
        onAddSongsToPlaylist = { _, _, -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onGetPlaylists = { emptyList() }
    )
}

