package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song

@Composable
fun PlaylistTile(
    playlistTitle: String,
    songsInPlaylist: List<Song>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsInPlaylist: () -> Unit,
    onExportPlaylist: () -> Unit,
    onDeletePlayList: () -> Unit,
    onRenamePlaylist: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onSongsChanged: () -> Unit,
    onPlaylistClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors( containerColor = Color.Transparent ),
        onClick = onPlaylistClick
    ) {
        Box(
            modifier = Modifier.padding( 12.dp )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        model = ImageRequest.Builder( LocalContext.current ).apply {
                            data( songsInPlaylist.firstOrNull { it.artworkUri != null }?.artworkUri )
                            placeholder( fallbackResourceId )
                            error( fallbackResourceId )
                            crossfade( true )
                        }.build(),
                        contentDescription = null,
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
                            PlaylistDropdownMenu(
                                playlistTitle = playlistTitle,
                                expanded = showOptionsMenu,
                                language = language,
                                onDismissRequest = {
                                    showOptionsMenu = false
                                },
                                onExportPlaylist = onExportPlaylist,
                                onDelete = onDeletePlayList,
                                onRename = onRenamePlaylist,
                                onPlayNext = onPlayNext,
                                onShufflePlay = onShufflePlay,
                                onSongsChanged = onSongsChanged,
                            )
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
                            onClick = onPlaySongsInPlaylist
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer( modifier = Modifier.height( 8.dp ) )
                Text(
                    text = playlistTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun PlaylistDropdownMenu(
    playlistTitle: String,
    expanded: Boolean,
    language: Language,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onSongsChanged: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    onExportPlaylist: () -> Unit,
) {

    var showSongsPicker by remember { mutableStateOf( false ) }
    var showInfoDialog by remember { mutableStateOf( false ) }
    var showDeleteDialog by remember { mutableStateOf( false ) }
    var showAddToPlaylistDialog by remember { mutableStateOf( false ) }
    var showRenameDialog by remember { mutableStateOf( false ) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.shufflePlay )
            },
            onClick = {
                onDismissRequest()
                onShufflePlay()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.playNext )
            },
            onClick = {
                onDismissRequest()
                onPlayNext()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.addToPlaylist )
            },
            onClick = {
                onDismissRequest()
                showAddToPlaylistDialog = true
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.manageSongs )
            },
            onClick = {
                onDismissRequest()
                showSongsPicker = true
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.details )
            },
            onClick = {
                onDismissRequest()
                showInfoDialog = true
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.export )
            },
            onClick = {
                onDismissRequest()
                onExportPlaylist()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.rename )
            },
            onClick = {
                onDismissRequest()
                showRenameDialog = true
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.delete )
            },
            onClick = {
                onDismissRequest()
                showDeleteDialog = true
            }
        )
    }

    if ( showRenameDialog ) {
        RenamePlaylistDialog(
            playlistTitle = playlistTitle,
            language = language,
            onRename = onRename,
            onDismissRequest = {
                showRenameDialog = false
            }
        )
    }
}

@Preview( showBackground = true )
@Composable
fun PlaylistTilePreview() {
    PlaylistTile(
        playlistTitle = "Favorites",
        songsInPlaylist = emptyList(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlaySongsInPlaylist = { /*TODO*/ },
        onExportPlaylist = { /*TODO*/ },
        onDeletePlayList = { /*TODO*/ },
        onRenamePlaylist = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onSongsChanged = { /*TODO*/ }) {


    }
}

const val PLAYLIST_TILE_TAG = "PLAYLIST-TILE"
