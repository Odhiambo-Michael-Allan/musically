package com.odesa.musically.services.media.extensions

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat

/**
 * Useful extension methods for [PlaybackStateCompat]
 */
inline val PlaybackStateCompat.isPrepared
    get() = ( state == PlaybackStateCompat.STATE_BUFFERING ) ||
            ( state == PlaybackStateCompat.STATE_PLAYING ) ||
            ( state == PlaybackStateCompat.STATE_PAUSED )

inline val PlaybackStateCompat.isPlaying
    get() = ( state == PlaybackStateCompat.STATE_BUFFERING ) ||
            ( state == PlaybackStateCompat.STATE_PLAYING )

inline val PlaybackStateCompat.isPlayEnabled
    get() = ( actions and PlaybackStateCompat.ACTION_PLAY != 0L ) ||
            ( ( actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L ) &&
                    ( state == PlaybackStateCompat.STATE_PAUSED ) )

inline val PlaybackStateCompat.isPauseEnabled
    get() = ( actions and PlaybackStateCompat.ACTION_PAUSE != 0L ) ||
            ( ( actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L ) &&
                    ( state == PlaybackStateCompat.STATE_BUFFERING ||
                            state == PlaybackStateCompat.STATE_PLAYING ) )

inline val PlaybackStateCompat.isSkipToNextEnabled
    get() = actions and PlaybackStateCompat.ACTION_SKIP_TO_NEXT != 0L

inline val PlaybackStateCompat.isSkipToPreviousEnabled
    get() = actions and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS != 0L

//inline val PlaybackStateCompat.stateName
//    get() = when ( state ) {
//        PlaybackStateCompat.STATE_NONE -> "STATE_NONE"
//        PlaybackStateCompat.STATE_STOPPED -> "STATE_STOPPED"
//        PlaybackStateCompat.STATE_PAUSED -> "STATE_PAUSED"
//    }

/**
 * Calculates the current playback position based on last update time along with playback state
 * and speed.
 */
inline val PlaybackStateCompat.currentPlaybackPosition: Long
    get() = if ( state == PlaybackStateCompat.STATE_PLAYING ) {
        val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
        ( position + ( timeDelta * playbackSpeed ) ).toLong()
    } else position