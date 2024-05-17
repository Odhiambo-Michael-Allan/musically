package com.odesa.musicMatters.services.media.connection

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Genre
import com.odesa.musicMatters.services.media.Song
import kotlinx.coroutines.flow.StateFlow

interface MusicServiceConnection {
    val isInitializing: StateFlow<Boolean>
    val nowPlaying: StateFlow<MediaItem>
    val playbackState: StateFlow<PlaybackState>
    val currentlyPlayingMediaItemIndex: StateFlow<Int>
    val isPlaying: StateFlow<Boolean>
    val player: Player?
    val mediaItemsInQueue: StateFlow<List<MediaItem>>
    val cachedSongs: StateFlow<List<Song>>
    val cachedGenres: StateFlow<List<Genre>>
    val cachedRecentlyAddedSongs: StateFlow<List<Song>>
    val cachedArtists: StateFlow<List<Artist>>
    val cachedSuggestedArtists: StateFlow<List<Artist>>
    val cachedAlbums: StateFlow<List<Album>>
    val cachedSuggestedAlbums: StateFlow<List<Album>>
    suspend fun playMediaItem(
        mediaItem: MediaItem,
        mediaItems: List<MediaItem>,
        shuffle: Boolean,
    )
    fun setPlaybackSpeed( playbackSpeed: Float )
    fun setPlaybackPitch( playbackPitch: Float )
    fun setRepeatMode( @Player.RepeatMode repeatMode: Int )
    fun shuffleSongsInQueue()
    fun moveMediaItem( from: Int, to: Int )
    fun clearQueue()
    fun mediaItemIsPresentInQueue( mediaItem: MediaItem ): Boolean
    fun playNext( mediaItem: MediaItem )
    fun addToQueue( mediaItem: MediaItem )
    fun searchSongsMatching( query: String ): List<Song>
}