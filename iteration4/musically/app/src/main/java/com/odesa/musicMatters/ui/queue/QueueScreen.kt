package com.odesa.musicMatters.ui.queue

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
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.NewPlaylistDialog
import com.odesa.musicMatters.ui.components.QueueScreenTopAppBar
import com.odesa.musicMatters.ui.components.emptyQueueScreenUiState
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun QueueScreen(
    queueScreenViewModel: QueueScreenViewModel,
    onBackArrowClick: () -> Unit
) {

    val uiState by queueScreenViewModel.uiState.collectAsState()

    QueueScreenContent(
        uiState = uiState,
        onBackArrowClick = onBackArrowClick,
        onSaveClick = { queueScreenViewModel.saveCurrentPlaylist( it ) },
        onClearClick = { queueScreenViewModel.clearQueue() },
        onFavorite = { queueScreenViewModel.addToFavorites( it ) },
        playSong = {
            queueScreenViewModel.playMedia(
                it.mediaItem,
            )
        },
        onMoveSong = { from, to -> queueScreenViewModel.moveSong( from, to ) }
    )
}

@Composable
fun QueueScreenContent(
    uiState: QueueScreenUiState,
    onBackArrowClick: () -> Unit,
    onSaveClick: ( String ) -> Unit,
    onClearClick: () -> Unit,
    onFavorite: ( String ) -> Unit,
    playSong: ( Song ) -> Unit,
    onMoveSong: ( Int, Int ) -> Unit,
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
                onMove = onMoveSong
            )
        }

        if ( showSaveDialog ) {
            NewPlaylistDialog(
                language = uiState.language,
                onConfirmation = {
                    onSaveClick( it )
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
        onSaveClick = {},
        onClearClick = {},
        onFavorite = {},
        playSong = {},
        onMoveSong = { _, _ -> {} }
    )
}

