package com.odesa.musicMatters.ui.forYou

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
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
import com.odesa.musicMatters.ui.components.ArtistsRow
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun ForYouScreen(
    viewModel: ForYouScreenViewModel,
    onSettingsClicked: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onSuggestedAlbumClick: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ForYouScreenContent(
        uiState = uiState,
        onShuffleAndPlay = { viewModel.shuffleAndPlay() },
        onSettingsClicked = onSettingsClicked,
        onNavigateToSearch = onNavigateToSearch,
        onSongInRecentlyAddedSongsSelected = { viewModel.playRecentlyAddedSong( it ) },
        onSongInMostPlayedSongsSelected = { viewModel.playMostPlayedSong( it ) },
        onSongInPlayHistorySelected = { viewModel.playSongInPlayHistory( it ) },
        onSuggestedAlbumClick = onSuggestedAlbumClick,
        onViewArtist = onViewArtist,
        onPlaySongsInAlbum = {
            viewModel.playSongsInAlbum(
                album = it,
                shuffle = false
            )
        },
        onShufflePlaySongsInAlbum = {
            viewModel.playSongsInAlbum(
                album = it,
                shuffle = true
            )
        },
        onAddSongsInAlbumToQueue = {
            val numberOfSongsAddedToQueue = viewModel.addSongsInAlbumToQueue( it )
            val messageToDisplayToUser = if ( numberOfSongsAddedToQueue < 1 )
                "All Songs in album are already in queue"
            else
                "Added $numberOfSongsAddedToQueue songs to queue"
            Toast.makeText(
                context,
                messageToDisplayToUser,
                Toast.LENGTH_SHORT
            ).show()
        },
        onPlaySongsInAlbumNext = {
            viewModel.playSongsInAlbumNext( it )
            Toast.makeText(
                context,
                "Songs in album ${it.name} will be played next",
                Toast.LENGTH_SHORT
            ).show()
        },
        onSearchSongsMatchingQuery = { query ->
            viewModel.searchSongsMatching( query )
        },
        onGetPlaylists = { viewModel.getPlaylists() },
        onGetSongsInAlbum = { viewModel.getSongsInAlbum( it ) },
        onCreatePlaylist = { playlistName, songsToAddToPlaylist ->
            viewModel.createPlaylist( playlistName, songsToAddToPlaylist )
        },
        onAddSongsToPlaylist = { playlist, songs ->
            viewModel.addSongsToPlaylist( playlist, songs )
        },
        onGetSongsInPlaylist = { viewModel.getSongsInPlaylist( it ) },
        onGetSongsByArtist = { viewModel.getSongsByArtist( it ) },
        onPlaySongsByArtistNext = {
            viewModel.playSongsByArtistNext( it )
            Toast.makeText(
                context,
                "Songs by artist ${it.name} will be played next",
                Toast.LENGTH_SHORT
            ).show()
        },
        onShufflePlaySongsByArtist = {
            viewModel.playSongsByArtist(
                artist = it,
                shuffle = true
            )
        },
        onAddSongsByArtistToQueue = {
            val numberOfSongsAddedToQueue = viewModel.addSongsByArtistToQueue( it )
            val messageToDisplayToUser = if ( numberOfSongsAddedToQueue < 1 )
                "All Songs by artist ${it.name} are already in queue"
            else
                "Added $numberOfSongsAddedToQueue songs to queue"
            Toast.makeText(
                context,
                messageToDisplayToUser,
                Toast.LENGTH_SHORT
            ).show()
        },
        onPlaySongsByArtist = {
            viewModel.playSongsByArtist(
                artist = it
            )
        }
    )
}

