package com.odesa.musicMatters.services.media

import android.net.Uri
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.extensions.BITRATE_KEY
import com.odesa.musicMatters.services.media.extensions.BITS_PER_SAMPLE_KEY
import com.odesa.musicMatters.services.media.extensions.CODEC_KEY
import com.odesa.musicMatters.services.media.extensions.SAMPLING_RATE_KEY
import java.text.SimpleDateFormat
import java.util.Date
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
    val genre: String?,
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
    val codec = codec
    val bitsPerSample = bitsPerSample
    val bitrate = bitrate
    val samplingRate = samplingRate
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

val Song.codec
    get() = mediaItem.mediaMetadata.extras?.getString( CODEC_KEY )
val Song.bitsPerSample
    get() = mediaItem.mediaMetadata.extras?.getLong( BITS_PER_SAMPLE_KEY )
val Song.bitrate
    get() = mediaItem.mediaMetadata.extras?.getLong( BITRATE_KEY )?.div( 1000 )
val Song.samplingRate
    get() = mediaItem.mediaMetadata.extras?.getLong( SAMPLING_RATE_KEY )?.run {
        toFloat().div( 1000 )
    }
val Song.sizeString
    get() = "%.2f MB".format( size.toDouble() / ( 1024 * 1024 ) )
val Song.dateModifiedString: String
    get() = SimpleDateFormat.getInstance().format( Date( dateModified * 1000 ) )

enum class SortSongsBy {
    CUSTOM,
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED,
    DATE_MODIFIED,
    COMPOSER,
    ALBUM_ARTIST,
    YEAR,
    FILENAME,
    TRACK_NUMBER,
}

val artistTagSeparators = setOf( "Feat", "feat.", "Ft", "ft", ",", "&" )

val testSongs = List( 100 ) {
    Song(
        id = UUID.randomUUID().toString() + "$it",
        title = "You Right",
        displayTitle = "You Right",
        trackNumber = 1,
        year = 0,
        duration = 180000L,
        albumTitle = "Best of Levels",
        genre = "Pop",
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