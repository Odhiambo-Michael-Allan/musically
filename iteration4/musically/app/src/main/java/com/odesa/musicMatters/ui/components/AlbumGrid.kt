package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.R
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.AlbumSortBy
import com.odesa.musicMatters.services.media.testAlbums

@Composable
fun AlbumGrid(
    albums: List<Album>,
    language: Language,
    sortType: AlbumSortBy,
    sortReverse: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( AlbumSortBy ) -> Unit,
    onAlbumClick: ( String ) -> Unit,
    onPlayAlbum: ( String ) -> Unit,
) {
    MediaSortBarScaffold(
        mediaSortBar = {
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortType,
                sortTypes = AlbumSortBy.entries.associateBy( { it }, { it.label( language ) } ),
                onSortTypeChange = onSortTypeChange,
                label = {
                    Text(
                        text = language.xAlbums( albums.size.toString() )
                    )
                }
            )
        }
    ) {
        when {
            albums.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Filled.Album,
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
                    items( albums ) {  album ->
                        AlbumTile(
                            album = album,
                            language = language,
                            fallbackResourceId = fallbackResourceId,
                            onPlayAlbum = { onPlayAlbum( album.name ) },
                            onAddToQueue = { /*TODO*/ },
                            onPlayNext = { /*TODO*/ },
                            onShufflePlay = { /*TODO*/ },
                            onViewArtist = {},
                            onClick = { onAlbumClick( album.name ) }
                        )
                    }
                }
            }
        }
    }
}

fun AlbumSortBy.label( language: Language ) = when ( this ) {
    AlbumSortBy.ALBUM_NAME -> language.album
    AlbumSortBy.ARTIST_NAME -> language.artist
    AlbumSortBy.CUSTOM -> language.custom
    AlbumSortBy.TRACKS_COUNT -> language.trackCount
}

@Preview( showSystemUi = true )
@Composable
fun AlbumGridPreview() {
    AlbumGrid(
        albums = testAlbums,
        language = English,
        sortType = AlbumSortBy.ALBUM_NAME,
        sortReverse = false,
        fallbackResourceId = R.drawable.placeholder_light,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onAlbumClick = {},
        onPlayAlbum = {}
    )
}