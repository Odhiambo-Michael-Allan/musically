package com.odesa.musicMatters.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.odesa.musicMatters.services.media.Album

@Composable
fun AlbumRow(
    albums: List<Album>
) {
    BoxWithConstraints {
        val maxSize = min( maxHeight, maxWidth ).div( 2f )
        val width = min( maxSize, 200.dp )

        LazyRow {
            items( albums ) {
                Box(
                    modifier = Modifier.width( width )
                ) {
                    AlbumTile(
                        album = it,
                        language = language,
                        fallbackResourceId = fallbackResourceId,
                        onPlayAlbum = { onPlaySongsInAlbum( it ) },
                        onAddToQueue = { /*TODO*/ },
                        onPlayNext = { /*TODO*/ },
                        onShufflePlay = { /*TODO*/ },
                        onViewArtist =
                    )
                }
            }
        }
    }
}