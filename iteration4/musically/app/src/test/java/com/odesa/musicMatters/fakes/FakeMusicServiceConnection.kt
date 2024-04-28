package com.odesa.musicMatters.fakes

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.google.common.collect.ImmutableList
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.NOTHING_PLAYING
import com.odesa.musicMatters.services.media.connection.PlaybackState
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_RECENT_SONGS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
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

    override var isInitialized = false
        set( value ) {
            field = value
            if ( value ) {
                synchronized( isInitializedListeners ) {
                    isInitializedListeners.forEach {
                        it.invoke()
                    }
                }
            }
        }
    override val isInitializedListeners: MutableList<() -> Unit> = mutableListOf()

    override fun runWhenInitialized( fn: () -> Unit ) {
        if ( isInitialized ) fn.invoke() else isInitializedListeners.add( fn )
    }

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        return when ( parentId ) {
            MUSIC_MATTERS_TRACKS_ROOT -> trackList
            MUSIC_MATTERS_RECENT_SONGS_ROOT -> trackList.subList( 0, 5 )
            MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT -> albumList
            MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT -> artistList
            else -> genreList
        }
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
val id4 = UUID.randomUUID().toString()
val id5 = UUID.randomUUID().toString()
val id6 = UUID.randomUUID().toString()

val testMediaItems: ImmutableList<MediaItem> = ImmutableList.of(
    MediaItem.Builder().setMediaId( id1 ).build(),
    MediaItem.Builder().setMediaId( id2 ).build(),
    MediaItem.Builder().setMediaId( id3 ).build()
)

val genreList: ImmutableList<MediaItem> = ImmutableList.of(
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Pop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "RnB" )
            }.build()
        )
    }.build()
)

val trackList: ImmutableList<MediaItem> = ImmutableList.of(
    MediaItem.Builder().apply {
        setMediaId( id1 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "You Right" ).setGenre( "RnB" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id2 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Best Man" ).setGenre( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id3 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Huncho Jack" ).setGenre( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id4 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Lean on" ).setGenre( "Dance" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id5 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Scared To Be Lonely" ).setGenre( "House" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id6 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "High off life" )
                    .setGenre( "House" )
            }.build()
        )
    }.build(),
)

val albumList: ImmutableList<MediaItem> = ImmutableList.of (
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller1" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller2" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller3" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller4" )
            }.build()
        )
    }.build()
)

val artistList: ImmutableList<MediaItem> = ImmutableList.of (
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist1" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist2" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist3" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist4" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist5" )
            }.build()
        )
    }.build()
)

