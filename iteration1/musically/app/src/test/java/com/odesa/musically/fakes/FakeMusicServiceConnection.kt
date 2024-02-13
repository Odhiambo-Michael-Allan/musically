package com.odesa.musically.fakes

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.odesa.musically.services.media.MediaItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FakeMusicServiceConnection : MusicServiceConnection {

    private val _isConnected = MutableStateFlow( false )
    override val isConnected = _isConnected.asStateFlow()

    override val playbackState: StateFlow<PlaybackStateCompat>
        get() = TODO("Not yet implemented")

    override val nowPlaying: StateFlow<MediaMetadataCompat>
        get() = TODO("Not yet implemented")

    override val transportControls: MediaControllerCompat.TransportControls
        get() = TODO("Not yet implemented")


    override fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        callback.onChildrenLoaded( parentId, emptyList() )
    }

    override fun unsubscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        TODO("Not yet implemented")
    }

    override fun sendCommand(command: String, parameters: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: (Int, Bundle?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    fun setConnected( connected: Boolean ) {
        _isConnected.value = connected
    }
}

val testMediaItemDataList = listOf(
    MediaItemData(
        mediaId = UUID.randomUUID().toString(),
        title = "The Recipe",
        subtitle = "The Recipe ( feat. Rema )",
        albumArtUri = Uri.parse( "" ),
        browsable = false
    ),
    MediaItemData(
        mediaId = UUID.randomUUID().toString(),
        title = "Over the horizon",
        subtitle = "Samsung",
        albumArtUri = Uri.parse( "" ),
        browsable = false
    ),
    MediaItemData(
        mediaId = UUID.randomUUID().toString(),
        title = "Pebbles - Girlfriend",
        subtitle = "hermit-md",
        albumArtUri = Uri.parse( "" ),
        browsable = false
    ),
    MediaItemData(
        mediaId = UUID.randomUUID().toString(),
        title = "You Right",
        subtitle = "Doja Cat, The Weekend",
        albumArtUri = Uri.parse( "" ),
        browsable = false
    )
)