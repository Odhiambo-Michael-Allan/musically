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
import com.odesa.musicMatters.R
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.queue.QueueScreenUiState
import com.odesa.musicMatters.ui.queue.testQueueScreenUiState

@Composable
fun SongList(
    sortReverse: Boolean,
    sortSongsBy: SortSongsBy,
    language: Language,
    songs: List<Song>,
    playlists: List<Playlist>,
    @DrawableRes fallbackResourceId: Int,
    onShufflePlay: () -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onSortReverseChange: ( Boolean ) -> Unit,
    currentlyPlayingSongId: String,
    playSong: ( Song ) -> Unit,
    isFavorite: ( String ) -> Boolean,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    leadingContent: ( LazyListScope.() -> Unit )? = null
) {

    MediaSortBarScaffold(
        mediaSortBar = { 
            MediaSortBar(
                sortReverse = sortReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = sortSongsBy,
                sortTypes = SortSongsBy.entries.associateBy( { it }, { it.sortSongsByLabel( language ) } ),
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
                            playlists = playlists,
                            fallbackResourceId = fallbackResourceId,
                            onClick = { playSong( song ) },
                            onFavorite = { onFavorite( songs[ index ].id ) },
                            onPlayNext = onPlayNext,
                            onAddToQueue = onAddToQueue,
                            onViewArtist = onViewArtist,
                            onViewAlbum = onViewAlbum,
                            onShareSong = onShareSong,
                            onGetSongsInPlaylist = onGetSongsInPlaylist,
                            onAddSongsToPlaylist = onAddSongsToPlaylist,
                            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                            onCreatePlaylist = onCreatePlaylist
                        )
                    }
                }
            }
        }
    }
}

fun SortSongsBy.sortSongsByLabel(language: Language) = when ( this ) {
    SortSongsBy.CUSTOM -> language.custom
    SortSongsBy.TITLE -> language.title
    SortSongsBy.ARTIST -> language.artist
    SortSongsBy.ALBUM -> language.album
    SortSongsBy.DURATION -> language.duration
    SortSongsBy.DATE_ADDED -> language.dateAdded
    SortSongsBy.COMPOSER -> language.composer
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
        playlists = emptyList(),
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
        onGetSongsInPlaylist = { emptyList() },
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> }
    )
}

@Composable
fun QueueSongList(
    uiState: QueueScreenUiState,
    playlists: List<Playlist>,
    @DrawableRes fallbackResourceId: Int,
    isFavorite: ( String ) -> Boolean,
    onFavorite: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( Song ) -> Unit,
    onAddToQueue: ( Song ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    playSong: ( Song ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongsToPlaylist: ( Playlist, List<Song> ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
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
                itemsIndexed( uiState.songsInQueue ) { _, song ->
                    QueueSongCard(
                        language = uiState.language,
                        song = song,
                        isCurrentlyPlaying = uiState.currentlyPlayingSongId == song.id,
                        isFavorite = isFavorite( song.id ),
                        playlists = playlists,
                        fallbackResourceId = fallbackResourceId,
                        onClick = { playSong( song ) },
                        onFavorite = onFavorite,
                        onPlayNext = onPlayNext,
                        onAddToQueue = onAddToQueue,
                        onViewArtist = onViewArtist,
                        onViewAlbum = onViewAlbum,
                        onShareSong = onShareSong,
                        onGetSongsInPlaylist = onGetSongsInPlaylist,
                        onAddSongsToPlaylist = onAddSongsToPlaylist,
                        onDragHandleClick = {},
                        onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                        onCreatePlaylist = onCreatePlaylist,
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
        uiState = testQueueScreenUiState,
        fallbackResourceId = R.drawable.placeholder_light,
        isFavorite = { false },
        onFavorite = {},
        onShareSong = {},
        onPlayNext = {},
        playSong = {},
        onAddToQueue = {},
        onViewAlbum = {},
        onViewArtist = {},
        playlists = emptyList(),
        onGetSongsInPlaylist = { emptyList() },
        onAddSongsToPlaylist = { _, _ -> },
        onSearchSongsMatchingQuery = { emptyList() },
        onCreatePlaylist = { _, _ -> }
    )
}


