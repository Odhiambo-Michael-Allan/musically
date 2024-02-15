package com.odesa.musically.services.media

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EVENT_MEDIA_ITEM_TRANSITION
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Util.constrainValue
import com.odesa.musically.R
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
import com.odesa.musically.services.media.extensions.toMediaItem
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
    private lateinit var notificationManager: MusicallyNotificationManager
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope( Dispatchers.Main + serviceJob )
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0
    private var isForegroundService = false
    private val musicallyAudioAttributes = AudioAttributes.Builder()
        .setContentType( C.AUDIO_CONTENT_TYPE_MUSIC )
        .setUsage( C.USAGE_MEDIA )
        .build()
    private val playerListener = PlayerEventListener()

    /**
     * Configure ExoPlayer to handle audio focus for us. See
     * [Player.AudioComponent.setAudioAttributes] for details.
     */
    private val exoPlayer: Player by lazy {
        SimpleExoPlayer.Builder( this )
            .build()
            .apply {
                setAudioAttributes( musicallyAudioAttributes, true )
                setHandleAudioBecomingNoisy( true )
                addListener( playerListener )
            }
    }

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

        /**
         * The notification manager will use our player and media session to decide when to post
         * notifications. When notifications are posted or removed our listener will be called,
         * this allows us to promote the service to foreground ( required so that we're not
         * killed if the main UI is not visible )
         */
        notificationManager = MusicallyNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        musicSource = LocalMusicSource( applicationContext )
        serviceScope.launch {
            observePermissions()
        }

        // ExoPlayer will manage the MediaSession for us.
        mediaSessionConnector = MediaSessionConnector( mediaSession )
        mediaSessionConnector.setPlaybackPreparer( MusicallyPlaybackPreparer() )
        mediaSessionConnector.setQueueNavigator( MusicallyQueueNavigator( mediaSession ) )
        notificationManager.showNotificationForPlayer( exoPlayer )
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

    /**
     * This is the code that causes Musically to stop playing when swiping the activity away from
     * recents. The choice to do this is app specific. Some apps stop playback, while others allow
     * playback to continue and allow users to stop it with the notification.
     */
    override fun onTaskRemoved( rootIntent: Intent? ) {
        super.onTaskRemoved( rootIntent )
        /**
         * By stopping playbck, the player will transition to [Player.STATE_IDLE] triggering
         * [Player.EventListener.onPlayerStateChanged] to be called. This will cause the
         * notification to be hidden and trigger [PlayerNotificationManager.NotificationListener.
         * onNotificationCancelled] to be called. The service will then remove itself as a
         * foreground service, and will call [stopSelf]
         */
        exoPlayer.stop()
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
        // Free ExoPlayer resources
        exoPlayer.removeListener( playerListener )
        exoPlayer.release()
    }

    /**
     * Load the supplied list of songs and the song to play into the current player.
     */
    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        Timber.tag( TAG ).d( "PREPARING PLAYLIST" )
        // Since the playlist was probably based on some ordering ( such as tracks on an album ),
        // find which window index to play first so that the song the user actually wants to
        // hear plays first.
        val initialWindowIndex = if ( itemToPlay == null ) 0 else metadataList.indexOf( itemToPlay )
        currentPlaylistItems = metadataList
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.stop()
        // Set playlist and prepare
        exoPlayer.setMediaItems(
            metadataList.map { it.toMediaItem() },
            initialWindowIndex,
            playbackStartPositionMs
        )
        exoPlayer.prepare()
    }

    private inner class MusicallyQueueNavigator(
        mediaSession: MediaSessionCompat
    ) : TimelineQueueNavigator( mediaSession ) {
        override fun getMediaDescription( player: Player, windowIndex: Int ): MediaDescriptionCompat {
            Timber.tag( TAG ).d( "MUSICALLY-QUEUE-NAVIGATOR - getMediaDescription" )
            if ( windowIndex < currentPlaylistItems.size )
                return currentPlaylistItems[ windowIndex ].description
            return MediaDescriptionCompat.Builder().build()
        }

    }

    private inner class MusicallyPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {

        /**
         * Musically supports preparing ( and playing ) from search, as well as media ID, so thos
         * capabilities are declared here.
         * TODO: Add support for ACTION_PREPARE and ACTION_PLAY, which mean "prepare/play" something
         */
        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ) = false

        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

        override fun onPrepare( playWhenReady: Boolean ) {}

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            Timber.tag( TAG ).d( "onPrepareFromMediaId: $mediaId" )
            musicSource.whenReady {
                val itemToPlay: MediaMetadataCompat? = musicSource.find { item ->
                    item.mediaUri.toString() == mediaId
                }
                Timber.tag( TAG ).d( "ITEM TO PLAY: ${itemToPlay?.mediaUri}" )
                if ( itemToPlay == null )
                    Timber.tag( TAG ).w( "Content not found: MediaId = $mediaId" )
                else {
                    val playbackStartPositionMs =
                        extras?.getLong( MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
                            C.TIME_UNSET ) ?: C.TIME_UNSET
                    preparePlaylist(
                        listOf( itemToPlay ),
                        itemToPlay,
                        playWhenReady,
                        playbackStartPositionMs
                    )
                }
            }
        }

        override fun onPrepareFromSearch( query: String, playWhenReady: Boolean, extras: Bundle? ) {}

        override fun onPrepareFromUri( uri: Uri, playWhenReady: Boolean, extras: Bundle? ) = Unit

        /**
         * Builds a playlist based on a a [MediaMetadataCompat]
         *
         * TODO: Support building a playlist by artist, genre, etc..
         */
        private fun buildPlaylist( item: MediaMetadataCompat ): List<MediaMetadataCompat> =
            musicSource.filter { it.album == item.album }.sortedBy { it.trackNumber }

    }

    /**
     * Listen for notification events.
     */
    private inner class PlayerNotificationListener : PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if ( ongoing && !isForegroundService ) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent( applicationContext, this@MusicService.javaClass )
                )
                startForeground( notificationId, notification )
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground( true )
            isForegroundService = false
            stopSelf()
        }
    }

    /**
     * Listen for events from ExoPlayer
     */
    private inner class PlayerEventListener : Player.Listener {

        override fun onPlayerStateChanged( playWhenReady: Boolean, playbackState: Int ) {
            when ( playbackState ) {
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer( exoPlayer )
                    if ( playbackState == Player.STATE_READY ) {
                        if ( !playWhenReady ) {
                            // If playback is paused we remove the foreground state which allows
                            // the notification to be dismissed. An alternative would be to provide
                            // a "close" button in the notification which stops playback and clears
                            // the notification.
                            stopForeground( false )
                            isForegroundService = false
                        }
                    }
                } else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onEvents( player: Player, events: Player.Events ) {
            if ( events.contains( Player.EVENT_POSITION_DISCONTINUITY )
                || events.contains( EVENT_MEDIA_ITEM_TRANSITION )
                || events.contains( Player.EVENT_PLAY_WHEN_READY_CHANGED ) ) {
                currentMediaItemIndex = if ( currentPlaylistItems.isNotEmpty() ) {
                    constrainValue(
                        player.currentMediaItemIndex,
                        /* min = */ 0,
                        /* max = */ currentPlaylistItems.size - 1
                    )
                } else 0
            }
        }

        override fun onPlayerError( error: PlaybackException ) {
            var message = R.string.generic_error
            Timber.tag( TAG ).e( "Player error: ${error.errorCodeName} ( ${error.errorCode})" )
            if ( error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ) {
                message = R.string.error_media_not_found
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

const val TAG = "MUSIC SERVICE TAG"
val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"