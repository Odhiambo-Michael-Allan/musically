package com.odesa.musicMatters.services.media.connection

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.MoreExecutors
import com.odesa.musicMatters.utils.move
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Class that manages a connection to a [MediaLibraryService] instance, typically a [MusicService]
 * or one of its subclasses
 *
 * Typically it's best to construct/inject dependencies either using DI or, as Musically does,
 * using an [AppContainer]. There are a few difficulties for that here:
 * - [MediaBrowser] is a final class, so mocking it directly is difficult.
 * - A [MediaBrowserConnectionCallback] is a parameter into the construction of a
 *   [MediaBrowserCompat], and provides callbacks to this class
 * - [MediaBrowserCompat.ConnectionCallback.onConnected] is the best place to construct a
 *   [MediaControllerCompat] that will be used to control the [MediaSessionCompat]
 *
 *   Because of these reasons, rather than constructing additional classes, this is treated as
 *   a black box ( which is why there's very little logic here ).
 *
 *   This is also why the parameters to construct a [MusicServiceConnection] are simple parameters,
 *   rather than private properties. They're only required to build the [MediaBrowserConnectionCallback]
 *   and [MediaBrowserCompat] objects.
 */
class MusicServiceConnectionImpl( context: Context, serviceComponentName: ComponentName )
    : MusicServiceConnection {

    private val _rootMediaItem = MutableStateFlow( MediaItem.EMPTY )
    val rootMediaItem = _rootMediaItem.asStateFlow()

    private val _playbackState = MutableStateFlow( EMPTY_PLAYBACK_STATE )
    override val playbackState = _playbackState.asStateFlow()

    private val _nowPlaying = MutableStateFlow( NOTHING_PLAYING )
    override val nowPlaying = _nowPlaying.asStateFlow()

    override val player: Player?
        get() = browser

    private val _queueSize = MutableStateFlow( 0 )
    override val queueSize = _queueSize.asStateFlow()

    private val _currentPlayingMediaItemIndex = MutableStateFlow(
        player?.currentMediaItemIndex ?: 0
    )
    override val currentlyPlayingMediaItemIndex = _currentPlayingMediaItemIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow( player?.isPlaying ?: false )
    override val isPlaying = _isPlaying.asStateFlow()

    private var browser: MediaBrowser? = null
    private val playerListener: PlayerListener = PlayerListener()

    private val coroutineContext: CoroutineContext = Dispatchers.Main
    private val scope = CoroutineScope( coroutineContext + SupervisorJob() )

    private val _mediaItemsInQueue = MutableStateFlow( emptyList<MediaItem>() )
    override val mediaItemsInQueue = _mediaItemsInQueue.asStateFlow()

    override var isInitialized: Boolean = false
        set( value ) {
            field = value
            if ( value ) {
                synchronized( isInitializedListeners ) {
                    Timber.tag( TAG ).d( "MUSIC SERVICE CONNECTION INITIALIZED. NOTIFYING LISTENERS" )
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


    init {
        scope.launch {
            initializeMediaBrowser( context, serviceComponentName )
        }
    }

    private suspend fun initializeMediaBrowser( context: Context, serviceComponentName: ComponentName ) {
        Timber.tag( TAG ).d( "INITIALIZING MEDIA BROWSER" )
        val newBrowser =
            MediaBrowser.Builder( context, SessionToken( context, serviceComponentName ) )
                .setListener( BrowserListener() )
                .buildAsync()
                .await()
        Timber.tag( TAG ).d( "BROWSER INITIALIZED: $newBrowser" )
        newBrowser.addListener( playerListener )
        browser = newBrowser
        _rootMediaItem.value = newBrowser.getLibraryRoot( /* params = */ null ).await().value!!
        newBrowser.currentMediaItem?.let {
            _nowPlaying.value = it
        }
        isInitialized = true
    }

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        Timber.tag( TAG ).d( "LOADING CHILDREN FOR $parentId" )
        val children = this.browser?.getChildren( parentId, 0, Int.MAX_VALUE, null )
            ?.await()?.value
        Timber.tag( TAG ).d( "NUMBER OF CHILDREN LOADED: ${children?.size}" )
        return  children ?: ImmutableList.of()
    }

    override suspend fun sendCommand( command: String, parameters: Bundle? ): Boolean =
        sendCommand( command, parameters ) { _, _ -> }

    override suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( ( Int, Bundle? ) -> Unit )
    ): Boolean = if ( browser?.isConnected == true ) {
        val args = parameters ?: Bundle()
        browser?.sendCustomCommand( SessionCommand( command, args ), args )?.await()?.let {
            resultCallback( it.resultCode, it.extras )
        }
        true
    } else false

    override fun setPlaybackSpeed( playbackSpeed: Float ) {
        kotlin.runCatching {
            player?.let {
                val currentPitch = it.playbackParameters.pitch
                it.playbackParameters = PlaybackParameters( playbackSpeed, currentPitch )
            }
        }
    }

    override fun setPlaybackPitch( playbackPitch: Float ) {
        kotlin.runCatching {
            player?.let {
                val currentSpeed = it.playbackParameters.speed
                it.playbackParameters = PlaybackParameters( currentSpeed, playbackPitch )
            }
        }
    }

    override fun setRepeatMode( @Player.RepeatMode repeatMode: Int ) {
        kotlin.runCatching {
            player?.let {
                it.repeatMode = repeatMode
            }
        }
    }

    fun release() {
        _rootMediaItem.value = MediaItem.EMPTY
        _nowPlaying.value = NOTHING_PLAYING
        browser?.let {
            it.removeListener( playerListener )
            it.release()
        }
        instance = null
    }

    private fun updatePlaybackState( player: Player ) {
        _playbackState.value = PlaybackState(
            player.playbackState,
            player.playWhenReady,
            player.duration
        )
    }

    private fun updateCurrentlyPlayingMediaItemIndex( player: Player ) {
        _currentPlayingMediaItemIndex.value = player.currentMediaItemIndex
    }

    private fun updateQueueSize( player: Player ) {
        _queueSize.value = player.mediaItemCount
    }

    private fun updateNowPlaying( player: Player ) {
        val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
        if ( mediaItem == MediaItem.EMPTY ) {
            _nowPlaying.value = NOTHING_PLAYING
            return
        }
        val mediaItemFuture = browser!!.getItem( mediaItem.mediaId )
        mediaItemFuture.addListener(
            Runnable {
                val fullMediaItem = mediaItemFuture.get().value ?: return@Runnable
                _nowPlaying.value = mediaItem.buildUpon().setMediaMetadata(
                    fullMediaItem.mediaMetadata
                ).build()
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun updateIsPlaying( isPlaying: Boolean ) {
        _isPlaying.value = isPlaying
    }

    override fun shuffleSongsInQueue() {
        player?.let {
            if ( nowPlaying.value == NOTHING_PLAYING ) return
            it.moveMediaItem( it.currentMediaItemIndex, 0 )
            updateCurrentlyPlayingMediaItemIndex( it )
            val newMediaItemsInQueue = mediaItemsInQueue.value.filter {
                mediaItem ->  mediaItem.mediaId != nowPlaying.value.mediaId
            }.shuffled().toMutableList()
            it.replaceMediaItems( 1, newMediaItemsInQueue.size, newMediaItemsInQueue )
            newMediaItemsInQueue.add( 0, nowPlaying.value )
            _mediaItemsInQueue.value = newMediaItemsInQueue
        }
    }

    override fun moveMediaItem( from: Int, to: Int ) {
        player?.let {
            Timber.tag( TAG ).d( "MOVING MEDIA ITEM FROM POS: $from TO POS: $to" )
            it.moveMediaItem( from, to )
            val newQueue = _mediaItemsInQueue.value.toMutableList()
            newQueue.move( from, to )
            _mediaItemsInQueue.value = newQueue
            updateCurrentlyPlayingMediaItemIndex( it )
        }
    }

    override fun clearQueue() {
        player?.clearMediaItems()
        _mediaItemsInQueue.value = emptyList()
    }

    override fun mediaItemIsPresentInQueue( mediaItem: MediaItem ) = _mediaItemsInQueue.value
        .find { it.mediaId == mediaItem.mediaId }?.let {
            true
        } ?: false

    override fun playNext( mediaItem: MediaItem ) {
        player?.let {
            val newQueue = _mediaItemsInQueue.value.toMutableList()
            if ( mediaItemIsPresentInQueue( mediaItem ) ) {
                moveMediaItem(
                    newQueue.indexOf( mediaItem ),
                    _currentPlayingMediaItemIndex.value + 1
                )
            } else {
                addMediaItemToPlayer( mediaItem, _currentPlayingMediaItemIndex.value + 1 )
            }
        }
    }

    private fun addMediaItemToPlayer( mediaItem: MediaItem, position: Int ) {
        player?.let {
            val newQueue = _mediaItemsInQueue.value.toMutableList()
            it.addMediaItem( position, mediaItem )
            newQueue.add( position, mediaItem )
            _mediaItemsInQueue.value = newQueue
        }
    }

    override suspend fun playMediaItem(
        mediaItem: MediaItem,
        mediaItems: List<MediaItem>,
        shuffle: Boolean,
    ) {
        val player = player ?: return
        val mediaItemsCopy = mediaItems.toMutableList()
        if ( shuffle ) {
            mediaItemsCopy.apply {
                remove( mediaItem )
                shuffle()
                add( 0, mediaItem )
            }
        }
        val indexOfSelectedMediaItem = mediaItemsCopy.indexOf( mediaItem )
        _mediaItemsInQueue.value = mediaItemsCopy
        player.setMediaItems( mediaItemsCopy, indexOfSelectedMediaItem, C.TIME_UNSET )
        player.prepare()
        player.play()
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MusicServiceConnection? = null

        fun getInstance( context: Context, serviceComponentName: ComponentName ) =
            instance ?: synchronized( this ) {
                instance ?: MusicServiceConnectionImpl( context, serviceComponentName )
                    .also { instance = it }
            }
    }

    private inner class BrowserListener : MediaBrowser.Listener {
        override fun onDisconnected( controller : MediaController ) {
            release()
        }
    }

    private inner class PlayerListener : Player.Listener {
        override fun onEvents( player: Player, events: Player.Events ) {
            if ( events.contains( EVENT_PLAY_WHEN_READY_CHANGED )
                || events.contains( EVENT_PLAYBACK_STATE_CHANGED )
                || events.contains( EVENT_MEDIA_ITEM_TRANSITION )
                || events.contains( Player.EVENT_PLAYLIST_METADATA_CHANGED ) ) {
                updatePlaybackState( player )
                updateCurrentlyPlayingMediaItemIndex( player )
                updateQueueSize( player )
            }
            if ( events.contains( Player.EVENT_MEDIA_METADATA_CHANGED )
                || events.contains( EVENT_MEDIA_ITEM_TRANSITION )
                || events.contains( EVENT_PLAY_WHEN_READY_CHANGED ) ) {
                Timber.tag( TAG ).d( "MEDIA METADATA CHANGED EVENT GENERATED" )
                updateNowPlaying( player )
            }
        }

        override fun onIsPlayingChanged( isPlaying: Boolean ) {
            updateIsPlaying( isPlaying )
        }

        override fun onPlayerErrorChanged( error: PlaybackException? ) {
            when ( error?.errorCode ) {
                ERROR_CODE_IO_BAD_HTTP_STATUS,
                    ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE,
                    ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED,
                    ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {}
            }
        }
    }

}

class PlaybackState(
    private val playbackState: Int = Player.STATE_IDLE,
    private val playWhenReady: Boolean = false,
    val duration: Long = C.TIME_UNSET
) {
    val isPlaying: Boolean
        get() {
            return ( playbackState == Player.STATE_BUFFERING
                    || playbackState == Player.STATE_READY )
                    && playWhenReady
        }
}

fun Player.shuffle( shuffle: Boolean ) {
    if ( availableCommands.contains( Player.COMMAND_SET_SHUFFLE_MODE ) ) {
        this.shuffleModeEnabled = shuffle
    } else {
        throw Exception( "SHUFFLE MODE IS NOT ENABLED ON THIS DEVICE" )
    }
}

val EMPTY_PLAYBACK_STATE: PlaybackState = PlaybackState()
val NOTHING_PLAYING: MediaItem = MediaItem.EMPTY
const val TAG = "MUSIC-SERVICE-CONNECTION-TAG"