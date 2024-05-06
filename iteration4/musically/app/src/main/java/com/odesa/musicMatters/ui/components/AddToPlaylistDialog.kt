package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song

@Composable
fun AddToPlaylistDialog(
    playlists: List<Playlist>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onDismissRequest: () -> Unit,
) {
    ScaffoldDialog(
        title = {
            Text( text = language.addToPlaylist )
        },
        content = {
            when {
                playlists.isEmpty() -> SubtleCaptionText( text = language.noInAppPlaylistsFound )
                else -> {
                    LazyColumn (
                        contentPadding = PaddingValues( bottom = 4.dp )
                    ) {
                        items( playlists ) { playlist ->
                            GenericCard(
                                imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
                                    data( onGetSongsInPlaylist( playlist ).firstOrNull { it.artworkUri != null } )
                                    placeholder( fallbackResourceId )
                                    fallback( fallbackResourceId )
                                    error( fallbackResourceId )
                                    crossfade( true )
                                    build()
                                }.build(),
                                title = {
                                    Text( text = playlist.title )
                                },
                                onClick = {
                                    onDismissRequest()
                                }
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview( showBackground = true )
@Composable
fun AddToPlaylistDialogPreview() {
    AddToPlaylistDialog(
        playlists = testPlaylists,
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onGetSongsInPlaylist = { emptyList() },
        onDismissRequest = {}
    )
}