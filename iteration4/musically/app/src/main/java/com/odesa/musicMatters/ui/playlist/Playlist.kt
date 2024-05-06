package com.odesa.musicMatters.ui.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.PlaylistDropdownMenu
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun PlaylistScreen(
    playlistTitle: String,
    viewModel: PlaylistScreenViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    PlaylistScreenContent(
        playlistTitle = playlistTitle,
        uiState = uiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        playSong = {},
        onFavorite = {},
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun PlaylistScreenContent(
    playlistTitle: String,
    uiState: PlaylistScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onShufflePlay: () -> Unit,
    playSong: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onNavigateBack: () -> Unit
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    var showOptionsMenu by remember { mutableStateOf( false ) }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = onNavigateBack,
            title = playlistTitle,
            options = {
                IconButton(
                    onClick = { showOptionsMenu = !showOptionsMenu }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                    if ( showOptionsMenu ) {
                        PlaylistDropdownMenu(
                            playlistTitle = playlistTitle,
                            expanded = showOptionsMenu,
                            language = uiState.language,
                            onShufflePlay = { /*TODO*/ },
                            onPlayNext = { /*TODO*/ },
                            onSongsChanged = { /*TODO*/ },
                            onRename = { /*TODO*/ },
                            onDelete = { /*TODO*/ },
                            onDismissRequest = {
                                showOptionsMenu = false
                            },
                            onExportPlaylist = {}
                        )
                    }
                }
            }
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingSongsInPlaylist,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                onSortReverseChange = onSortReverseChange,
                sortSongsBy = SortSongsBy.TITLE,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songsInPlaylist,
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
fun PlaylistScreenContentPreview() {
    PlaylistScreenContent(
        playlistTitle = "Favorites",
        uiState = PlaylistScreenUiState(
            songsInPlaylist = testSongs,
            isLoadingSongsInPlaylist = false,
            language = English,
            currentlyPlayingSongId = testSongs.first().id,
            favoriteSongIds = emptyList(),
            themeMode = SettingsDefaults.themeMode,
        ),
        onFavorite = {},
        onShufflePlay = {},
        onSortTypeChange = {},
        onSortReverseChange = {},
        playSong = {},
        onNavigateBack = {}
    )
}