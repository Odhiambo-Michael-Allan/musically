package com.odesa.musicMatters.services.media.connection

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface MusicServiceConnection {
    val nowPlaying: StateFlow<MediaItem>
    val playbackState: StateFlow<PlaybackState>
    val currentlyPlayingMediaItemIndex: StateFlow<Int>
    val isPlaying: StateFlow<Boolean>
    val player: Player?
    val mediaItemsInQueue: StateFlow<List<MediaItem>>
    var isInitialized: Boolean
    val isInitializedListeners: MutableList<() -> Unit>
    fun runWhenInitialized( fn: () -> Unit )
    suspend fun getChildren( parentId: String ): ImmutableList<MediaItem>
    suspend fun sendCommand( command: String, parameters: Bundle? ) : Boolean
    suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( ( Int, Bundle? ) -> Unit ) ) : Boolean
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
}