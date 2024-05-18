package com.odesa.musicMatters.ui.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.SortPlaylistsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.PlaylistGrid
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel,
    onPlaylistClick: ( String, String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    PlaylistsScreenContent(
        uiState = uiState,
        onPlaylistClick = onPlaylistClick,
        onNavigateToSearch = onNavigateToSearch,
        onSettingsClicked = onSettingsClicked,
        onPlaySongsInPlaylist = { viewModel.playSongsInPlaylist( it.songIds ) }
    )
}

@Composable
fun PlaylistsScreenContent(
    uiState: PlaylistsScreenUiState,
    onPlaylistClick: ( String, String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit,
    onPlaySongsInPlaylist: ( Playlist ) -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TopAppBar(
                onNavigationIconClicked = onNavigateToSearch,
                title = uiState.language.playlists,
                settings = uiState.language.settings,
                onSettingsClicked = onSettingsClicked,
            )
            LoaderScaffold(
                isLoading = uiState.isLoadingSongs,
                loading = uiState.language.loading
            ) {
                PlaylistGrid(
                    playlists = uiState.playlists,
                    language = uiState.language,
                    sortType = SortPlaylistsBy.CUSTOM,
                    sortReverse = false,
                    fallbackResourceId = fallbackResourceId,
                    onSortReverseChange = {},
                    onSortTypeChange = {},
                    onPlaylistClick = onPlaylistClick,
                    onPlaySongsInPlaylist = onPlaySongsInPlaylist,
                    onGetSongsInPlaylist = { playlist -> uiState.songs.filter { playlist.songIds.contains( it.id ) } }
                )
            }
        }


        Column (
            modifier = Modifier
                .align( Alignment.BottomEnd )
                .padding( 16.dp ),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding( 8.dp ),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
            ExtendedFloatingActionButton(
                modifier = Modifier.padding( 8.dp ),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.ImportExport,
                    contentDescription = null
                )
                Text( text = "Import" )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PlaylistsScreenContentPreview() {
    PlaylistsScreenContent(
        uiState = PlaylistsScreenUiState(
            songs = emptyList(),
            playlists = testPlaylists,
            isLoadingSongs = false,
            language = English,
            themeMode = SettingsDefaults.themeMode
        ),
        onSettingsClicked = {},
        onPlaySongsInPlaylist = {},
        onPlaylistClick = { _, _ -> },
        onNavigateToSearch = {}
    )
}