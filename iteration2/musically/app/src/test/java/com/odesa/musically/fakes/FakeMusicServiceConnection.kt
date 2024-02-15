package com.odesa.musically.fakes

import android.media.browse.MediaBrowser.MediaItem.FLAG_PLAYABLE
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.extensions.METADATA_KEY_DATE_MODIFIED
import com.odesa.musically.services.media.extensions.METADATA_KEY_PATH
import com.odesa.musically.services.media.extensions.METADATA_KEY_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class FakeMusicServiceConnection : MusicServiceConnection {

    private val _isConnected = MutableStateFlow( false )
    override val isConnected = _isConnected.asStateFlow()
    override val nowPlayingSong: StateFlow<MediaMetadataCompat>
        get() = TODO("Not yet implemented")
    override val transportControls: MediaControllerCompat.TransportControls
        get() = TODO("Not yet implemented")
    override val playbackState: StateFlow<PlaybackStateCompat>
        get() = TODO("Not yet implemented")

    override fun subscribe( parentId: String, callback: MediaBrowserCompat.SubscriptionCallback ) {
        callback.onChildrenLoaded( parentId, mediaItems )
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

val mediaItems = List( 100 ) {
    val builder = MediaDescriptionCompat.Builder()
    builder.setMediaId( Random.nextLong().toString() )
    builder.setTitle("")
    builder.setDescription("")
    builder.setIconUri(Uri.EMPTY)
    builder.setMediaUri(Uri.EMPTY)
    val extras = Bundle()
    extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "")
    extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "")
    extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "")
    extras.putString(MediaMetadataCompat.METADATA_KEY_GENRE, "")
    extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0L)
    extras.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 0L)
    extras.putLong(MediaMetadataCompat.METADATA_KEY_YEAR, 0L)
    extras.putString(MediaMetadataCompat.METADATA_KEY_COMPOSER, "")
    extras.putLong(METADATA_KEY_DATE_MODIFIED, 0L)
    extras.putString(METADATA_KEY_PATH, "")
    extras.putLong(METADATA_KEY_SIZE, 0L)
    builder.setExtras(extras)
    MediaItem( builder.build(), FLAG_PLAYABLE )
}