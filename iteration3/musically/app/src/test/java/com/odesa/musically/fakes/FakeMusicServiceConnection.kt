package com.odesa.musically.fakes

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.NOTHING_PLAYING
import com.odesa.musically.services.media.connection.PlaybackState
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

    private val playlist = ArrayList<MediaItem>()

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        return testMediaItems
    }

    override suspend fun sendCommand( command: String, parameters: Bundle? ) = true

    override suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( Int, Bundle? ) -> Unit
    ) = true

    fun setNowPlaying( mediaItem: MediaItem ) {
        _nowPlaying.value = mediaItem
    }

    fun setPlaybackState( playbackState: PlaybackState ) {
        _playbackState.value = playbackState
    }

    fun setMediaItems( mediaItemList: List<MediaItem> ) {
        playlist.addAll( mediaItemList )
        _queueSize.value = mediaItemList.size
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