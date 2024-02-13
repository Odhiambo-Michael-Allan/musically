package com.odesa.musically.services.media.connection

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import com.odesa.musically.services.media.connection.MusicServiceConnection.MediaBrowserConnectionCallback
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
class MusicServiceConnection( context: Context, serviceComponentName: ComponentName ) {

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()

    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponentName,
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    private val _isConnected = MutableStateFlow( false )
    val isConnected = _isConnected.asStateFlow()

    fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        Timber.tag( TAG ).d( "SUBSCRIBING TO MEDIA BROWSER FOR ID: $parentId" )
        mediaBrowser.subscribe( parentId, callback )
    }

    fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        mediaBrowser.unsubscribe( parentId, callback )
    }

    private inner class MediaBrowserConnectionCallback :
        MediaBrowserCompat.ConnectionCallback() {

        /**
         * Invoked after [MediaBrowserCompat.connect] when the request has successfully completed.
         */
        override fun onConnected() {
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
}

const val TAG = "MUSIC-SERVICE-CONNECTION-TAG"