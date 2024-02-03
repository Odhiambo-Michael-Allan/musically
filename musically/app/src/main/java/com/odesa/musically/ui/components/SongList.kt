package com.odesa.musically.ui.components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.R
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.songs.impl.testSongs
import com.odesa.musically.services.audio.Song
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language

@Composable
fun SongList(
    sortReverse: Boolean,
    onSortReverseChange: ( Boolean ) -> Unit,
    sortType: SortSongsBy,
    onSortTypeChange: (SortSongsBy ) -> Unit,
    language: Language,
    songs: List<Song>,
    onShufflePlay: () -> Unit,
    @DrawableRes fallbackResourceId: Int,
) {

    MediaSortBarScaffold(
        mediaSortBar = { 
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortType,
                sortTypes = SortSongsBy.entries.associateBy( { it }, { it.label( language ) }),
                onSortTypeChange = onSortTypeChange,
                label = {
                    Text( text = language.xSongs( songs.size.toString() ) )
                },
                onShufflePlay = onShufflePlay
            )
        }
    ) {
        when {
            songs.isEmpty() -> IconTextBody(
                icon = { modifier ->
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = null
                    )
                },
                content = {
                    Text( language.damnThisIsSoEmpty )
                }
            )
            else -> {
                val lazyListState = rememberLazyListState()
                var highlightedCardPosition by remember { mutableIntStateOf( -1 ) }

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.drawScrollBar( lazyListState )
                ) {
                    itemsIndexed( songs ) { index, song ->
                        SongCard(
                            language = language,
                            song = song,
                            isHighlighted = highlightedCardPosition == index,
                            onFavoriteButtonClicked = {},
                            fallbackResourceId = fallbackResourceId,
                            onClick = {
                                highlightedCardPosition = index
                            },
                            onFavorite = {},
                            onPlayNext = { /*TODO*/ },
                            onAddToQueue = {},
                            onViewArtist = {},
                            onViewAlbum = {},
                            onShareSong = {}
                        )
                    }
                }
            }
        }
    }
}

fun SortSongsBy.label(language: Language ) = when ( this ) {
    SortSongsBy.CUSTOM -> language.custom
    SortSongsBy.TITLE -> language.title
    SortSongsBy.ARTIST -> language.artist
    SortSongsBy.ALBUM -> language.album
    SortSongsBy.DURATION -> language.duration
    SortSongsBy.DATE_ADDED -> language.dateAdded
    SortSongsBy.DATE_MODIFIED -> language.lastModified
    SortSongsBy.COMPOSER -> language.composer
    SortSongsBy.ALBUM_ARTIST -> language.albumArtist
    SortSongsBy.YEAR -> language.year
    SortSongsBy.FILENAME -> language.filename
    SortSongsBy.TRACK_NUMBER -> language.trackNumber
}
@RequiresApi( Build.VERSION_CODES.O )
@Preview( showSystemUi = true )
@Composable
fun SongListPreview() {
    SongList(
        sortReverse = false,
        onSortReverseChange = {},
        sortType = SortSongsBy.TITLE,
        onSortTypeChange = {},
        language = English,
        songs = testSongs,
        onShufflePlay = {},
        fallbackResourceId = R.drawable.placeholder_light
    )
}

