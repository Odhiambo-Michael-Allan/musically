package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Person
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
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.testAlbums

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumTile(
    modifier: Modifier,
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlayAlbum: () -> Unit,
    onAddToQueue: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onClick: () -> Unit
) {
    Tile(
        modifier = modifier,
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( album.artworkUri )
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
                          AlbumOptionsBottomSheetMenu(
                              album = album,
                              language = language,
                              fallbackResourceId = fallbackResourceId,
                              onShufflePlay = onShufflePlay,
                              onPlayNext = onPlayNext,
                              onAddToQueue = onAddToQueue,
                              onAddToPlaylist = onAddToPlaylist,
                              onViewArtist = onViewArtist,
                              onDismissRequest = onDismissRequest
                          )
                      }
                  }
        },
        content = {
            Text(
                text = album.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if ( album.artists.isNotEmpty() ) {
                Text(
                    album.artists.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onPlay = onPlayAlbum,
        onClick = onClick
    )
}

@Composable
fun AlbumOptionsBottomSheetMenu(
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToQueue: () -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddToPlaylist: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BottomSheetMenuContent(
        bottomSheetHeader = {
            BottomSheetMenuHeader(
                headerImage = ImageRequest.Builder( LocalContext.current ).apply {
                    data( album.artworkUri )
                    placeholder( fallbackResourceId )
                    fallback( fallbackResourceId )
                    error( fallbackResourceId )
                    crossfade( true )
                }.build(),
                title = album.name,
                description = album.artists.joinToString()
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
        album.artists.forEach {
            BottomSheetMenuItem(
                imageVector = Icons.Filled.Person,
                label = "${language.viewArtist}: $it"
            ) {
                onDismissRequest()
                onViewArtist( it )
            }
        }
    }
}


@Preview( showSystemUi = true )
@Composable
fun AlbumTilePreview() {
    AlbumTile(
        modifier = Modifier.fillMaxWidth(),
        album = testAlbums.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onPlayAlbum = { /*TODO*/ },
        onAddToQueue = { /*TODO*/ },
        onPlayNext = { /*TODO*/ },
        onShufflePlay = { /*TODO*/ },
        onAddToPlaylist = {},
        onViewArtist = {}
    ) {}
}