package com.odesa.musically.services.media

import android.net.Uri
import androidx.media3.common.MediaItem
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.extensions.BITRATE_KEY
import com.odesa.musically.services.media.extensions.BITS_PER_SAMPLE_KEY
import com.odesa.musically.services.media.extensions.CODEC_KEY
import com.odesa.musically.services.media.extensions.SAMPLING_RATE_KEY
import java.math.RoundingMode
import java.util.UUID

data class Song(
    val id: String,
    val mediaUri: Uri,
    val title: String,
    val displayTitle: String,
    val trackNumber: Int?,
    val year: Int?,
    val duration: Long,
    val albumTitle: String?,
    val artists: Set<String>,
    val composer: String?,
    val dateModified: Long,
    val size: Long,
    val path: String,
    val artworkUri: Uri?,
    val mediaItem: MediaItem
)

fun Song.toSamplingInfoString( language: Language ): String? {
    val values = mutableListOf<String>()
    val codec = mediaItem.mediaMetadata.extras?.getString( CODEC_KEY )
    val bitsPerSample = mediaItem.mediaMetadata.extras?.getLong( BITS_PER_SAMPLE_KEY )
    val bitrate = mediaItem.mediaMetadata.extras?.getLong( BITRATE_KEY )?.div( 1000 )
    val samplingRate = mediaItem.mediaMetadata.extras?.getLong( SAMPLING_RATE_KEY )?.apply {
        ( toFloat() / 1000 ).toBigDecimal().setScale( 1, RoundingMode.CEILING ).toFloat()
    }
    values.apply {
        codec?.let { add( it ) }
        bitsPerSample?.let { add( language.xBit( it.toString() ) ) }
        bitrate?.let { add( language.xKbps( it.toString() ) ) }
        samplingRate?.let { add( language.xKHZ( it.toString() ) ) }
    }
    return when {
        values.isNotEmpty() -> values.joinToString( ", " )
        else -> null
    }
}

val testSongs = List( 100 ) {
    Song(
        id = UUID.randomUUID().toString(),
        title = "You Right",
        displayTitle = "You Right",
        trackNumber = 1,
        year = 0,
        duration = 180000L,
        albumTitle = "Best of Levels",
        artists = setOf( "The Weekend", "Doja Cat" ),
        composer = "Abel \"The Weekend\" Tesfaye",
        dateModified = 1000000,
        size = 1000,
        path = "/storage/emulated/0/Music/Telegram/DojaCat - You Right.mp3",
        mediaUri = Uri.EMPTY,
        artworkUri = Uri.EMPTY,
        mediaItem = MediaItem.EMPTY
    )
}