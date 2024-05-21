package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.SortAlbumsBy
import com.odesa.musicMatters.services.media.testAlbums

@Composable
fun AlbumGrid(
    albums: List<Album>,
    language: Language,
    sortType: SortAlbumsBy,
    sortReverse: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortAlbumsBy ) -> Unit,
    onAlbumClick: ( String ) -> Unit,
    onPlayAlbum: ( String ) -> Unit,
    onAddToQueue: ( Album ) -> Unit,
    onPlayNext: ( Album ) -> Unit,
    onShufflePlay: ( Album ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddSongsToPlaylist: (Playlist, List<Song> ) -> Unit,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onGetPlaylists: () -> List<Playlist>,
    onGetSongsInAlbum: ( Album ) -> List<Song>,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
) {
    MediaSortBarScaffold(
        mediaSortBar = {
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortType,
                sortTypes = SortAlbumsBy.entries.associateBy( { it }, { it.label( language ) } ),
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
                            modifier = Modifier.fillMaxWidth(),
                            album = album,
                            language = language,
                            fallbackResourceId = fallbackResourceId,
                            onPlayAlbum = { onPlayAlbum( album.name ) },
                            onAddToQueue = { onAddToQueue( album ) },
                            onPlayNext = { onPlayNext( album ) },
                            onShufflePlay = { onShufflePlay( album ) },
                            onViewArtist = onViewArtist,
                            onClick = { onAlbumClick( album.name ) },
                            onAddSongsToPlaylist = onAddSongsToPlaylist,
                            onCreatePlaylist = onCreatePlaylist,
                            onGetPlaylists = onGetPlaylists,
                            onGetSongsInAlbum = onGetSongsInAlbum,
                            onGetSongsInPlaylist = onGetSongsInPlaylist,
                            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                        )
                    }
                }
            }
        }
    }
}

fun SortAlbumsBy.label(language: Language ) = when ( this ) {
    SortAlbumsBy.ALBUM_NAME -> language.album
    SortAlbumsBy.ARTIST_NAME -> language.artist
    SortAlbumsBy.CUSTOM -> language.custom
    SortAlbumsBy.TRACKS_COUNT -> language.trackCount
}

@Preview( showSystemUi = true )
@Composable
fun AlbumGridPreview() {
    AlbumGrid(
        albums = testAlbums,
        language = English,
        sortType = SortAlbumsBy.ALBUM_NAME,
        sortReverse = false,
        fallbackResourceId = R.drawable.placeholder_light,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onAlbumClick = {},
        onPlayAlbum = {},
        onViewArtist = {},
        onPlayNext = {},
        onAddToQueue = {},
        onShufflePlay = {},
        onAddSongsToPlaylist = { _, _ -> },
        onGetSongsInAlbum = { emptyList() },
        onGetPlaylists = { emptyList() },
        onCreatePlaylist = { _, _ ->  },
        onGetSongsInPlaylist = { emptyList() },
        onSearchSongsMatchingQuery = { emptyList() }
    )
}