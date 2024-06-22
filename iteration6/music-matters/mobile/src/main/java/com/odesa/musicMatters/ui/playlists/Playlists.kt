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
import com.odesa.musicMatters.core.data.preferences.SortPlaylistsBy
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.PlaylistGrid
import com.odesa.musicMatters.ui.components.TopAppBar

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
        onPlaySongsInPlaylist = { viewModel.playSongsInPlaylist( it ) },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
        onAddSongsInPlaylistToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist(
                playlist = playlist,
                songs = songs
            )
        },
        onAddSongsInPlaylistToQueue = {  playlist ->
            viewModel.apply {
                getSongsInPlaylist( playlist ).forEach { addSongToQueue( it ) }
            }
        },
        onCreatePlaylist = { playlistTitle, songs ->
            viewModel.createPlaylist(
                playlistTitle = playlistTitle,
                songsToAddToPlaylist = songs
            )
        },
        onPlaySongsInPlaylistNext = { playlist ->
            viewModel.apply {
                getSongsInPlaylist( playlist ).forEach { playSongNext( it ) }
            }
        },
        onShufflePlaySongsInPlaylist = {
            viewModel.apply {
                shuffleAndPlay(
                    songs = getSongsInPlaylist( it )
                )
            }
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) }
    )
}

@Composable
fun PlaylistsScreenContent(
    uiState: PlaylistsScreenUiState,
    onPlaylistClick: ( String, String ) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSettingsClicked: () -> Unit,
    onPlaySongsInPlaylist: ( Playlist ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsInPlaylistToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onAddSongsInPlaylistToQueue: ( Playlist ) -> Unit,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onPlaySongsInPlaylistNext: ( Playlist ) -> Unit,
    onShufflePlaySongsInPlaylist: ( Playlist ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
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
                    onGetSongsInPlaylist = onGetSongsInPlaylist,
                    onGetPlaylists = { uiState.playlists },
                    onAddSongsInPlaylistToPlaylist = onAddSongsInPlaylistToPlaylist,
                    onAddSongsInPlaylistToQueue = onAddSongsInPlaylistToQueue,
                    onCreatePlaylist = onCreatePlaylist,
                    onPlaySongsInPlaylistNext = onPlaySongsInPlaylistNext,
                    onShufflePlaySongsInPlaylist = onShufflePlaySongsInPlaylist,
                    onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                )
            }
        }


        Column (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
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
        uiState = testPlaylistsScreenUiState,
        onSettingsClicked = {},
        onPlaySongsInPlaylist = {},
        onPlaylistClick = { _, _ -> },
        onNavigateToSearch = {},
        onGetSongsInPlaylist = { emptyList() },
        onAddSongsInPlaylistToPlaylist = { _, _ -> },
        onPlaySongsInPlaylistNext = {},
        onSearchSongsMatchingQuery = { emptyList() },
        onAddSongsInPlaylistToQueue = {},
        onCreatePlaylist = { _, _ -> },
        onShufflePlaySongsInPlaylist = {}
    )
}