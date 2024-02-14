package com.odesa.musically.services.media

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.odesa.musically.services.PermissionsManager
import com.odesa.musically.services.media.extensions.METADATA_KEY_DATE_MODIFIED
import com.odesa.musically.services.media.extensions.METADATA_KEY_PATH
import com.odesa.musically.services.media.extensions.METADATA_KEY_SIZE
import com.odesa.musically.services.media.extensions.album
import com.odesa.musically.services.media.extensions.albumArtist
import com.odesa.musically.services.media.extensions.artist
import com.odesa.musically.services.media.extensions.artworkUri
import com.odesa.musically.services.media.extensions.composer
import com.odesa.musically.services.media.extensions.dateModified
import com.odesa.musically.services.media.extensions.duration
import com.odesa.musically.services.media.extensions.flag
import com.odesa.musically.services.media.extensions.genre
import com.odesa.musically.services.media.extensions.id
import com.odesa.musically.services.media.extensions.mediaUri
import com.odesa.musically.services.media.extensions.path
import com.odesa.musically.services.media.extensions.size
import com.odesa.musically.services.media.extensions.title
import com.odesa.musically.services.media.extensions.trackNumber
import com.odesa.musically.services.media.extensions.year
import com.odesa.musically.services.media.library.BrowseTree
import com.odesa.musically.services.media.library.LocalMusicSource
import com.odesa.musically.services.media.library.MUSICALLY_BROWSABLE_ROOT
import com.odesa.musically.services.media.library.MusicSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * This class is the entry point for browsing and playback commands from the APP's UI.
 *
 * Browsing begins with the method [MusicService.onGetRoot], and continues in the callback
 * [MusicService.onLoadChildren].
 */
class MusicService : MediaBrowserServiceCompat() {

    private lateinit var musicSource: MusicSource

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope( Dispatchers.Main + serviceJob )
    private lateinit var mediaSession: MediaSessionCompat

    /**
     * This must be 'by lazy' because the [MusicSource] won't initially be ready. See
     * [MusicService.onLoadChildren] to see where it's accessed ( and first constructed )
     */
    private val browseTree: BrowseTree by lazy {
        BrowseTree( musicSource )
    }

    override fun onCreate() {
        super.onCreate()
        Timber.tag( TAG ).d( "MUSIC SERVICE CREATED" )
        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage( packageName )?.let {
                PendingIntent.getActivity( this, 0, it,
                    PendingIntent.FLAG_IMMUTABLE )
            }

        mediaSession = MediaSessionCompat( this, "Music-Service" )
            .apply {
                setSessionActivity( sessionActivityPendingIntent )
                isActive = true
            }

        /**
         * In order for [MediaBrowserCompat.ConnectionCallback.onConnected] to be called, a
         * [MediaSessionCompat.Token] needs to be set on the [MediaBrowserServiceCompat].
         * It is possible to wait to set the session token, if required for a specific use-case.
         * However, the token *must* be set by the time [MediaBrowserServiceCompat.onGetRoot]
         * returns, or the connection will fail silently. ( The system will not even call
         * [MediaBrowserCompat.ConnectionCallback.onConnectionFailed]. )
         */
        sessionToken = mediaSession.sessionToken

        musicSource = LocalMusicSource( applicationContext )
        serviceScope.launch {
            observePermissions()
        }
    }

    private suspend fun observePermissions() {
        PermissionsManager.mediaPermissionGranted.collect { permissionGranted ->
            if ( permissionGranted ) {
                Timber.tag( TAG ).d( "PERMISSION GRANTED, LOADING MUSIC SOURCE" )
                musicSource.load()
            }
            else {
                Timber.tag( TAG ).d( "PERMISSION DENIED" )
            }
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot = BrowserRoot( MUSICALLY_BROWSABLE_ROOT, Bundle.EMPTY )

    override fun onLoadChildren(
        parentMediaId: String,
        result: Result<List<MediaItem>>
    ) {
        Timber.tag( TAG ).d( "LOADING CHILDREN OF PARENT MEDIA ID: $parentMediaId" )
        // If the media source is ready, the results will be set synchronously here.
        val resultsSent = musicSource.whenReady { successfullyInitialized ->
            if ( successfullyInitialized ) {
                Timber.tag( TAG ).d( "MUSIC SOURCE SUCCESSFULLY INITIALIZED" )
                val children = browseTree[ parentMediaId ].map { child ->
                    val builder = MediaDescriptionCompat.Builder()
                    builder.setMediaId( child.id.toString() )
                    builder.setTitle( child.title )
                    builder.setDescription( child.description.toString() )
                    builder.setIconUri( child.artworkUri )
                    builder.setMediaUri( child.mediaUri )
                    val extras = Bundle()
                    extras.putString( MediaMetadataCompat.METADATA_KEY_ALBUM, child.album )
                    extras.putString( MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, child.albumArtist )
                    extras.putString( MediaMetadataCompat.METADATA_KEY_ARTIST, child.artist )
                    extras.putString( MediaMetadataCompat.METADATA_KEY_GENRE, child.genre )
                    extras.putLong( MediaMetadataCompat.METADATA_KEY_DURATION, child.duration )
                    extras.putLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, child.trackNumber )
                    extras.putLong( MediaMetadataCompat.METADATA_KEY_YEAR, child.year )
                    extras.putString( MediaMetadataCompat.METADATA_KEY_COMPOSER, child.composer )
                    extras.putLong( METADATA_KEY_DATE_MODIFIED, child.dateModified )
                    extras.putString( METADATA_KEY_PATH, child.path )
                    extras.putLong( METADATA_KEY_SIZE, child.size )
                    builder.setExtras( extras )
                    MediaItem( builder.build(), child.flag.toInt() )
                }
                result.sendResult( children )
            } else {
                Timber.tag( TAG ).d( "MUSIC SOURCE WAS NOT SUCCESSFULLY INITIALIZED" )
                result.sendResult( null )
            }
        }
        // If the results are not ready, the service must "detach" the results before the method
        // returns. After the source is ready, the lambda above will run, and the caller will
        // be notified that the results are ready.
        if ( !resultsSent )
            result.detach()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            isActive = false
            release()
        }
        // Cancel coroutines when the service is going away.
        serviceJob.cancel()
    }
}

const val TAG = "MUSIC SERVICE TAG"