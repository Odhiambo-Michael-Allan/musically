package com.odesa.musically.ui.components

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
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musically.R
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.testSongs

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SongCard(
    language: Language,
    song: Song,
    isHighlighted: Boolean = false,
    isCurrentlyPlaying: Boolean = false,
    disableHeartIcon: Boolean = true,
    isFavorite: Boolean = false,
    @DrawableRes fallbackResourceId: Int,
    onFavoriteButtonClicked: ( String ) -> Unit,
    onClick: () -> Unit,
    onFavorite: ( String ) -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
) {

    var showSongOptionsMenu by remember { mutableStateOf( false ) }

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
                            .size( 45.dp )
                            .clip( RoundedCornerShape( 10.dp ) ),
                        contentDescription = null
                    )
                }
                Spacer( modifier = Modifier.width( 16.dp ) )
                Column( modifier = Modifier.weight( 1f ) ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = when {
                                isHighlighted || isCurrentlyPlaying -> MaterialTheme.colorScheme.primary
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
                    if ( !disableHeartIcon && isFavorite ) {
                        IconButton(
                            onClick = { onFavoriteButtonClicked( song.id ) }
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
                        onClick = { showSongOptionsMenu = !showSongOptionsMenu }
                    ) {
                        Icon(
                            modifier = Modifier.size( 24.dp ),
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        SongDropdownMenu(
                            language = language,
                            song,
                            isFavorite = isFavorite,
                            expanded = showSongOptionsMenu,
                            onFavorite = onFavorite,
                            onAddToQueue = onAddToQueue,
                            onPlayNext = onPlayNext,
                            onViewArtist = onViewArtist,
                            onViewAlbum = onViewAlbum,
                            onShareSong = onShareSong,
                            onDismissRequest = {
                                showSongOptionsMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SongDropdownMenu(
    language: Language,
    song: Song,
    isFavorite: Boolean,
    expanded: Boolean,
    onFavorite: ( String ) -> Unit,
    onAddToQueue: ( String ) -> Unit,
    onPlayNext: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onDismissRequest: () -> Unit
) {
    var showSongDetailsDialog by remember { mutableStateOf( false ) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf( false ) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = if ( isFavorite ) language.unfavorite else language.favorite
                )
            },
            onClick = {
                onDismissRequest()
                onFavorite( song.id )
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.PlaylistPlay,
                    contentDescription = null
                )
            },
            text = { Text( text = language.playNext ) },
            onClick = {
                onDismissRequest()
                onPlayNext()
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon( imageVector = Icons.Filled.PlaylistPlay, contentDescription = null )
            },
            text = {
                Text(text = language.addToQueue)
            },
            onClick = {
                onDismissRequest()
                onAddToQueue( song.id )
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.PlaylistAdd,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.addToPlaylist )
            },
            onClick = {
                onDismissRequest()
                showAddSongToPlaylistDialog = true
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = "${language.viewArtist}: ${song.artists ?: "<Unknown>"}"
                )
            },
            onClick = {
                onDismissRequest()
//                onViewArtist( song.artists )
            }
        )
        song.albumTitle?.let {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Album,
                        contentDescription = null
                    )
                },
                text = {
                    Text( text = language.viewAlbum )
                },
                onClick = {
                    onDismissRequest()
                    onViewAlbum( it )
                }
            )
        }
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.shareSong )
            },
            onClick = {
                onDismissRequest()
//                onShareSong( song.uri )
            }
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon( imageVector = Icons.Filled.Info, contentDescription = null )
            },
            text = {
                Text( text = language.details )
            },
            onClick = {
                onDismissRequest()
                showSongDetailsDialog = true
            }
        )
    }
    if ( showSongDetailsDialog ) {
//        SongDetailsDialog(
//            song = song,
//            onDismissRequest = { showSongDetailsDialog = false }
//        )
    }
    if ( showAddSongToPlaylistDialog ) {
//        AddToPlaylistDialog(
//            songIds = listOf( song.id ),
//            onDismissRequest = { showAddSongToPlaylistDialog = false }
//        )
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
        disableHeartIcon = false,
        fallbackResourceId = R.drawable.placeholder_light,
        onFavoriteButtonClicked = {},
        onClick = {},
        onFavorite = {},
        onPlayNext = {},
        onAddToQueue = {},
        onViewArtist = {},
        onViewAlbum = {},
        onShareSong = {},
    )
}

@Preview( showBackground = true )
@Composable
fun SongDropdownMenuPreview() {
    SongDropdownMenu(
        language = English,
        song = testSongs.first(),
        isFavorite = true,
        expanded = true,
        onFavorite = {},
        onPlayNext = {},
        onAddToQueue = {},
        onViewArtist = {},
        onViewAlbum = {},
        onShareSong = {},
        onDismissRequest = {}
    )
}