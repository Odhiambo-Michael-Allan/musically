package com.odesa.musicMatters.ui.playlist

import android.net.Uri
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
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.PlaylistDropdownMenu
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.navigation.createShareSongIntent
import com.odesa.musicMatters.ui.navigation.displayToastWithMessage
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun PlaylistScreen(
    playlistTitle: String,
    viewModel: PlaylistScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    PlaylistScreenContent(
        playlistTitle = playlistTitle,
        uiState = uiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        playSong = { viewModel.playMedia( it.mediaItem ) },
        onFavorite = { viewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onPlayNext = onPlayNext,
        onAddToQueue = onAddToQueue,
        onAddSongToPlaylist = { playlist, song ->
            viewModel.addSongToPlaylist( playlist, song )
        },
        onShareSong = {
            try {
                val intent = createShareSongIntent( context, it )
                context.startActivity( intent )
            }
            catch ( exception: Exception ) {
                displayToastWithMessage(
                    context,
                    uiState.language.shareFailedX( exception.localizedMessage
                        ?: exception.toString() )
                )
            }
        }
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
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onAddSongToPlaylist: ( Playlist, Song ) -> Unit,
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
                playlists = uiState.playlists,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = {  playlist ->
                    uiState.songsInPlaylist.filter { playlist.songIds.contains( it.id ) }
                },
                onAddSongToPlaylist = onAddSongToPlaylist
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
            playlists = emptyList(),
        ),
        onFavorite = {},
        onShufflePlay = {},
        onSortTypeChange = {},
        onSortReverseChange = {},
        playSong = {},
        onViewAlbum = {},
        onViewArtist = {},
        onNavigateBack = {},
        onShareSong = {},
        onPlayNext = {},
        onAddToQueue = {},
        onAddSongToPlaylist = { _, _ -> }
    )
}