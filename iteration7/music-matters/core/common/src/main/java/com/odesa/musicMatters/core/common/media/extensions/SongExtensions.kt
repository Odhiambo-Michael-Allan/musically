package com.odesa.musicMatters.core.common.media.extensions

import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Song
import java.util.Date

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
    get() = java.text.SimpleDateFormat.getInstance().format( Date( dateModified * 1000 ) )



val artistTagSeparators = setOf( "Feat", "feat.", "Ft", "ft", ",", "&" )