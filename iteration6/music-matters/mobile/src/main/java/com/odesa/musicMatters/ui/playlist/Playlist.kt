package com.odesa.musicMatters.ui.playlist

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.designsystem.theme.MusicMattersTheme
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.BottomSheetMenuItem
import com.odesa.musicMatters.ui.components.GenericOptionsBottomSheet
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.RenamePlaylistDialog
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.utils.displayToastWithMessage

@Composable
fun PlaylistScreen(
    playlistTitle: String,
    viewModel: PlaylistScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    PlaylistScreenContent(
        playlistTitle = playlistTitle,
        uiState = uiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = { viewModel.shuffleAndPlay( songs = uiState.songsInPlaylist ) },
        playSong = {
            viewModel.playSongs(
                selectedSong = it,
                songsInPlaylist = uiState.songsInPlaylist
            )
        },
        onFavorite = { viewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onPlayNext = {
            viewModel.playSongNext( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} will play next"
            )
        },
        onAddToQueue = {
            viewModel.addSongToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} added to queue"
            )
        },
        onAddSongsToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist( playlist, songs )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs ->
            viewModel.createPlaylist( title, songs )
        },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onGetPlaylists = { uiState.playlists },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
        onPlaySongsInPlaylistNext = { viewModel.playSongsNext( songs = uiState.songsInPlaylist ) },
        onAddSongsInPlaylistToQueue = { viewModel.addSongsToQueue( songs = uiState.songsInPlaylist ) },
        onRenamePlaylist = { viewModel.renamePlaylist( it ) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreenContent(
    playlistTitle: String,
    uiState: PlaylistScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onShufflePlay: () -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onNavigateBack: () -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsInPlaylistToQueue: () -> Unit,
    onPlaySongsInPlaylistNext: () -> Unit,
    onRenamePlaylist: ( String ) -> Unit,
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
                        ModalBottomSheet(
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            PlaylistBottomSheetMenu(
                                isLoading = uiState.isLoadingSongsInPlaylist,
                                playlist = uiState.playlist,
                                fallbackResourceId = fallbackResourceId,
                                language = uiState.language,
                                onGetSongsInPlaylist = onGetSongsInPlaylist,
                                onShufflePlay = onShufflePlay,
                                onPlayNext = onPlaySongsInPlaylistNext,
                                onAddToQueue = onAddSongsInPlaylistToQueue,
                                onDismissRequest = { showOptionsMenu = false },
                                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                                onGetPlaylists = onGetPlaylists,
                                onGetSongs = { onGetSongsInPlaylist( uiState.playlist ) },
                                onAddSongsToPlaylist = onAddSongsToPlaylist,
                                onCreatePlaylist = onCreatePlaylist,
                                onRenamePlaylist = onRenamePlaylist,
                            )
                        }
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
                playlists = onGetPlaylists(),
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist
            )
        }
    }
}

@Composable
fun PlaylistBottomSheetMenu(
    isLoading: Boolean,
    playlist: Playlist,
    @DrawableRes fallbackResourceId: Int,
    language: Language,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onCreatePlaylist: (String, List<Song>) -> Unit,
    onGetSongs: () -> List<Song>,
    onSearchSongsMatchingQuery: (String ) -> List<Song>,
    onDismissRequest: () -> Unit,
    onRenamePlaylist: ( String ) -> Unit,
) {

    var showRenamePlaylistDialog by remember { mutableStateOf( false ) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        if ( isLoading ) {
            CircularProgressIndicator(
                modifier = Modifier.align( Alignment.TopCenter )
            )
        } else {
            GenericOptionsBottomSheet(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( onGetSongsInPlaylist( playlist ).firstOrNull { it.artworkUri != null }?.artworkUri )
                    placeholder( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                headerTitle = playlist.title,
                headerDescription = language.playlist,
                language = language,
                fallbackResourceId = fallbackResourceId,
                onDismissRequest = onDismissRequest,
                onShufflePlay = onShufflePlay,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onGetPlaylists = onGetPlaylists,
                onCreatePlaylist = onCreatePlaylist,
                onGetSongs = onGetSongs,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                additionalBottomSheetMenuItems = {
                    BottomSheetMenuItem(
                        leadingIcon = Icons.Default.Save,
                        label = language.export,
                        onClick = onDismissRequest
                    )
                    BottomSheetMenuItem(
                        leadingIcon = Icons.Default.Edit,
                        label = language.rename,
                        onClick = {
                            showRenamePlaylistDialog = true
                            onDismissRequest()
                        }
                    )
                    BottomSheetMenuItem(
                        leadingIcon = Icons.Default.Delete,
                        leadingIconTint = Color.Red,
                        label = language.delete,
                        onClick = onDismissRequest
                    )
                }
            )
        }
        if ( showRenamePlaylistDialog ) {
            RenamePlaylistDialog(
                playlistTitle = playlist.title,
                language = language,
                onRename = onRenamePlaylist
            ) {
                showRenamePlaylistDialog = false
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun PlaylistBottomSheetMenuPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = SettingsDefaults.USE_MATERIAL_YOU
    ) {
        PlaylistBottomSheetMenu(
            isLoading = false,
            playlist = testPlaylists.first(),
            fallbackResourceId = R.drawable.placeholder_light,
            language = English,
            onGetSongsInPlaylist = { emptyList() },
            onPlayNext = {},
            onShufflePlay = {},
            onAddToQueue = {},
            onGetPlaylists = { emptyList() },
            onCreatePlaylist = { _, _ -> },
            onGetSongs = { emptyList() },
            onAddSongsToPlaylist = { _, _ -> },
            onSearchSongsMatchingQuery = { emptyList() },
            onRenamePlaylist = {},
            onDismissRequest = {}
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun PlaylistScreenContentPreview() {
    PlaylistScreenContent(
        playlistTitle = "Favorites",
        uiState = testPlaylistScreenUiState,
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
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onAddSongsInPlaylistToQueue = {},
        onPlaySongsInPlaylistNext = {},
        onRenamePlaylist = {}
    )
}