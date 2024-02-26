package com.odesa.musically.services.media.connection

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
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
        player?.currentMediaItemIndex ?: 0 )
    override val currentlyPlayingMediaItemIndex = _currentPlayingMediaItemIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow( player?.isPlaying ?: false )
    override val isPlaying = _isPlaying.asStateFlow()

    private var browser: MediaBrowser? = null
    private val playerListener: PlayerListener = PlayerListener()

    private val coroutineContext: CoroutineContext = Dispatchers.Main
    private val scope = CoroutineScope( coroutineContext + SupervisorJob() )

    init {
        scope.launch {
            initializeMediaBrowser( context, serviceComponentName )
        }
    }

    private suspend fun initializeMediaBrowser( context: Context, serviceComponentName: ComponentName ) {
        val newBrowser =
            MediaBrowser.Builder( context, SessionToken( context, serviceComponentName ) )
                .setListener( BrowserListener() )
                .buildAsync()
                .await()
        newBrowser.addListener( playerListener )
        browser = newBrowser
        _rootMediaItem.value = newBrowser.getLibraryRoot( /* params = */ null ).await().value!!
        newBrowser.currentMediaItem?.let {
            _nowPlaying.value = it
        }
    }

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> {
        return this.browser?.getChildren( parentId, 0, Int.MAX_VALUE, null )
            ?.await()?.value ?: ImmutableList.of()
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

    private fun updateNowPlaying( player: Player ) {
        val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
        if ( mediaItem == MediaItem.EMPTY )
            return
        val mediaItemFuture = browser!!.getItem( mediaItem.mediaId )
        mediaItemFuture.addListener(
            Runnable {
                val fullMediaItem = mediaItemFuture.get().value ?: return@Runnable
                _nowPlaying.value = mediaItem.buildUpon().setMediaMetadata(
                    fullMediaItem.mediaMetadata ).build()
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun updateIsPlaying() {
        _isPlaying.value = player?.isPlaying ?: false
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
                || events.contains( EVENT_MEDIA_ITEM_TRANSITION ) ) {
                updatePlaybackState( player )
                updateIsPlaying()
            }
            if ( events.contains( Player.EVENT_MEDIA_METADATA_CHANGED )
                || events.contains( EVENT_MEDIA_ITEM_TRANSITION )
                || events.contains( EVENT_PLAY_WHEN_READY_CHANGED ) ) {
                updateNowPlaying( player )
            }
            if ( events.contains( Player.EVENT_PLAYLIST_METADATA_CHANGED )
                || events.contains( Player.EVENT_MEDIA_METADATA_CHANGED ) ) {
                _queueSize.value = player.mediaItemCount
                _currentPlayingMediaItemIndex.value = player.currentMediaItemIndex
            }
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

val EMPTY_PLAYBACK_STATE: PlaybackState = PlaybackState()
val NOTHING_PLAYING: MediaItem = MediaItem.EMPTY
private const val POSITION_UPDATE_INTERVAL_MILLIS = 1L

const val TAG = "MUSIC-SERVICE-CONNECTION-TAG"
const val MEDIA_CONTROLLER_TAG = "MEDIA-CONTROLLER"