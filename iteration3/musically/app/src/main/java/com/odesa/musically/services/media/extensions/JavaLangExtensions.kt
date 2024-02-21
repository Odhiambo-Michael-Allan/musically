package com.odesa.musically.services.media.extensions

import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Locale

/**
 * This file contains extension methods for the java.lang package
 */

/**
 * Helper method to check if a [String] contains another in a case insensitive way.
 */
fun String?.containsIgnoreCase( other: String? ) =
    if ( this != null && other != null ) {
        lowercase( Locale.getDefault() ).contains( other.lowercase( Locale.getDefault() ) )
    } else
        this == other

/**
 * Helper extension to Url encode a [String]. Returns an empty string when called on null.
 */
inline val String?.urlEncoded: String
    get() = if ( Charset.isSupported( "UTF-8" ) ) {
        URLEncoder.encode( this ?: "", "UTF-8" )
    } else {
        // If UTF-8 is not supported, use the default charset.
        URLEncoder.encode( this ?: "" )
    }



fun Long.formatMilliseconds() = formatToMinAndSec(
    this.div( 1000 ),
    this.div( 60 ),
    this.div( 60 ),
    this.div( 24 )
)

private fun formatToMinAndSec( duration: Long, hours: Long, minutes: Long, seconds: Long ) = when {
    duration == 0L && hours == 0L -> String.format( "%02d:%0sd", minutes, seconds )
    duration == 0L -> String.format( "%02d:%02d:%02d", hours, minutes, seconds )
    else -> String.format( "%02d:%02d:%02d:%02d", duration, hours, minutes, seconds )
}