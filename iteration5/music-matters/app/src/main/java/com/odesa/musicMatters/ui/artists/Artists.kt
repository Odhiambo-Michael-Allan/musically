package com.odesa.musicMatters.ui.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortArtistsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.ui.components.ArtistsGrid
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun ArtistsScreen(
    viewModel: ArtistsScreenViewModel,
    onArtistClick: ( String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val artistsScreenUiState by viewModel.uiState.collectAsState()

    ArtistsScreenContent(
        uiState = artistsScreenUiState,
        onArtistClick = onArtistClick,
        onPlaySongsByArtist = { viewModel.playSongsByArtist( it ) },
        onSettingsClicked = onSettingsClicked,
        onNavigateToSearch = onNavigateToSearch,
        onPlayNext = {},
        onShufflePlay = {},
        onAddToQueue = {},
    )
}

@Composable
fun ArtistsScreenContent(
    uiState: ArtistsScreenUiState,
    onArtistClick: ( String ) -> Unit,
    onPlaySongsByArtist: ( Artist ) -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onAddToQueue: ( Artist ) -> Unit,
    onShufflePlay: ( Artist ) -> Unit,
    onPlayNext: ( Artist ) -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = onNavigateToSearch,
            title = uiState.language.artists,
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingArtists,
            loading = uiState.language.loading
        ) {
            ArtistsGrid(
                artists = uiState.artists,
                language = uiState.language,
                sortBy = SortArtistsBy.ARTIST_NAME,
                sortReverse = false,
                fallbackResourceId = fallbackResourceId,
                onSortReverseChange = {},
                onSortTypeChange = {},
                onArtistClick = onArtistClick,
                onPlaySongsByArtist = onPlaySongsByArtist,
                onAddToQueue = onAddToQueue,
                onShufflePlay = onShufflePlay,
                onPlayNext = onPlayNext,
            )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun ArtistsScreenContentPreview() {
    ArtistsScreenContent(
        uiState = ArtistsScreenUiState(
            artists = testArtists,
            isLoadingArtists = false,
            language = English,
            themeMode = SettingsDefaults.themeMode
        ),
        onArtistClick = {},
        onPlaySongsByArtist = {},
        onSettingsClicked = {},
        onNavigateToSearch = {},
        onAddToQueue = {},
        onShufflePlay = {},
        onPlayNext = {}
    )
}