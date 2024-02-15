package com.odesa.musically.services.media.connection

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.odesa.musically.services.media.connection.MusicServiceConnectionImpl.MediaBrowserConnectionCallback
import com.odesa.musically.services.media.extensions.UNKNOWN_LONG_VALUE
import com.odesa.musically.services.media.extensions.UNKNOWN_STRING_VALUE
import com.odesa.musically.services.media.extensions.id
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Default class that manages a connection to a [MediaBrowserServiceCompat] instance, typically a
 * [MusicService] or one of its subclasses.
 *
 * Typically it's best to construct/inject dependencies either using DI or, as Musically does,
 * using an [AppContainer]. There are a few difficulties for that here:
 * - [MediaBrowserCompat] is a final class, so mocking it directly is difficult.
 * - A [MediaBrowserConnectionCallback] is a parameter into the construction of a
 *   [MediaBrowserCompat], and provides callbacks to this class
 * - [MediaBrowserCompat.ConnectionCallback.onConnected] is the best place to construct a
 *   [MediaControllerCompat] that will be used to control the [MediaSessionCompat]
 */
class MusicServiceConnectionImpl( private val context: Context, serviceComponentName: ComponentName )
    : MusicServiceConnection {

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()

    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponentName,
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    private val _isConnected = MutableStateFlow( false )
    override val isConnected = _isConnected.asStateFlow()

    private val _nowPlayingSong = MutableStateFlow( NOTHING_PLAYING )
    override val nowPlayingSong = _nowPlayingSong.asStateFlow()

    private val _playbackState = MutableStateFlow( EMPTY_PLAYBACK_STATE )
    override val playbackState = _playbackState.asStateFlow()

    override val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private lateinit var mediaController: MediaControllerCompat

    override fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        Timber.tag( TAG ).d( "SUBSCRIBING TO MEDIA BROWSER FOR ID: $parentId" )
        mediaBrowser.subscribe( parentId, callback )
    }

    override fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        Timber.tag( TAG ).d( "UNSUBSCRIBING TO MEDIA BROWSER FOR ID: $parentId" )
        mediaBrowser.unsubscribe( parentId, callback )
    }

    override fun sendCommand(command: String, parameters: Bundle?) =
        sendCommand( command, parameters ) { _, _, -> }

    override fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ( Int, Bundle? ) -> Unit
    ) = if ( mediaBrowser.isConnected ) {
        Timber.tag( TAG ).d( "SENDING COMMAND: $command" )
        mediaController.sendCommand( command, parameters, object : ResultReceiver( Handler() ) {
            override fun onReceiveResult( resultCode: Int, resultData: Bundle? ) {
                resultCallback( resultCode, resultData )
            }
        } )
        true
    } else false

    private inner class MediaBrowserConnectionCallback :
        MediaBrowserCompat.ConnectionCallback() {

        /**
         * Invoked after [MediaBrowserCompat.connect] when the request has successfully completed.
         */
        override fun onConnected() {
            // Get a media controller for the media session.
            mediaController = MediaControllerCompat( context, mediaBrowser.sessionToken ).apply {
                registerCallback( MediaControllerCallback() )
            }
            _isConnected.value = true

        }

        /**
         * Invoked when the client is disconnected from the media browser
         */
        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        /**
         * Invoked when the connected to the media browser failed
         */
        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged( state: PlaybackStateCompat? ) {
            Timber.tag( MEDIA_CONTROLLER_TAG ).d( "ON-PLAYBACK-STATE-CHANGED CALLBACK INVOKED" )
            _playbackState.value = state ?: EMPTY_PLAYBACK_STATE
        }

        override fun onMetadataChanged( metadata: MediaMetadataCompat? ) {
            // When ExoPlayer stops we will receive a callback with "empty" metadata. This is a
            // metadata object which has been instantiated with default values. The default value
            // for media ID is null so we assume that if this value is null we are not playing
            // anything.
            Timber.tag( MEDIA_CONTROLLER_TAG ).d( "ON-METADATA-CHANGED CALLBACK INVOKED" )
            _nowPlayingSong.value = if ( metadata?.id == null ) NOTHING_PLAYING else metadata
        }

        override fun onQueueChanged( queue: MutableList<MediaSessionCompat.QueueItem>? ) {}

        override fun onSessionEvent( event: String?, extras: Bundle?) {
            super.onSessionEvent( event, extras )
        }

        /**
         * Normally if a [MediaBrowserServiceCompat] drops its connection the callback comes via
         * [MediaControllerCompat.Callback] ( here ). But since other connection status events
         * are sent to [MediaBrowserCompat.ConnectionCallback], we catch the disconnect here
         * and send it on to the other callback
         */
        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState( PlaybackStateCompat.STATE_NONE, 0, 0f )
    .build()

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString( MediaMetadataCompat.METADATA_KEY_MEDIA_ID, UNKNOWN_STRING_VALUE )
    .putLong( MediaMetadataCompat.METADATA_KEY_DURATION, UNKNOWN_LONG_VALUE )
    .build()

const val TAG = "MUSIC-SERVICE-CONNECTION-TAG"
const val MEDIA_CONTROLLER_TAG = "MEDIA-CONTROLLER"