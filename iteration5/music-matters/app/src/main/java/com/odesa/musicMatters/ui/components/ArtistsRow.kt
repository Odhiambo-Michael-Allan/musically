package com.odesa.musicMatters.ui.components;

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist

@Composable
fun ArtistsRow(
    modifier: Modifier = Modifier,
    artists: List<Artist>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsByArtist: ( Artist ) -> Unit,
    onAddToQueue: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onViewArtist: ( Artist ) -> Unit,
) {
    BoxWithConstraints {
        val maxSize = min( maxHeight, maxWidth ).div( 2f )
        val width = min( maxSize, 200.dp )

        LazyRow (
            modifier = modifier,
            contentPadding = PaddingValues( 8.dp, 0.dp )
        ) {
            items( artists ) {
                Box(
                    modifier = Modifier.width( width.minus( 15.dp ) )
                ) {
                    ArtistTile(
                        artist = it,
                        language = language,
                        fallbackResourceId = fallbackResourceId,
                        onPlaySongsByArtist = {},
                        onShufflePlay = onShufflePlay,
                        onPlayNext = onPlayNext,
                        onAddToQueue = onAddToQueue,
                        onAddToPlaylist = {},
                        onClick = { onViewArtist( it ) }
                    )
                }
            }
        }
    }
}

