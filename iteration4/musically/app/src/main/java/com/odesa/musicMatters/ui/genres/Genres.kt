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
import com.odesa.musicMatters.ui.components.GenreGrid
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.components.testGenreList

@Composable
fun GenresScreen(
    viewModel: GenresScreenViewModel,
    onGenreClick: ( String ) -> Unit,
    onSettingsClicked: () -> Unit
) {

    val genreScreenUiState by viewModel.uiState.collectAsState()

    GenresScreenContent(
        uiState = genreScreenUiState,
        onGenreClick = onGenreClick,
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun GenresScreenContent(
    uiState: GenreScreenUiState,
    onGenreClick: ( String ) -> Unit,
    onSettingsClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = { /*TODO*/ },
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
                sortType = GenreSortBy.GENRE,
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
        onSettingsClicked = {}
    )
}

val testGenreScreenUiState = GenreScreenUiState(
    genres = testGenreList,
    language = English,
    isLoading = false
)