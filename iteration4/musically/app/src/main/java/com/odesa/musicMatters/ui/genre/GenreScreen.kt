package com.odesa.musicMatters.ui.genre

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun GenreScreen(
    genre: String,
) {

    GenreScreenContent(
        uiState = testGenreScreenUiState,
        genreName = genre,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onSettingsClicked = {},
        onShufflePlay = {},
        playSong = {},
        onFavorite = {}
    )
}

@Composable
fun GenreScreenContent(
    uiState: GenreScreenUiState,
    genreName: String,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onSettingsClicked: () -> Unit,
    onShufflePlay: () -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = { /*TODO*/ },
            title = "${uiState.language.genre} - $genreName"
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                onSortReverseChange = onSortReverseChange,
                sortSongsBy = SortSongsBy.TITLE,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songs,
                onShufflePlay = onShufflePlay,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun GenreScreenContentPreview() {
    GenreScreenContent(
        uiState = testGenreScreenUiState,
        genreName = "Hip Hop",
        onSortReverseChange = {},
        onSortTypeChange = {},
        onSettingsClicked = {},
        onShufflePlay = {},
        playSong = {},
        onFavorite = {}
    )
}

