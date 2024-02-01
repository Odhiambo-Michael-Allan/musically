package com.odesa.musically.services.audio

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.odesa.musically.utils.CursorUtil

data class Song(
    val id: Long,
    val title: String,
    val trackNumber: Int?,
    val year: Int?,
    val duration: Long,
    val album: String?,
    val artists: Set<String>,
    val composers: Set<String>,
    val dateAdded: Long,
    val dateModified: Long,
    val size: Long,
    val path: String
) {

    val uri: Uri
        get() = buildUri( id )

    companion object {

        fun buildUri( id: Long ) = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
        )

        fun fromCursor(
            cursorUtil: CursorUtil,
            artistTagSeparators: Set<String>
        ): Song {
            return Song(
                id = cursorUtil.getLongFrom( AudioColumns._ID ),
                title = cursorUtil.getStringFrom( AudioColumns.TITLE ),
                trackNumber = cursorUtil.getIntNullableFrom( AudioColumns.TRACK )?.takeIf { it > 1000 },
                year = cursorUtil.getIntNullableFrom( AudioColumns.YEAR )?.takeIf { it > 0 },
                duration = cursorUtil.getLongFrom( AudioColumns.DURATION ),
                album = cursorUtil.getStringNullableFrom( AudioColumns.ALBUM ),
                artists = cursorUtil.getStringNullableFrom( AudioColumns.ARTIST )
                    ?.let { parseArtistStringIntoIndividualArtists( it, artistTagSeparators ) }
                    ?: setOf(),
                composers = cursorUtil.getStringNullableFrom( AudioColumns.COMPOSER )
                    ?.let{ parseArtistStringIntoIndividualArtists( it, artistTagSeparators ) }
                    ?: setOf(),
                dateAdded = cursorUtil.getLongFrom( AudioColumns.DATE_ADDED ),
                dateModified = cursorUtil.getLongFrom( AudioColumns.DATE_MODIFIED ),
                size = cursorUtil.getLongFrom( AudioColumns.SIZE ),
                path = cursorUtil.getStringFrom( AudioColumns.DATA )

            )
        }

        private fun parseArtistStringIntoIndividualArtists(artistString: String, separators: Set<String> ) =
            artistString.split( *separators.toTypedArray() )
                .mapNotNull { x -> x.trim().takeIf { it.isNotEmpty() } }
                .toSet()
    }
}
