package com.odesa.musicMatters.ui.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.extensions.formatMilliseconds
import com.odesa.musicMatters.services.media.testSongs

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SongCard(
    language: Language,
    song: Song,
    isCurrentlyPlaying: Boolean,
    isFavorite: Boolean,
    playlists: List<Playlist>,
    @DrawableRes fallbackResourceId: Int,
    onClick: () -> Unit,
    onFavorite: ( String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
) {

    var showSongOptionsBottomSheet by remember { mutableStateOf( false ) }
    var showSongDetailsDialog by remember { mutableStateOf( false ) }
    var showAddToPlaylistDialog by remember { mutableStateOf( false ) }
    var showCreateNewPlaylistDialog by remember { mutableStateOf( false ) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors( containerColor = Color.Transparent ),
        onClick = onClick
    ) {
        Box( modifier = Modifier.padding( 12.dp, 12.dp, 4.dp, 12.dp ) ) {
            Row( verticalAlignment = Alignment.CenterVertically ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder( LocalContext.current ).apply {
                            data( song.artworkUri )
                            placeholder( fallbackResourceId )
                            fallback( fallbackResourceId )
                            error( fallbackResourceId )
                            crossfade( true )
                        }.build(),
                        modifier = Modifier
                            .size(45.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = null
                    )
                }
                Spacer( modifier = Modifier.width( 16.dp ) )
                Column( modifier = Modifier.weight( 1f ) ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = when {
                                isCurrentlyPlaying -> MaterialTheme.colorScheme.primary
                                else -> LocalTextStyle.current.color
                            }
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.artists.joinToString(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer( modifier = Modifier.width( 15.dp ) )
                Row {
                    if ( isFavorite ) {
                        IconButton(
                            onClick = { onFavorite( song.id ) }
                        ) {
                            Icon(
                                modifier = Modifier.size( 24.dp ),
                                imageVector = Icons.Filled.Favorite,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                    }
                    IconButton(
                        onClick = { showSongOptionsBottomSheet = !showSongOptionsBottomSheet }
                    ) {
                        Icon(
                            modifier = Modifier.size( 24.dp ),
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        if ( showSongOptionsBottomSheet ) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    showSongOptionsBottomSheet = false
                                }
                            ) {
                                SongOptionsBottomSheetMenu(
                                    language = language,
                                    song = song,
                                    isFavorite = isFavorite,
                                    isCurrentlyPlaying = isCurrentlyPlaying,
                                    fallbackResourceId = fallbackResourceId,
                                    onFavorite = onFavorite,
                                    onAddToQueue = onAddToQueue,
                                    onPlayNext = onPlayNext,
                                    onViewArtist = onViewArtist,
                                    onViewAlbum = onViewAlbum,
                                    onShareSong = onShareSong,
                                    onShowSongDetails = { showSongDetailsDialog = true },
                                    onAddToPlaylist = { showAddToPlaylistDialog = true },
                                    onDismissRequest = {
                                        showSongOptionsBottomSheet = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            if ( showSongDetailsDialog ) {
                SongDetailsDialog(
                    song = song,
                    language = language,
                    durationFormatter = { it.formatMilliseconds() }
                ) {
                    showSongDetailsDialog = false
                }
            }
            if ( showAddToPlaylistDialog ) {
                AddSongsToPlaylistDialog(
                    songs = listOf( song ),
                    onGetPlaylists = { playlists },
                    language = language,
                    fallbackResourceId = fallbackResourceId,
                    onGetSongsInPlaylist = onGetSongsInPlaylist,
                    onAddSongsToPlaylist = { onAddSongsToPlaylist( it, listOf( song ) ) },
                    onCreateNewPlaylist = { showCreateNewPlaylistDialog = true },
                    onDismissRequest = { showAddToPlaylistDialog = false }
                )
            }
            if ( showCreateNewPlaylistDialog ) {
                NewPlaylistDialog(
                    language = uiState.language,
                    fallbackResourceId = fallbackResourceId,
                    initialSongsToAdd = listOf( song ),
                    onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                    onConfirmation = { playlistName, selectedSongs ->
                        showCreateNewPlaylistDialog = false
                        onCreatePlaylist( playlistName, selectedSongs )
                    },
                    onDismissRequest = { showCreateNewPlaylistDialog = false }
                )
            }
        }
    }
}

@Composable
fun SongOptionsBottomSheetMenu(
    language: Language,
    song: Song,
    isFavorite: Boolean,
    isCurrentlyPlaying: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onFavorite: ( String ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onShowSongDetails: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( song.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = song.title,
                titleIsHighlighted = isCurrentlyPlaying,
                description = song.artists.joinToString()
            )
        }
    ) {
        BottomSheetMenuItem(
            imageVector = if ( isFavorite ) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            label = language.favorite
        ) {
            onFavorite( song.id )
            onDismissRequest()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.playNext
        ) {
            onDismissRequest()
            onPlayNext( song.mediaItem )
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.addToQueue
        ) {
            onAddToQueue( song.mediaItem )
            onDismissRequest()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
            label = language.addToPlaylist
        ) {
            onDismissRequest()
            onAddToPlaylist()
        }
        song.artists.forEach {
            BottomSheetMenuItem(
                imageVector = Icons.Filled.Person,
                label = "${language.viewArtist}: $it"
            ) {
                onDismissRequest()
                onViewArtist( it )
            }
        }
        song.albumTitle?.let {
            BottomSheetMenuItem(
                imageVector = Icons.Filled.Album,
                label = language.viewAlbum
            ) {
                onDismissRequest()
                onViewAlbum( it )
            }
        }
        BottomSheetMenuItem(
            imageVector = Icons.Filled.Share,
            label = language.shareSong
        ) {
            onDismissRequest()
            onShareSong( song.mediaUri )
        }
        BottomSheetMenuItem(
            imageVector = Icons.Filled.Info,
            label = language.details
        ) {
            onDismissRequest()
            onShowSongDetails()
        }
    }
}

@Preview( showBackground = true )
@Composable
fun SongOptionsBottomSheetContentPreview() {
    SongOptionsBottomSheetMenu(
        language = English,
        song = testSongs.first(),
        isFavorite = true,
        isCurrentlyPlaying = true,
        fallbackResourceId = R.drawable.placeholder_light,
        onFavorite = {},
        onAddToQueue = {},
        onPlayNext = { /*TODO*/ },
        onViewArtist = {},
        onViewAlbum = {},
        onShareSong = {},
        onShowSongDetails = {},
        onAddToPlaylist = {},
        onDismissRequest = {}
    )
}

@Composable
fun NowPlayingSongOptionsBottomSheetMenu(
    song: Song,
    language: Language,
    isFavorite: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onDismissRequest: () -> Unit
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( song.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = song.title,
                titleIsHighlighted = true,
                description = song.artists.joinToString()
            )
        }
    ) {
        BottomSheetMenuItem(
            imageVector = if ( isFavorite ) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            label = language.favorite,
            onClick = {
                onFavorite( song.id )
                onDismissRequest()
            }
        )
        song.albumTitle?.let {
            BottomSheetMenuItem(
                imageVector = Icons.Filled.Album,
                label = language.viewAlbum
            ) {
                onDismissRequest()
                onViewAlbum( it )
            }
        }
        BottomSheetMenuItem(
            imageVector = Icons.Filled.Person,
            label = "${language.viewArtist}: ${song.artists}"
        ) {
            onDismissRequest()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
            label = language.addToPlaylist
        ) {
            onDismissRequest()
        }
        BottomSheetMenuItem(
            imageVector = Icons.Filled.Share,
            label = language.shareSong
        ) {
            onDismissRequest()
        }
        BottomSheetMenuItem(
            imageVector = Icons.Filled.Info,
            label = language.details
        ) {
            onDismissRequest()
        }
    }
}

@Composable
fun QueueSongCard(
    modifier: Modifier = Modifier,
    language: Language,
    song: Song,
    isCurrentlyPlaying: Boolean,
    isFavorite: Boolean,
    playlists: List<Playlist>,
    @DrawableRes fallbackResourceId: Int,
    onClick: () -> Unit,
    onFavorite: ( String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: (String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onDragHandleClick: () -> Unit,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = modifier,
            onClick = onDragHandleClick
        ) {
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = null
            )
        }
        SongCard(
            language = language,
            song = song,
            isCurrentlyPlaying = isCurrentlyPlaying,
            isFavorite = isFavorite,
            playlists = playlists,
            fallbackResourceId = fallbackResourceId,
            onClick = onClick,
            onFavorite = onFavorite,
            onPlayNext = onPlayNext,
            onAddToQueue = onAddToQueue,
            onViewArtist = onViewArtist,
            onViewAlbum = onViewAlbum,
            onShareSong = onShareSong,
            onGetSongsInPlaylist = onGetSongsInPlaylist,
            onAddSongsToPlaylist = onAddSongsToPlaylist,
            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            onCreatePlaylist = onCreatePlaylist
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun SongCardPreview() {
    SongCard(
        language = English,
        song = testSongs.first(),
        isCurrentlyPlaying = true,
        isFavorite = true,
        playlists = emptyList(),
        fallbackResourceId = R.drawable.placeholder_light,
        onClick = {},
        onFavorite = {},
        onPlayNext = {},
        onAddToQueue = {},
        onViewArtist = {},
        onViewAlbum = {},
        onShareSong = {},
        onGetSongsInPlaylist = { emptyList() },
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> }
    )
}

@Preview( showBackground = true )
@Composable
fun QueueSongCardPreview() {
    QueueSongCard(
        language = English,
        song = testSongs.first(),
        isCurrentlyPlaying = true,
        isFavorite = true,
        playlists = emptyList(),
        fallbackResourceId = R.drawable.placeholder_light,
        onClick = {},
        onFavorite = {},
        onPlayNext = {},
        onAddToQueue = {},
        onViewArtist = {},
        onViewAlbum = {},
        onShareSong = {},
        onGetSongsInPlaylist = { emptyList() },
        onDragHandleClick = {},
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> }
    )
}