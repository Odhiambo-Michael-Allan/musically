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
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.designsystem.theme.isLight
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Artist
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.components.AlbumRow
import com.odesa.musicMatters.ui.components.Banner
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.utils.displayToastWithMessage


@Composable
fun ArtistScreen(
    artistName: String,
    viewModel: ArtistScreenViewModel,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: (String ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onNavigateBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ArtistScreenContent(
        artistName = artistName,
        uiState = uiState,
        onSortTypeChange = { viewModel.setSortSongsBy( it ) },
        onSortReverseChange = { viewModel.setSortSongsByArtistInReverse( it ) },
        onPlaySongsInAlbum = { viewModel.playSongsInAlbum( it ) },
        onShufflePlay = { viewModel.shufflePlaySongsByArtist( it ) },
        onFavorite = { viewModel.addToFavorites( it ) },
        playSong = {
            viewModel.playSongs(
                selectedSong = it,
                songsInPlaylist = uiState.songsByArtist
            )
        },
        onViewAlbum = { onViewAlbum( it ) },
        onViewArtist = onViewArtist,
        onNavigateBack = onNavigateBack,
        onPlayNext = {
            viewModel.playSongNext( it )
            displayToastWithMessage(
                context,
                "${it.title} will play next"
            )
        },
        onAddToQueue = {
            viewModel.addSongToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "${it.title} added to queue"
            )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs -> viewModel.createPlaylist( title, songs ) },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onAddSongsToPlaylist = { playlist, songs -> viewModel.addSongsToPlaylist( playlist, songs ) },
        onGetSongsInAlbum = { viewModel.getSongsInAlbum( it ) },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
        onShufflePlaySongsInAlbum = { viewModel.shufflePlaySongsInAlbum( it ) },
        onPlaySongsInAlbumNext = {
            viewModel.playSongsInAlbumNext( it )
            displayToastWithMessage(
                context = context,
                message = "Songs in ${it.title} will play next"
            )
        },
        onAddSongsInAlbumToQueue = {
            viewModel.addSongsInAlbumToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "Songs in ${it.title} added to queue"
            )
        },
        onAddSongsByArtistToQueue = {
            viewModel.addSongsByArtistToQueue( it )
            displayToastWithMessage(
                context = context,
                message = "Songs by ${it.name} added to queue"
            )
        },
        onPlaySongsByArtistNext = {
            viewModel.playSongsByArtistNext( it )
            displayToastWithMessage(
                context = context,
                message = "Songs by ${it.name} will play next"
            )
        },
        onShuffleAndPlaySongsByArtist = { viewModel.shufflePlaySongsByArtist( it ) },
        onGetPlaylists = { uiState.playlists }
    )
}

@Composable
fun ArtistScreenContent(
    artistName: String,
    uiState: ArtistScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onPlaySongsInAlbum: (Album) -> Unit,
    onShufflePlay: (Artist) -> Unit,
    playSong: (Song) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onNavigateBack: () -> Unit,
    onShufflePlaySongsInAlbum: ( Album ) -> Unit,
    onPlaySongsInAlbumNext: ( Album ) -> Unit,
    onAddSongsInAlbumToQueue: ( Album ) -> Unit,
    onAddSongsByArtistToQueue: ( Artist ) -> Unit,
    onPlaySongsByArtistNext: ( Artist ) -> Unit,
    onShuffleAndPlaySongsByArtist: ( Artist ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
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
                sortReverse = uiState.sortSongsByArtistInReverse,
                sortSongsBy = uiState.sortSongsByArtistBy,
                language = uiState.language,
                songs = uiState.songsByArtist,
                fallbackResourceId = fallbackResourceId,
                onShufflePlay = { uiState.artist?.let { onShufflePlay( it ) } },
                onSortTypeChange = onSortTypeChange,
                onSortReverseChange = onSortReverseChange,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playlists = onGetPlaylists(),
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains(it) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onAddSongsToPlaylist = onAddSongsToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist,
                leadingContent = {
                    item {
                        ArtistArtwork(
                            artist = uiState.artist!!,
                            language = uiState.language,
                            fallbackResourceId = fallbackResourceId,
                            onShufflePlay = onShuffleAndPlaySongsByArtist,
                            onPlayNext = onPlaySongsByArtistNext,
                            onAddToQueue = onAddSongsByArtistToQueue,
                            onGetSongsByArtist = { uiState.songsByArtist },
                            onGetPlaylists = onGetPlaylists,
                            onGetSongsInPlaylist = onGetSongsInPlaylist,
                            onAddSongsToPlaylist = onAddSongsToPlaylist,
                            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                            onCreatePlaylist = onCreatePlaylist
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
                                onAddToQueue = onAddSongsInAlbumToQueue,
                                onPlayNext = onPlaySongsInAlbumNext,
                                onShufflePlay = onShufflePlaySongsInAlbum,
                                onViewAlbum = onViewAlbum,
                                onViewArtist = onViewArtist,
                                onAddSongsToPlaylist = onAddSongsToPlaylist,
                                onCreatePlaylist = onCreatePlaylist,
                                onGetPlaylists = onGetPlaylists,
                                onGetSongsInAlbum = onGetSongsInAlbum,
                                onGetSongsInPlaylist = onGetSongsInPlaylist,
                                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
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
    onShufflePlay: ( Artist ) -> Unit,
    onPlayNext: ( Artist ) -> Unit,
    onAddToQueue: ( Artist ) -> Unit,
    onGetSongsByArtist: ( Artist ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: (String ) -> List<Song>,
    onCreatePlaylist: (String, List<Song> ) -> Unit,
) {
    Banner(
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( artist.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        bottomSheetHeaderTitle = language.artist,
        bottomSheetHeaderDescription = artist.name,
        language = language,
        fallbackResourceId = fallbackResourceId,
        onShufflePlay = { onShufflePlay( artist ) },
        onPlayNext = { onPlayNext( artist ) },
        onAddToQueue = { onAddToQueue( artist ) },
        onAddSongsToPlaylist = onAddSongsToPlaylist,
        onGetSongs = { onGetSongsByArtist( artist ) },
        onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
        onGetPlaylists = onGetPlaylists,
        onCreatePlaylist = onCreatePlaylist,
        onGetSongsInPlaylist = onGetSongsInPlaylist,
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
        uiState = testArtistScreenUiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        playSong = {},
        onFavorite = {},
        onPlaySongsInAlbum = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onPlayNext = {},
        onNavigateBack = {},
        onAddToQueue = {},
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onAddSongsToPlaylist = { _, _ -> },
        onGetSongsInAlbum = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onShufflePlaySongsInAlbum = {},
        onPlaySongsInAlbumNext = {},
        onAddSongsInAlbumToQueue = {},
        onAddSongsByArtistToQueue = {},
        onPlaySongsByArtistNext = {},
        onShuffleAndPlaySongsByArtist = {},
        onGetPlaylists = { emptyList() }
    )
}