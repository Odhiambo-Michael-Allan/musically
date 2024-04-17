package com.odesa.musicMatters.ui.genres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.data.preferences.GenreSortBy
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.ui.components.GenreGridList
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.components.testGenreList

@Composable
fun GenresScreen(
    viewModel: GenreScreenViewModel,
    onSettingsClicked: () -> Unit
) {

    val genreScreenUiState by viewModel.uiState.collectAsState()

    GenresScreenContent(
        uiState = genreScreenUiState,
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun GenresScreenContent(
    uiState: GenreScreenUiState,
    onSettingsClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = { /*TODO*/ },
            title = uiState.language.genres,
            rescan = uiState.language.rescan,
            onRefreshClicked = { /*TODO*/ },
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            GenreGridList(
                genres = uiState.genres,
                language = uiState.language,
                sortType = GenreSortBy.GENRE,
                sortReverse = false,
                onSortReverseChange = {},
                onSortTypeChange = {}
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun GenresScreenContentPreview() {
    GenresScreenContent(
        uiState = testGenreScreenUiState,
        onSettingsClicked = {}
    )
}

val testGenreScreenUiState = GenreScreenUiState(
    genres = testGenreList,
    language = English,
    isLoading = false
)