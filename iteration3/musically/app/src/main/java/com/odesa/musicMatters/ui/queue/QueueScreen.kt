package com.odesa.musicMatters.ui.queue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.QueueScreenTopAppBar
import com.odesa.musicMatters.ui.components.QueueSongList
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
        onSaveClick = {},
        onClearClick = {}
    )
}

@Composable
fun QueueScreenContent(
    uiState: QueueScreenUiState,
    onBackArrowClick: () -> Unit,
    onSaveClick: () -> Unit,
    onClearClick: () -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QueueScreenTopAppBar(
            onBackArrowClick = onBackArrowClick,
            onSaveClick = onSaveClick,
            onClearClick = onClearClick
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            QueueSongList(
                uiState = uiState,
                fallbackResourceId = fallbackResourceId
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
        onClearClick = {}
    )
}