@Composable
fun ForYouScreenContent(
    uiState: ForYouScreenUiState,
    onShuffleAndPlay: () -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onSongInRecentlyAddedSongsSelected: ( MediaItem ) -> Unit,
    onSongInMostPlayedSongsSelected: ( MediaItem ) -> Unit,
    onSongInPlayHistorySelected: ( MediaItem ) -> Unit,
    onSuggestedAlbumClick: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlaySongsInAlbum: ( Album ) -> Unit,
    onShufflePlaySongsInAlbum: ( Album ) -> Unit,
    onAddSongsInAlbumToQueue: ( Album ) -> Unit,
    onPlaySongsInAlbumNext: ( Album ) -> Unit,
    onSearchSongsMatchingQuery: (String ) -> List<Song>,
    onCreatePlaylist: (String, List<Song> ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: (Playlist ) -> List<Song>,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsByArtist: ( Artist ) -> List<Song>,
    onShufflePlaySongsByArtist: ( Artist ) -> Unit,
    onPlaySongsByArtistNext: (Artist ) -> Unit,
    onAddSongsByArtistToQueue: ( Artist ) -> Unit,
    onPlaySongsByArtist: ( Artist ) -> Unit,
    ) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            TopAppBar(
                onNavigationIconClicked = onNavigateToSearch,
                title = stringResource( id = R.string.app_name ),
                settings = "Settings",
                onSettingsClicked = onSettingsClicked
            )
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
            ) {
                RecentlyAddedSongs(
                    language = uiState.language,
                    isLoadingRecentSongs = uiState.isLoadingRecentSongs,
                    recentlyAddedSongs = uiState.recentlyAddedSongs,
                    fallbackResourceId = fallbackResourceId,
                    onSongInRecentlyAddedSongsSelected = onSongInRecentlyAddedSongsSelected
                )
                SuggestedAlbums(
                    language = uiState.language,
                    isLoading = uiState.isLoadingSuggestedAlbums,
                    albums = uiState.suggestedAlbums,
                    fallbackResourceId = fallbackResourceId,
                    onPlaySongsInAlbum = onPlaySongsInAlbum,
                    onShufflePlaySongsInAlbum = onShufflePlaySongsInAlbum,
                    onViewArtist = onViewArtist,
                    onAddSongsInAlbumToQueue = onAddSongsInAlbumToQueue,
                    onPlaySongsInAlbumNext = onPlaySongsInAlbumNext,
                    onClick = { onSuggestedAlbumClick( it ) },
                    onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                    onCreatePlaylist = onCreatePlaylist,
                    onAddSongsToPlaylist = onAddSongsToPlaylist,
                    onGetPlaylists = onGetPlaylists,
                    onGetSongsInPlaylist = onGetSongsInPlaylist,
                    onGetSongsInAlbum = onGetSongsInAlbum
                )
                if ( uiState.mostPlayedSongs.isNotEmpty() ) {
                    MostPlayedSongs(
                        language = uiState.language,
                        isLoadingMostPlayedSongs = uiState.isLoadingMostPlayedSongs,
                        mostPlayedSongs = uiState.mostPlayedSongs,
                        fallbackResourceId = fallbackResourceId,
                        onSongInMostPlayedSongsSelected = onSongInMostPlayedSongsSelected
                    )
                }
                SuggestedArtists(
                    language = uiState.language,
                    isLoading = uiState.isLoadingSuggestedArtists,
                    suggestedArtists = uiState.suggestedArtists,
                    fallbackResourceId = fallbackResourceId,
                    onSuggestedArtistClick = { onViewArtist( it.name ) },
                    onGetSongsByArtist = onGetSongsByArtist,
                    onAddToQueue = onAddSongsByArtistToQueue,
                    onShufflePlay = onShufflePlaySongsByArtist,
                    onPlayNext = onPlaySongsByArtistNext,
                    onGetPlaylists = onGetPlaylists,
                    onGetSongsInPlaylist = onGetSongsInPlaylist,
                    onAddSongsToPlaylist = onAddSongsToPlaylist,
                    onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                    onCreatePlaylist = onCreatePlaylist,
                    onPlaySongsByArtist = onPlaySongsByArtist
                )
                if ( uiState.recentlyPlayedSongs.isNotEmpty() ) {
                    PlayHistory(
                        language = uiState.language,
                        isLoadingPlayHistory = uiState.isLoadingRecentlyPlayedSongs,
                        songsInPlayHistory = uiState.recentlyPlayedSongs,
                        fallbackResourceId = fallbackResourceId,
                        onSongInPlayHistorySelected = onSongInPlayHistorySelected
                    )
                }
                Spacer( modifier = Modifier.height( 100.dp ) )
            }
        }
        LargeFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = onShuffleAndPlay
        ) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = null
            )
        }
    }
}

