package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@OptIn( ExperimentalMaterial3Api::class )
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
                  if ( expanded ) {
                      ModalBottomSheet(
                          onDismissRequest = onDismissRequest
                      ) {
                          ArtistOptionsBottomSheetMenu(
                              artist = artist,
                              language = language,
                              fallbackResourceId = fallbackResourceId,
                              onShufflePlay = onShufflePlay,
                              onPlayNext = onPlayNext,
                              onAddToQueue = onAddToQueue,
                              onAddToPlaylist = onAddToPlaylist,
                              onDismissRequest = onDismissRequest
                          )
                      }
                  }
        },
        content = {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onPlay = onPlaySongsByArtist,
        onClick = onClick
    )
}

@Composable
fun ArtistOptionsBottomSheetMenu(
    artist: Artist,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( artist.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = language.artist,
                description = artist.name
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
    }
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