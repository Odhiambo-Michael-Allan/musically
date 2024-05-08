package com.odesa.musicMatters.ui.tree

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.components.TreeSongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun TreeScreen(
    viewModel: TreeScreenViewModel,
    onSettingsClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    TreeScreenContent(
        uiState = uiState,
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun TreeScreenContent(
    uiState: TreeScreenUiState,
    onSettingsClicked: () -> Unit
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column {
        TopAppBar(
            onNavigationIconClicked = { /*TODO*/ },
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
                togglePath = {},
                onPlaySong = {},
                fallbackResourceId = fallbackResourceId
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun TreeScreenContentPreview() {
    TreeScreenContent(
        uiState = testTreeScreenUiState,
        onSettingsClicked = {}
    )
}