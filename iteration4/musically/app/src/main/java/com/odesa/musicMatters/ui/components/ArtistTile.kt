package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.testArtists


@Composable
fun ArtistTile(
    artist: Artist,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsByArtist: () -> Unit,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onClick: () -> Unit,
) {
    Tile(
        modifier = Modifier.fillMaxWidth(),
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( artist.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        options = {
            expanded, onDismissRequest ->
                  ArtistDropdownMenu(
                      expanded = expanded,
                      language = language,
                      onShufflePlay = onShufflePlay,
                      onPlayNext = onPlayNext,
                      onAddToQueue = onAddToQueue,
                      onDismissRequest = onDismissRequest
                  )
        },
        content = {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        onPlay = onPlaySongsByArtist,
        onClick = onClick
    )
}

@Composable
fun ArtistDropdownMenu(
    expanded: Boolean,
    language: Language,
//    playlists: List<Playlist>,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onDismissRequest: () -> Unit,
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
                Text(text = language.shufflePlay )
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
    }
//    if ( showAddToPlaylistDialog ) {
//        AddToPlaylistDialog(
//            playlists = playlists,
//            language = language,
//            fallbackResourceId = fallbackResourceId,
//            onGetSongsInPlaylist =
//        ) {
//
//        }
//    }
}

@Preview( showBackground = true )
@Composable
fun ArtistTilePreview() {
    ArtistTile(
        artist = testArtists.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlaySongsByArtist = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onAddToQueue = { /*TODO*/ },
        onAddToPlaylist = { /*TODO*/ },
        onClick = {}
    )
}