@Composable
fun RecentlyAddedSongs(
    language: Language,
    isLoadingRecentSongs: Boolean,
    recentlyAddedSongs: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInRecentlyAddedSongsSelected: ( MediaItem ) -> Unit
) {
    ForYouSongRow(
        heading = language.recentlyAddedSongs,
        isLoading = isLoadingRecentSongs,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = recentlyAddedSongs,
        onSongSelected = onSongInRecentlyAddedSongsSelected
    )
}

@Composable
fun MostPlayedSongs(
    language: Language,
    isLoadingMostPlayedSongs: Boolean,
    mostPlayedSongs: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInMostPlayedSongsSelected: ( MediaItem ) -> Unit,
) {
    ForYouSongRow(
        heading = "Most Played Songs",
        isLoading = isLoadingMostPlayedSongs,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = mostPlayedSongs,
        onSongSelected = onSongInMostPlayedSongsSelected
    )
}

@Composable
fun PlayHistory(
    language: Language,
    isLoadingPlayHistory: Boolean,
    songsInPlayHistory: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onSongInPlayHistorySelected: ( MediaItem ) -> Unit
) {
    ForYouSongRow(
        heading = "Play History",
        isLoading = isLoadingPlayHistory,
        language = language,
        fallbackResourceId = fallbackResourceId,
        songs = songsInPlayHistory,
        onSongSelected = onSongInPlayHistorySelected
    )
}

