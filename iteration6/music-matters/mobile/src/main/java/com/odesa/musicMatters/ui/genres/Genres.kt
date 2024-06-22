package com.odesa.musicMatters.ui.genres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.core.data.preferences.SortGenresBy
import com.odesa.musicMatters.core.datatesting.genres.testGenres
import com.odesa.musicMatters.core.i8n.English

import com.odesa.musicMatters.ui.components.GenreGrid
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar

@Composable
fun GenresScreen(
    viewModel: GenresScreenViewModel,
    onGenreClick: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val genreScreenUiState by viewModel.uiState.collectAsState()

    GenresScreenContent(
        uiState = genreScreenUiState,
        onGenreClick = onGenreClick,
        onNavigateToSearch = onNavigateToSearch,
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun GenresScreenContent(
    uiState: GenreScreenUiState,
    onGenreClick: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = onNavigateToSearch,
            title = uiState.language.genres,
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            GenreGrid(
                genres = uiState.genres,
                language = uiState.language,
                sortType = SortGenresBy.GENRE,
                sortReverse = false,
                onSortReverseChange = {},
                onSortTypeChange = {},
                onGenreClick = onGenreClick
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun GenresScreenContentPreview() {
    GenresScreenContent(
        uiState = testGenreScreenUiState,
        onGenreClick = {},
        onNavigateToSearch = {},
        onSettingsClicked = {}
    )
}

val testGenreScreenUiState = GenreScreenUiState(
    genres = testGenres,
    language = English,
    isLoading = false
)