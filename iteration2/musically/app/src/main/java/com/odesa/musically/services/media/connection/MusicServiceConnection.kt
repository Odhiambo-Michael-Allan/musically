package com.odesa.musically.services.media.connection

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import kotlinx.coroutines.flow.StateFlow

interface MusicServiceConnection {
    val isConnected: StateFlow<Boolean>
    val nowPlayingSong: StateFlow<MediaMetadataCompat>
    val transportControls: MediaControllerCompat.TransportControls
    val playbackState: StateFlow<PlaybackStateCompat>
    fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback )
    fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback )
    fun sendCommand( command: String, parameters: Bundle? ) : Boolean
    fun sendCommand( command: String,
                     parameters: Bundle?,
                     resultCallback: ( ( Int, Bundle? ) -> Unit ) ) : Boolean
}