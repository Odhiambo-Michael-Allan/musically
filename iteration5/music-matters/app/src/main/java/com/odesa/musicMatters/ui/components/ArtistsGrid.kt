package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortArtistsBy
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.testArtists

@Composable
fun ArtistsGrid(
    artists: List<Artist>,
    language: Language,
    sortBy: SortArtistsBy,
    sortReverse: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortArtistsBy ) -> Unit,
    onArtistClick: ( String ) -> Unit,
    onPlaySongsByArtist: ( Artist ) -> Unit,
    onShufflePlay: ( Artist ) -> Unit,
    onAddToQueue: ( Artist ) -> Unit,
    onPlayNext: ( Artist ) -> Unit,
    onAddToPlaylist: ( Artist ) -> Unit,
) {
    MediaSortBarScaffold(
        mediaSortBar = {
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortBy,
                sortTypes = SortArtistsBy.entries.associateBy({ it }, { it.label(language) }),
                onSortTypeChange = onSortTypeChange,
                label = {
                    Text( text = language.xArtists( artists.size.toString() ) )
                }
            )
        }
    ) {
        when {
            artists.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                }
            ) {
                Text( text = language.damnThisIsSoEmpty )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive( minSize = 128.dp ),
                    contentPadding = PaddingValues( 8.dp )
                ) {
                    items( artists ) { artist ->
                        ArtistTile(
                            artist = artist,
                            language = language,
                            fallbackResourceId = fallbackResourceId,
                            onPlaySongsByArtist = { onPlaySongsByArtist( artist ) },
                            onShufflePlay = { onShufflePlay( artist ) },
                            onPlayNext = { onPlayNext( artist ) },
                            onAddToQueue = { onAddToQueue( artist ) },
                            onAddToPlaylist = { onAddToPlaylist( artist ) },
                            onClick = { onArtistClick( artist.name ) }
                        )
                    }
                }
            }
        }
    }
}

fun SortArtistsBy.label( language: Language ) = when ( this ) {
    SortArtistsBy.ARTIST_NAME -> language.name
    SortArtistsBy.CUSTOM -> language.custom
    SortArtistsBy.TRACKS_COUNT -> language.trackCount
    SortArtistsBy.ALBUMS_COUNT -> language.albumCount
}

@Preview( showSystemUi = true )
@Composable
fun ArtistsGridPreview() {
    ArtistsGrid(
        artists = testArtists,
        language = English,
        sortBy = SortArtistsBy.ARTIST_NAME,
        sortReverse = false,
        fallbackResourceId = R.drawable.placeholder_light,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onArtistClick = {},
        onPlaySongsByArtist = {},
        onAddToQueue = {},
        onAddToPlaylist = {},
        onShufflePlay = {},
        onPlayNext = {}
    )
}