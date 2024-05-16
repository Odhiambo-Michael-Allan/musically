package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs

@Composable
fun AddToPlaylistDialog(
    song: Song,
    playlists: List<Playlist>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongToPlaylist: ( Playlist ) -> Unit,
    onCreateNewPlaylist: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        BoxWithConstraints {
            val dialogWidth = maxWidth * 0.85f
            val dialogHeight = maxHeight * 0.65f

            Card (
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight),
                shape = RoundedCornerShape( 16.dp )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = language.addToPlaylist,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    HorizontalDivider( thickness = 1.dp )
                    when {
                        playlists.isEmpty() -> SubtleCaptionText( text = language.noInAppPlaylistsFound )
                        else -> {
                            LazyColumn (
                                modifier = Modifier.height( dialogHeight * 0.65f ),
                                contentPadding = PaddingValues( bottom = 4.dp )
                            ) {
                                items( playlists ) { playlist ->
                                    GenericCard(
                                        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
                                            data( onGetSongsInPlaylist( playlist ).firstOrNull { it.artworkUri != null }?.artworkUri )
                                            placeholder( fallbackResourceId )
                                            fallback( fallbackResourceId )
                                            error( fallbackResourceId )
                                            crossfade( true )
                                            build()
                                        }.build(),
                                        title = {
                                            Text( text = playlist.title )
                                        },
                                        imageLabel = {
                                            if ( onGetSongsInPlaylist( playlist ).contains( song ) ) {
                                                Icon(
                                                    modifier = Modifier.size( 16.dp ),
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null
                                                )
                                            }
                                        },
                                        onClick = {
                                            onDismissRequest()
                                            onAddSongToPlaylist( playlist )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row (
                        modifier = Modifier.padding( 8.dp ),
                    ) {
                        TextButton(
                            onClick = onCreateNewPlaylist
                        ) {
                            Text(
                                text = language.newPlaylist,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun AddToPlaylistDialogPreview() {
    AddToPlaylistDialog(
        song = testSongs.first(),
        playlists = testPlaylists.subList( 0, 2 ),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onGetSongsInPlaylist = { emptyList() },
        onAddSongToPlaylist = {},
        onCreateNewPlaylist = {},
        onDismissRequest = {}
    )
}