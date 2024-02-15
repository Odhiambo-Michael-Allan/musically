package com.odesa.musically.services.media

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.odesa.musically.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

/**
 * A wrapper class for ExoPlayer's PlayerNotificationManager. It sets up the notification shown to
 * the user during audio playback and provides track metadata, such as track title and icon image.
 *
 * A notification allows users to see the song being played and to control playback. It's also a
 * mandatory requirement for a foreground service and stops the MusicService from being killed.
 *
 * Musically delegates the display and update of its notification to a PlayerNotificationManager
 * provided by ExoPlayer.
 */
class MusicallyNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope( Dispatchers.Main + serviceJob )
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat( context, sessionToken )
        val builder = PlayerNotificationManager.Builder( context, NOW_PLAYING_NOTIFICATION_ID,
            NOW_PLAYING_CHANNEL_ID )
        with ( builder ) {
            setMediaDescriptionAdapter( DescriptionAdapter( mediaController ) )
            setNotificationListener( notificationListener )
            setChannelNameResourceId( R.string.notification_channel )
            setChannelDescriptionResourceId( R.string.notification_channel_description )
        }
        notificationManager = builder.build()
        notificationManager.setMediaSessionToken( sessionToken )
        notificationManager.setSmallIcon( R.drawable.musically_logo )
        notificationManager.setUseRewindAction( false )
        notificationManager.setUseFastForwardAction( false )
    }

    fun hideNotification() {
        notificationManager.setPlayer( null )
    }

    fun showNotificationForPlayer( player: Player ) {
        notificationManager.setPlayer( player )
    }

    private inner class DescriptionAdapter( private val controller: MediaControllerCompat ) :
            PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun getCurrentContentTitle( player: Player ) = "Content Title"
//            controller.metadata.description.title.toString()

        override fun createCurrentContentIntent( player: Player ): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentText( player: Player ) = "Content Text"
//            controller.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
//            val iconUri = controller.metadata.description.iconUri
//            return if ( currentIconUri != null || currentBitmap == null ) {
//                // Cache the bitmap for the current song so that successive calls to
//                // 'getCurrentLargeIcon' don't cause the bitmap to be recreated.
//                currentIconUri = iconUri
//                serviceScope.launch {
//                    currentBitmap = iconUri?.let {
//                        resolveUriAsBitmap( it )
//                    }
//                    currentBitmap?.let { callback.onBitmap( it ) }
//                }
//                null
//            } else currentBitmap
            return null
        }

        private suspend fun resolveUriAsBitmap( uri: Uri ): Bitmap? {
            return withContext( Dispatchers.IO ) {
                Glide.with( context ).applyDefaultRequestOptions( glideOptions )
                    .asBitmap()
                    .load( uri )
                    .submit( NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE )
                    .get()
            }
        }

    }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144 //px
const val NOW_PLAYING_CHANNEL_ID = "com.odesa.musically.media.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0xb339
private val glideOptions = RequestOptions()
    .fallback( R.drawable.placeholder )
    .diskCacheStrategy( DiskCacheStrategy.DATA )