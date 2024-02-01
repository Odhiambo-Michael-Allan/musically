package com.odesa.musically.services.audio

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.odesa.musically.utils.CursorUtil
import com.odesa.musically.utils.Logger
import com.odesa.musically.utils.getColumnIndices

class SongsRetriever(
    private val context: Context,
    private val artistTagSeparators: Set<String>
) {

    fun fetch(): List<Song> {
        val songs = ArrayList<Song>()
        try {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columnsOfInterest.toTypedArray(),
                MediaStore.Audio.Media.IS_MUSIC + " = 1",
                null,
                null
            )
            cursor?.use {
                val cursorUtil = CursorUtil( it, it.getColumnIndices( columnsOfInterest ) )

                while ( it.moveToNext() ) {
                    kotlin.runCatching {
                        Song.fromCursor(
                            cursorUtil = cursorUtil,
                            artistTagSeparators = artistTagSeparators
                        )
                    }.getOrNull()
                        ?.also { song ->
                            songs.add( song )
                        }
                }
            }
        } catch ( exception: Exception ) {
            Logger.error( "SongRetriever", "fetch failed", exception )
        }
        return songs
    }



    companion object {
        val columnsOfInterest = listOf(
            AudioColumns._ID,
            AudioColumns.DATE_MODIFIED,
            AudioColumns.TITLE,
            AudioColumns.TRACK,
            AudioColumns.YEAR,
            AudioColumns.DURATION,
            AudioColumns.ALBUM_ID,
            AudioColumns.ALBUM,
            AudioColumns.ARTIST_ID,
            AudioColumns.ARTIST,
            AudioColumns.COMPOSER,
            AudioColumns.DATE_ADDED,
            AudioColumns.SIZE,
            AudioColumns.DATA
        )
    }
}