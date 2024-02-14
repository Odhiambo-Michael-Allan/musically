package com.odesa.musically.services.media.connection

import android.support.v4.media.MediaBrowserCompat
import kotlinx.coroutines.flow.StateFlow

interface MusicServiceConnection {
    val isConnected: StateFlow<Boolean>
    fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback )
    fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback )
}