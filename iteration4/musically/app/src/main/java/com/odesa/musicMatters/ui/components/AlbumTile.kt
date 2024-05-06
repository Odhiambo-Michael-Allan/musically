package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.testAlbums

@Composable
fun AlbumTile(
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlayAlbum: () -> Unit,
    onAddToQueue: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onViewArtist: (String ) -> Unit,
    onClick: () -> Unit
) {
    Tile(
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( album.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        options = {
            expanded, onDismissRequest ->
                  AlbumDropdownMenu(
                      album = album,
                      language = language,
                      expanded = expanded,
                      onAddToQueue = onAddToQueue,
                      onPlayNext = onPlayNext,
                      onShufflePlay = onShufflePlay,
                      onViewArtist = onViewArtist,
                      onDismissRequest = onDismissRequest
                  )
        },
        content = {
            Text(
                text = album.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if ( album.artists.isNotEmpty() ) {
                Text(
                    album.artists.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onPlay = onPlayAlbum,
        onClick = onClick
    )
}

@Composable
fun AlbumDropdownMenu(
    album: Album,
    language: Language,
    expanded: Boolean,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onDismissRequest: () -> Unit
) {
    var showAddToPlaylistDialog by remember { mutableStateOf( false ) }

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
                    imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                    contentDescription = null
                )
            },
            text = {
                Text( text = language.addToQueue )
            },
            onClick = {
                onDismissRequest()
                onAddToQueue()
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
        album.artists.forEach { artistName ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                text = {
                    Text( text = "${language.viewArtist}: $artistName" )
                },
                onClick = {
                    onDismissRequest()
                    onViewArtist( artistName )
                }
            )
        }
    }
//    if ( showAddToPlaylistDialog ) {
//        AddToPlaylistDialog
//    }
}

@Preview( showBackground = true )
@Composable
fun AlbumTilePreview() {
    AlbumTile(
        album = testAlbums.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlayAlbum = { /*TODO*/ },
        onAddToQueue = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onViewArtist = {}
    ) {}
}