package com.odesa.musicMatters.ui.artist

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.AlbumRow
import com.odesa.musicMatters.ui.components.ArtistDropdownMenu
import com.odesa.musicMatters.ui.components.Banner
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.navigation.createShareSongIntent
import com.odesa.musicMatters.ui.navigation.displayToastWithMessage
import com.odesa.musicMatters.ui.theme.isLight


@Composable
fun ArtistScreen(
    artistName: String,
    viewModel: ArtistScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: (String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ArtistScreenContent(
        artistName = artistName,
        uiState = uiState,
        onSortTypeChange = {},
        onSortReverseChange = {},
        onPlaySongsInAlbum = { viewModel.playAlbum( it.name ) },
        onShufflePlay = {},
        onFavorite = {},
        playSong = { viewModel.playSong( it.mediaItem ) },
        onViewAlbum = { onViewAlbum( it ) },
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onPlayNext = onPlayNext,
        onAddToQueue = onAddToQueue,
        onAddSongToPlaylist = { playlist, song -> viewModel.addSongToPlaylist( playlist, song ) },
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
fun ArtistScreenContent(
    artistName: String,
    uiState: ArtistScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onPlaySongsInAlbum: ( Album ) -> Unit,
    onShufflePlay: () -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onAddSongToPlaylist: ( Playlist, Song ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = onNavigateBack,
            title = artistName
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingSongsByArtist,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = false,
                sortSongsBy = SortSongsBy.TITLE,
                language = uiState.language,
                songs = uiState.songsByArtist,
                fallbackResourceId = fallbackResourceId,
                onShufflePlay = onShufflePlay,
                onSortTypeChange = onSortTypeChange,
                onSortReverseChange = onSortReverseChange,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playlists = uiState.playlists,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains(it) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = { playlist ->
                    uiState.songsByArtist.filter { playlist.songIds.contains( it.id ) }
                },
                onAddSongToPlaylist = onAddSongToPlaylist,
                leadingContent = {
                    item {
                        ArtistArtwork(
                            artist = uiState.artist!!,
                            language = uiState.language,
                            fallbackResourceId = fallbackResourceId,
                            onShufflePlay = { /*TODO*/ },
                            onPlayNext = { /*TODO*/ },
                            onAddToQueue = {}
                        )
                    }
                    if ( uiState.albumsByArtist.isNotEmpty() ) {
                        item {
                            Spacer( modifier = Modifier.height( 4.dp ) )
                            Text(
                                modifier = Modifier.padding( 12.dp ),
                                text = uiState.language.albums,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            AlbumRow(
                                albums = uiState.albumsByArtist,
                                language = uiState.language,
                                fallbackResourceId = fallbackResourceId,
                                onPlaySongsInAlbum = { onPlaySongsInAlbum( it ) },
                                onAddToQueue = {},
                                onPlayNext = { /*TODO*/ },
                                onShufflePlay = { /*TODO*/ },
                                onViewAlbum = onViewAlbum
                            )
                            HorizontalDivider()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ArtistArtwork(
    artist: Artist,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
) {
    Banner(
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( artist.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        options = { expanded, onDismissRequest ->
            ArtistDropdownMenu(
                expanded = expanded,
                language = language,
                onShufflePlay = onShufflePlay,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onDismissRequest = onDismissRequest
            )
        }
    ) {
        Column {
            Text( text = artist.name )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun ArtistScreenContentPreview() {
    ArtistScreenContent(
        artistName = "Travis Scott",
        uiState = ArtistScreenUiState(
            artist = testArtists.first(),
            songsByArtist = testSongs,
            isLoadingSongsByArtist = false,
            albumsByArtist = testAlbums,
            language = English,
            themeMode = SettingsDefaults.themeMode,
            currentlyPlayingSongId = testSongs.first().id,
            favoriteSongIds = emptyList(),
            playlists = emptyList(),
        ),
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = { /*TODO*/ },
        playSong = {},
        onFavorite = {},
        onPlaySongsInAlbum = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onPlayNext = {},
        onNavigateBack = {},
        onAddToQueue = {},
        onAddSongToPlaylist = { _, _ -> }
    )
}