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
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.preferences.SortArtistsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
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
        onPlaySongsByArtistNext = { viewModel.playSongsByArtistNext( it ) },
        onShufflePlaySongsByArtist = { viewModel.playSongsByArtist( it, shuffle = true ) },
        onAddSongsByArtistToQueue = { viewModel.addSongsByArtistToQueue( it ) },
        onAddSongsToPlaylist = { playlist, songs -> viewModel.addSongsToPlaylist( playlist, songs ) },
        onGetSongsByArtist = { viewModel.getSongsByArtist( it ) },
        onGetPlaylists = { viewModel.getPlaylists() },
        onCreatePlaylist = { name, songs -> viewModel.createPlaylist( name, songs ) },
        onSortTypeChange = { viewModel.setSortArtistsBy( it ) },
        onSortReverseChange = { viewModel.setSortArtistsInReverse( it ) },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) }
    )
}

@Composable
fun ArtistsScreenContent(
    uiState: ArtistsScreenUiState,
    onArtistClick: ( String ) -> Unit,
    onPlaySongsByArtist: ( Artist ) -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortArtistsBy ) -> Unit,
    onAddSongsByArtistToQueue: ( Artist ) -> Unit,
    onShufflePlaySongsByArtist: ( Artist ) -> Unit,
    onPlaySongsByArtistNext: ( Artist ) -> Unit,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onGetSongsByArtist: ( Artist ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
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
                sortBy = uiState.sortArtistsBy,
                sortReverse = uiState.sortArtistsInReverse,
                fallbackResourceId = fallbackResourceId,
                onSortReverseChange = onSortReverseChange,
                onSortTypeChange = onSortTypeChange,
                onArtistClick = onArtistClick,
                onPlaySongsByArtist = onPlaySongsByArtist,
                onAddSongsByArtistToQueue = onAddSongsByArtistToQueue,
                onShufflePlaySongsByArtist = onShufflePlaySongsByArtist,
                onPlaySongsByArtistNext = onPlaySongsByArtistNext,
                onAddSongsByArtistToPlaylist = onAddSongsToPlaylist,
                onGetSongsByArtist = onGetSongsByArtist,
                onGetPlaylists = onGetPlaylists,
                onCreatePlaylist = onCreatePlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
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
            themeMode = SettingsDefaults.themeMode,
            sortArtistsBy = SettingsDefaults.sortArtistsBy,
            sortArtistsInReverse = false
        ),
        onArtistClick = {},
        onPlaySongsByArtist = {},
        onSettingsClicked = {},
        onNavigateToSearch = {},
        onAddSongsByArtistToQueue = {},
        onShufflePlaySongsByArtist = {},
        onPlaySongsByArtistNext = {},
        onAddSongsToPlaylist = { _, _ -> },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onSearchSongsMatchingQuery = { emptyList() },
        onGetSongsByArtist = { emptyList() },
        onSortTypeChange = {},
        onSortReverseChange = {}
    )
}