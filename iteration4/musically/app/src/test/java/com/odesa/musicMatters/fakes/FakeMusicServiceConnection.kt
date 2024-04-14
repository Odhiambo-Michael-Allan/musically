package com.odesa.musicMatters.fakes

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.NOTHING_PLAYING
import com.odesa.musicMatters.services.media.connection.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID


class FakeMusicServiceConnection : MusicServiceConnection {

    private val _nowPlaying = MutableStateFlow( NOTHING_PLAYING )
    override val nowPlaying = _nowPlaying.asStateFlow()

    private val _playbackState = MutableStateFlow( PlaybackState() )
    override val playbackState = _playbackState.asStateFlow()

    private val _queueSize = MutableStateFlow( 0 )
    override val queueSize = _queueSize.asStateFlow()

    private val _currentlyPlayingMediaItemIndex = MutableStateFlow( 0 )
    override val currentlyPlayingMediaItemIndex = _currentlyPlayingMediaItemIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow( false )
    override val isPlaying = _isPlaying.asStateFlow()

    override val player: Player? = null

    private val _mediaItemsInQueue = MutableStateFlow( emptyList<MediaItem>() )
    override val mediaItemsInQueue = _mediaItemsInQueue.asStateFlow()

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        return testMediaItems
    }

    override suspend fun sendCommand( command: String, parameters: Bundle? ) = true

    override suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( Int, Bundle? ) -> Unit
    ) = true

    override suspend fun playMediaItem(
        mediaItem: MediaItem,
        mediaItems: List<MediaItem>,
        shuffle: Boolean ) {}

    override fun setPlaybackSpeed( playbackSpeed: Float ) {}

    override fun setPlaybackPitch( playbackPitch: Float ) {}
    override fun setRepeatMode( repeatMode: Int ) {}
    override fun shuffleSongsInQueue() {}
    override fun moveMediaItem(from: Int, to: Int) {}
    override fun clearQueue() {
        _mediaItemsInQueue.value = emptyList()
    }

    fun setNowPlaying( mediaItem: MediaItem ) {
        _nowPlaying.value = mediaItem
    }

    fun setPlaybackState( playbackState: PlaybackState ) {
        _playbackState.value = playbackState
    }

    fun setMediaItems( mediaItemList: List<MediaItem> ) {
        _mediaItemsInQueue.value = mediaItemList
        _queueSize.value = _mediaItemsInQueue.value.size
    }

    fun setCurrentMediaItemIndex( index: Int ) {
        _currentlyPlayingMediaItemIndex.value = index
    }

    fun setIsPlaying( isPlaying: Boolean ) {
        _isPlaying.value = isPlaying
    }

}

val id1 = UUID.randomUUID().toString()
val id2 = UUID.randomUUID().toString()
val id3 = UUID.randomUUID().toString()

val testMediaItems: ImmutableList<MediaItem> = ImmutableList.of(
    MediaItem.Builder().setMediaId( id1 ).build(),
    MediaItem.Builder().setMediaId( id2 ).build(),
    MediaItem.Builder().setMediaId( id3 ).build()
)