@Composable
fun ForYouSongRow(
    heading: String,
    isLoading: Boolean,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    songs: List<Song>,
    onSongSelected: ( MediaItem ) -> Unit,
) {
    SideHeading {
        Text( text = heading )
    }
    when {
        isLoading -> Loading()
        songs.isEmpty() -> Empty( language = language )
        else -> {
            LazyRow (
                contentPadding = PaddingValues( 20.dp, 0.dp ),
                horizontalArrangement = Arrangement.spacedBy( 8.dp )
            ) {
                items( songs ) { song ->
                    ForYouSongCard(
                        modifier = Modifier
                            .width(300.dp)
                            .height(96.dp),
                        song = song,
                        fallbackResourceId = fallbackResourceId,
                        onClick = { onSongSelected( song.mediaItem ) }
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun ForYouScreenContentPreview() {
    ForYouScreenContent(
        uiState = testForYouScreenUiState,
        onShuffleAndPlay = {},
        onSettingsClicked = {},
        onSongInPlayHistorySelected = {},
        onSongInMostPlayedSongsSelected = {},
        onSongInRecentlyAddedSongsSelected = {},
        onSuggestedAlbumClick = {},
        onViewArtist = {},
        onNavigateToSearch = {},
        onPlaySongsInAlbum = {},
        onShufflePlaySongsInAlbum = {},
        onAddSongsInAlbumToQueue = {},
        onPlaySongsInAlbumNext = {},
        onAddSongsToPlaylist = { _, _ -> },
        onGetSongsInAlbum = { emptyList() },
        onCreatePlaylist = { _, _ -> },
        onGetPlaylists = { emptyList() },
        onGetSongsInPlaylist = { emptyList() },
        onSearchSongsMatchingQuery = { emptyList() },
        onGetSongsByArtist = { emptyList() },
        onPlaySongsByArtistNext = {},
        onShufflePlaySongsByArtist = {},
        onAddSongsByArtistToQueue = {},
        onPlaySongsByArtist = {},
    )
}

@Composable
private fun SideHeading( text: @Composable () -> Unit ) {
    Box(
        modifier = Modifier.padding( 20.dp, 12.dp )
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        ) {
            text()
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * 0.2f).dp)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Empty(
    language: Language
) {
    val height = ( LocalConfiguration.current.screenHeightDp * 0.15f ).dp
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language.damnThisIsSoEmpty,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy( alpha = 0.7f )
            )
        )
    }
}

@Composable
fun ForYouSongCard(
    modifier: Modifier = Modifier,
    song: Song,
    @DrawableRes fallbackResourceId: Int,
    onClick: () -> Unit
) {

    val backgroundColor = MaterialTheme.colorScheme.surface

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = ImageRequest.Builder( LocalContext.current ).apply {
                    data( song.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = 0.2f),
                                backgroundColor.copy(alpha = 0.7f),
                                backgroundColor.copy(alpha = 0.8f),
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier.padding( 8.dp )
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp)),
                        model = ImageRequest.Builder( LocalContext.current ).apply {
                            data( song.artworkUri )
                            placeholder( fallbackResourceId )
                            fallback( fallbackResourceId )
                            error( fallbackResourceId )
                            crossfade( true )
                        }.build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    backgroundColor.copy(alpha = 0.25f),
                                    CircleShape
                                )
                                .padding(1.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size( 20.dp ),
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if ( song.artists.isNotEmpty() ) {
                        Text(
                            text = song.artists.joinToString(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestedAlbums(
    language: Language,
    isLoading: Boolean,
    albums: List<Album>,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsInAlbum: ( Album ) -> Unit,
    onShufflePlaySongsInAlbum: ( Album ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddSongsInAlbumToQueue: ( Album ) -> Unit,
    onPlaySongsInAlbumNext: ( Album ) -> Unit,
    onClick: ( String ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>
) {
    SideHeading {
        Text( text = language.suggestedAlbums )
    }
    when {
        isLoading -> RowLoading()
        albums.isEmpty() -> RowEmpty( language )
        else -> AlbumRow(
            albums = albums,
            language = language,
            fallbackResourceId = fallbackResourceId,
            onPlaySongsInAlbum = onPlaySongsInAlbum,
            onAddToQueue = onAddSongsInAlbumToQueue,
            onPlayNext = onPlaySongsInAlbumNext,
            onShufflePlay = onShufflePlaySongsInAlbum,
            onViewArtist = onViewArtist,
            onViewAlbum = onClick,
            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            onCreatePlaylist = onCreatePlaylist,
            onAddSongsToPlaylist = onAddSongsToPlaylist,
            onGetPlaylists = onGetPlaylists,
            onGetSongsInAlbum = onGetSongsInAlbum,
            onGetSongsInPlaylist = onGetSongsInPlaylist,
        )
    }
}

@Composable
private fun SuggestedArtists(
    language: Language,
    isLoading: Boolean,
    suggestedArtists: List<Artist>,
    @DrawableRes fallbackResourceId: Int,
    onSuggestedArtistClick: ( Artist ) -> Unit,
    onAddToQueue: ( Artist ) -> Unit,
    onShufflePlay: ( Artist ) -> Unit,
    onPlayNext: ( Artist ) -> Unit,
    onGetSongsByArtist: ( Artist ) -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onPlaySongsByArtist: ( Artist ) -> Unit,
) {

    SideHeading {
        Text( text = language.suggestedArtists )
    }
    when {
        isLoading -> RowLoading()
        suggestedArtists.isEmpty() -> RowEmpty( language )
        else -> ArtistsRow(
            artists = suggestedArtists,
            language = language,
            fallbackResourceId = fallbackResourceId,
            onAddToQueue = onAddToQueue,
            onPlayNext = onPlayNext,
            onShufflePlay = onShufflePlay,
            onViewArtist = onSuggestedArtistClick,
            onGetSongsByArtist = onGetSongsByArtist,
            onGetPlaylists = onGetPlaylists,
            onGetSongsInPlaylist = onGetSongsInPlaylist,
            onAddSongsToPlaylist = onAddSongsToPlaylist,
            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            onCreatePlaylist = onCreatePlaylist,
            onPlaySongsByArtist = onPlaySongsByArtist,
        )
    }
}

@Composable
private fun RowLoading() {
    Box(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * 0.2f).dp)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun RowEmpty(
    language: Language
) {
    val height = ( LocalConfiguration.current.screenHeightDp * 0.15f ).dp
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(0.dp, 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language.damnThisIsSoEmpty,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy( alpha = 0.7f )
            )
        )
    }
}

val testForYouScreenUiState = ForYouScreenUiState(
    language = English,
    themeMode = SettingsDefaults.themeMode,
    isLoadingRecentSongs = false,
    recentlyAddedSongs = testSongs,
    isLoadingSuggestedAlbums = false,
    suggestedAlbums = testAlbums,
    isLoadingMostPlayedSongs = false,
    mostPlayedSongs = testSongs,
    isLoadingSuggestedArtists = false,
    suggestedArtists = testArtists,
    isLoadingRecentlyPlayedSongs = false,
    recentlyPlayedSongs = testSongs,
)