package com.odesa.musicMatters.ui.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.queue.QueueScreenUiState

@Composable
fun SongList(
    sortReverse: Boolean,
    sortSongsBy: SortSongsBy,
    language: Language,
    songs: List<Song>,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onSortReverseChange: ( Boolean ) -> Unit,
    currentlyPlayingSongId: String,
    playSong: ( Song ) -> Unit,
    isFavorite: ( String ) -> Boolean,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    leadingContent: ( LazyListScope.() -> Unit )? = null
) {

    MediaSortBarScaffold(
        mediaSortBar = { 
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortSongsBy,
                sortTypes = SortSongsBy.entries.associateBy( { it }, { it.label( language ) } ),
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

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.drawScrollBar( lazyListState )
                ) {
                    leadingContent?.invoke( this )
                    itemsIndexed( songs ) { index, song ->
                        SongCard(
                            language = language,
                            song = song,
                            isCurrentlyPlaying = currentlyPlayingSongId == song.id,
                            isFavorite = isFavorite( songs[ index ].id ),
                            fallbackResourceId = fallbackResourceId,
                            onClick = { playSong( song ) },
                            onFavorite = { onFavorite( songs[ index ].id ) },
                            onPlayNext = onPlayNext,
                            onAddToQueue = onAddToQueue,
                            onViewArtist = onViewArtist,
                            onViewAlbum = onViewAlbum,
                            onShareSong = onShareSong
                        )
                    }
                }
            }
        }
    }
}

fun SortSongsBy.label( language: Language ) = when ( this ) {
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


@Preview( showSystemUi = true )
@Composable
fun SongListPreview() {
    SongList(
        sortReverse = false,
        sortSongsBy = SortSongsBy.TITLE,
        language = English,
        songs = testSongs,
        fallbackResourceId = R.drawable.placeholder_light,
        onShufflePlay = {},
        onSortTypeChange = {},
        onSortReverseChange = {},
        isFavorite = { true },
        onFavorite = {},
        currentlyPlayingSongId = testSongs.first().id,
        playSong = {},
        onViewAlbum = {},
        onViewArtist = {},
        onShareSong = {},
        onPlayNext = {},
        onAddToQueue = {},
    )
}

@Composable
fun QueueSongList(
    uiState: QueueScreenUiState,
    @DrawableRes fallbackResourceId: Int,
    isFavorite: ( String ) -> Boolean,
    onFavorite: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    playSong: ( Song ) -> Unit,
) {
    when {
        uiState.songsInQueue.isEmpty() -> IconTextBody(
            icon = { modifier ->
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null
                )
            },
            content = {
                Text( uiState.language.damnThisIsSoEmpty )
            }
        )
        else -> {
            val lazyListState = rememberLazyListState()

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.drawScrollBar( lazyListState )
            ) {
                itemsIndexed( uiState.songsInQueue ) { index, song ->
                    QueueSongCard(
                        language = uiState.language,
                        song = song,
                        isCurrentlyPlaying = uiState.currentlyPlayingSongId == song.id,
                        isFavorite = isFavorite( song.id ),
                        fallbackResourceId = fallbackResourceId,
                        onClick = { playSong( song ) },
                        onFavorite = onFavorite,
                        onPlayNext = onPlayNext,
                        onAddToQueue = onAddToQueue,
                        onViewArtist = onViewArtist,
                        onViewAlbum = onViewAlbum,
                        onShareSong = onShareSong,
                        onDragHandleClick = {},
                    )
                }
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun QueueSongListPreview() {
    QueueSongList(
        uiState = emptyQueueScreenUiState,
        fallbackResourceId = R.drawable.placeholder_light,
        isFavorite = { false },
        onFavorite = {},
        onShareSong = {},
        onPlayNext = {},
        playSong = {},
        onAddToQueue = {},
        onViewAlbum = {},
        onViewArtist = {},
    )
}

val emptyQueueScreenUiState = QueueScreenUiState(
    songsInQueue = testSongs,
    language = SettingsDefaults.language,
    currentlyPlayingSongId = testSongs.first().id,
    currentlyPlayingSongIndex = 0,
    themeMode = SettingsDefaults.themeMode,
    favoriteSongIds = emptyList(),
    isLoading = false
)

