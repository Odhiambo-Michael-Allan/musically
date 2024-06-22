package com.odesa.musicMatters.ui.album

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.Banner
import com.odesa.musicMatters.ui.components.BottomSheetMenuItem
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.utils.displayToastWithMessage

@Composable
fun AlbumScreen(
    albumName: String,
    viewModel: AlbumScreenViewModel,
    onNavigateBack: () -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    AlbumScreenContent(
        uiState = uiState,
        albumName = albumName,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {
            uiState.album?.let { viewModel.shufflePlaySongsInAlbum( it ) }
        },
        onNavigateBack = onNavigateBack,
        playSong = {
            viewModel.playSongs(
                selectedSong = it,
                songsInPlaylist = uiState.songsInAlbum
            )
        },
        onFavorite = { viewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
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
        onCreatePlaylist = { playlistName, songs ->
            viewModel.createPlaylist(
                playlistTitle = playlistName,
                songsToAddToPlaylist = songs
            )
        },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onGetPlaylists = { uiState.playlists },
        onPlaySongsInAlbumNext = { viewModel.playSongsInAlbumNext( it ) },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) }
    )
}

@Composable
fun AlbumScreenContent(
    uiState: AlbumScreenUiState,
    albumName: String,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onShufflePlay: () -> Unit,
    onNavigateBack: () -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onPlaySongsInAlbumNext: ( Album ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = onNavigateBack,
            title = albumName
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingSongsInAlbum,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                sortSongsBy = SortSongsBy.TITLE,
                language = uiState.language,
                songs = uiState.songsInAlbum,
                fallbackResourceId = fallbackResourceId,
                onShufflePlay = onShufflePlay,
                onSortTypeChange = onSortTypeChange,
                onSortReverseChange = onSortReverseChange,
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
                onGetSongsInPlaylist = { playlist ->
                    uiState.songsInAlbum.filter { playlist.songIds.contains( it.id ) }
                },
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
                leadingContent = {
                    item {
                        AlbumArtwork(
                            album = uiState.album!!,
                            language = uiState.language,
                            fallbackResourceId = fallbackResourceId,
                            onAddSongsInAlbumToQueue = {},
                            onShufflePlaySongsInAlbum = { onShufflePlay() },
                            onPlaySongsInAlbumNext = onPlaySongsInAlbumNext,
                            onViewArtist = onViewArtist,
                            onAddSongsToPlaylist = onAddSongsToPlaylist,
                            onGetPlaylists = onGetPlaylists,
                            onGetSongsInPlaylist = onGetSongsInPlaylist,
                            onCreatePlaylist = onCreatePlaylist,
                            onGetSongsInAlbum = { uiState.songsInAlbum },
                            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery
                        )
                    }
                }
            )
        }

    }
}

@Composable
private fun AlbumArtwork(
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlaySongsInAlbum: ( Album ) -> Unit,
    onPlaySongsInAlbumNext: ( Album ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onAddSongsInAlbumToQueue: ( Album ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
) {
    Banner(
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( album.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        bottomSheetHeaderTitle = language.album,
        bottomSheetHeaderDescription = album.title,
        fallbackResourceId = fallbackResourceId,
        language = language,
        onShufflePlay = { onShufflePlaySongsInAlbum( album ) },
        onPlayNext = { onPlaySongsInAlbumNext( album ) },
        onAddToQueue = { onAddSongsInAlbumToQueue( album ) },
        onGetPlaylists = onGetPlaylists,
        onCreatePlaylist = onCreatePlaylist,
        onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
        onGetSongs = { onGetSongsInAlbum( album ) },
        onGetSongsInPlaylist = onGetSongsInPlaylist,
        onAddSongsToPlaylist = onAddSongsToPlaylist,
        additionalBottomSheetMenuItems = { onDismissRequest ->
            album.artists.forEach { artistName ->
                BottomSheetMenuItem(
                    leadingIcon = Icons.Default.Person,
                    label = artistName
                ) {
                    onDismissRequest()
                    onViewArtist( artistName )
                }
            }
        }
    ) {
        Column {
            Text( text = album.title )
            if ( album.artists.isNotEmpty() ) {
                Text(
                    text = album.artists.joinToString(),
                    style = MaterialTheme.typography.bodyMedium
                        .copy( fontWeight = FontWeight.Bold )
                )
            }
        }
    }
}


@Preview( showSystemUi = true )
@Composable
fun AlbumScreenContentPreview() {
    AlbumScreenContent(
        uiState = testAlbumScreenUiState,
        albumName = "Mixer",
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = { /*TODO*/ },
        onNavigateBack = { /*TODO*/ },
        playSong = {},
        onFavorite = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onPlayNext = {},
        onAddToQueue = {},
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onPlaySongsInAlbumNext = {}
    )
}