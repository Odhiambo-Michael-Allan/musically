package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.SortPlaylistsBy
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song

@Composable
fun PlaylistGrid(
    playlists: List<Playlist>,
    language: Language,
    sortType: SortPlaylistsBy,
    sortReverse: Boolean,
    @DrawableRes fallbackResourceId: Int,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortPlaylistsBy ) -> Unit,
    onPlaylistClick: ( String, String ) -> Unit,
    onPlaySongsInPlaylist: ( Playlist ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
) {
    MediaSortBarScaffold(
        mediaSortBar = {
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortType,
                sortTypes = SortPlaylistsBy.entries.associateBy({ it }, { it.label(language) }),
                onSortTypeChange = onSortTypeChange,
                label = {
                    Text(
                        text = language.xPlaylists( playlists.size.toString() )
                    )
                }
            )
        }
    ) {
        when {
            playlists.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = null
                    )
                }
            ) {
                Text( text = language.damnThisIsSoEmpty )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive( minSize = 128.dp ),
                    contentPadding = PaddingValues( start = 8.dp, end = 8.dp,
                        top = 8.dp, bottom = 150.dp )
                ) {
                    items( playlists ) {
                        PlaylistTile(
                            playlistTitle = it.title,
                            songsInPlaylist = onGetSongsInPlaylist( it ),
                            language = language,
                            fallbackResourceId = fallbackResourceId,
                            onPlaySongsInPlaylist = { onPlaySongsInPlaylist( it ) },
                            onExportPlaylist = { /*TODO*/ },
                            onDeletePlayList = { /*TODO*/ },
                            onRenamePlaylist = { /*TODO*/ },
                            onPlayNext = { /*TODO*/ },
                            onShufflePlay = { /*TODO*/ },
                            onSongsChanged = {},
                            onPlaylistClick = { onPlaylistClick( it.id, it.title ) }
                        )
                    }
                }
            }
        }
    }
}

fun SortPlaylistsBy.label(language: Language ) = when ( this ) {
    SortPlaylistsBy.TITLE -> language.title
    SortPlaylistsBy.CUSTOM -> language.custom
    SortPlaylistsBy.TRACKS_COUNT -> language.trackCount
}

@Preview( showSystemUi = true )
@Composable
fun PlaylistGridPreview() {
    PlaylistGrid(
        playlists = testPlaylists,
        language = English,
        sortType = SortPlaylistsBy.CUSTOM,
        sortReverse = false,
        fallbackResourceId = R.drawable.placeholder_light,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onPlaylistClick = { _, _ -> },
        onPlaySongsInPlaylist = {},
        onGetSongsInPlaylist = { emptyList() }
    )
}