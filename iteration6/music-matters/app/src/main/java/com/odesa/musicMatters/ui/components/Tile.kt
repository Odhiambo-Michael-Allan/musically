package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun GenericTile(
    modifier: Modifier,
    imageRequest: ImageRequest,
    title: String,
    description: String? = null,
    headerDescription: String,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlay: () -> Unit,
    onClick: () -> Unit,
    onShufflePlay: () -> Unit,
    onAddToQueue: () -> Unit,
    onPlayNext: () -> Unit,
    onGetSongs: () -> List<Song>,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist ) -> Unit,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    additionalBottomSheetMenuItems: ( @Composable ( () -> Unit ) -> Unit )? = null,
) {

    var showAddToPlaylistDialog by remember { mutableStateOf( false ) }
    var showCreateNewPlaylistDialog by remember { mutableStateOf( false ) }

    Tile(
        modifier = modifier,
        imageRequest = imageRequest,
        options = { expanded, onDismissRequest ->
            if ( expanded ) {
                ModalBottomSheet(
                    onDismissRequest = onDismissRequest
                ) {
                    GenericTileOptionsBottomSheet(
                        headerImage = imageRequest,
                        headerTitle = title,
                        headerDescription = headerDescription,
                        language = language,
                        onDismissRequest = onDismissRequest,
                        onShufflePlay = onShufflePlay,
                        onAddToQueue = onAddToQueue,
                        onAddToPlaylist = { showAddToPlaylistDialog = true },
                        onPlayNext = onPlayNext,
                        additionalBottomSheetMenuItems = additionalBottomSheetMenuItems,
                    )
                }
            }
        },
        content = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onPlay = onPlay,
        onClick = onClick
    )
    if ( showAddToPlaylistDialog ) {
        AddSongsToPlaylistDialog(
            songs = onGetSongs(),
            onGetPlaylists = onGetPlaylists,
            language = language,
            fallbackResourceId = fallbackResourceId,
            onGetSongsInPlaylist = onGetSongsInPlaylist,
            onAddSongsToPlaylist = onAddSongsToPlaylist,
            onCreateNewPlaylist = { showCreateNewPlaylistDialog = true  },
            onDismissRequest = { showAddToPlaylistDialog = false }
        )
    }
    if ( showCreateNewPlaylistDialog ) {
        NewPlaylistDialog(
            language = language,
            fallbackResourceId = fallbackResourceId,
            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            initialSongsToAdd = onGetSongs(),
            onConfirmation = { playlistName, selectedSongs ->
                showCreateNewPlaylistDialog = false
                onCreatePlaylist( playlistName, selectedSongs )
            },
            onDismissRequest = { showCreateNewPlaylistDialog = false }
        )
    }
}

@Composable
fun GenericTileOptionsBottomSheet(
    headerImage: ImageRequest,
    headerTitle: String,
    headerDescription: String,
    language: Language,
    onDismissRequest: () -> Unit,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: () -> Unit,
    additionalBottomSheetMenuItems: ( @Composable ( () -> Unit ) -> Unit )? = null,
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = headerImage,
                title = headerTitle,
                description = headerDescription
            )
        }
    ) {
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.shufflePlay
        ) {
            onDismissRequest()
            onShufflePlay()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.playNext
        ) {
            onDismissRequest()
            onPlayNext()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
            label = language.addToQueue
        ) {
            onDismissRequest()
            onAddToQueue()
        }
        BottomSheetMenuItem(
            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
            label = language.addToPlaylist
        ) {
            onDismissRequest()
            onAddToPlaylist()
        }
        additionalBottomSheetMenuItems?.let {
            it( onDismissRequest )
        }
    }
}

@Composable
fun Tile(
    modifier: Modifier,
    imageRequest: ImageRequest,
    options: @Composable ( Boolean, () -> Unit ) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    onPlay: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .then(modifier),
        colors = CardDefaults.cardColors( containerColor = Color.Transparent ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding( 12.dp )
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        model = imageRequest,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp)
                    ) {
                        var showOptionsMenu by remember { mutableStateOf( false ) }

                        IconButton(
                            onClick = { showOptionsMenu = !showOptionsMenu }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                            options( showOptionsMenu ) {
                                showOptionsMenu = false
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(12.dp)
                                )
                                .then(Modifier.size(36.dp)),
                            onClick = onPlay
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer( modifier = Modifier.height( 8.dp ) )
                content()
            }
        }
    }
}
