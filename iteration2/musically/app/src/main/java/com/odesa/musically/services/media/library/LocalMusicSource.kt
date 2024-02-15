package com.odesa.musically.services.media.library

import android.content.Context
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import com.odesa.musically.services.media.extensions.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Source of [MediaMetadataCompat] objects stored locally on the user's device.
 */
class LocalMusicSource( private val context: Context ) : AbstractMusicSource() {

    private var musicCatalog: List<MediaMetadataCompat> = emptyList()

    init {
        Timber.tag( LocalMusicSourceTag ).d( "INITIALIZING LOCAL MUSIC SOURCE" )
        state = STATE_INITIALIZING
    }

    override fun iterator() = musicCatalog.iterator()

    override suspend fun load() {
        updateCatalog()?.let {
            musicCatalog = it
            state = STATE_INITIALIZED
            Timber.tag( LocalMusicSourceTag ).d( "MUSIC CATALOG INITIALIZED. STATE IS INITIALIZED" )
        } ?: run {
            musicCatalog = emptyList()
            state = STATE_ERROR
            Timber.tag( LocalMusicSourceTag ).d( "STATE IS ERROR.." )
        }
    }

    private suspend fun updateCatalog(): List<MediaMetadataCompat>? {
        return withContext( Dispatchers.IO ) {
            Timber.tag( LocalMusicSourceTag ).d( "READING MEDIA ITEMS FROM STORAGE" )
            val mediaMetadataCompatList = mutableListOf<MediaMetadataCompat>()
            try {
                val cursor = context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Audio.Media.IS_MUSIC + " = 1",
                    null,
                    null
                )?.use {
                    while ( it.moveToNext() ) {
                        kotlin.runCatching {
                            MediaMetadataCompat.Builder().from( it )
                        }.getOrNull() ?.also { mediaMetadataCompatBuilder ->
                            mediaMetadataCompatList.add( mediaMetadataCompatBuilder.build() )
                        }
                    }
                }
            } catch ( exception: Exception ) {
                Timber.tag( LocalMusicSourceTag ).e( exception,
                    "Error occurred while updating music catalog" )
                return@withContext null
            }
            Timber.tag( LocalMusicSourceTag ).d( "NUMBER OF TRACKS RETRIEVED FROM STORAGE ${mediaMetadataCompatList.size}" )
            mediaMetadataCompatList
        }
    }
}

//val projection = arrayOf<String>(
//    MediaStore.Audio.Media._ID,
//    MediaStore.Audio.Media.DATE_ADDED,
//    MediaStore.Audio.Media.ARTIST,
//    MediaStore.Audio.Media.ALBUM,
//    MediaStore.Audio.Media.ALBUM_ID,
//    MediaStore.Audio.Media.COMPOSER,
//    MediaStore.Audio.Media.GENRE,
//    MediaStore.Audio.Media.YEAR,
//    MediaStore.Audio.Media.SIZE,
//    MediaStore.Audio.Media.ARTIST_ID,
//    MediaStore.Audio.Media.DURATION,
//    MediaStore.Audio.Media.TRACK,
//    MediaStore.Audio.Media.TITLE,
//    MediaStore.Audio.Media.ALBUM_ARTIST,
//    MediaStore.Audio.Media.AUTHOR,
//    MediaStore.Audio.Media.BITRATE,
//    MediaStore.Audio.Media.BOOKMARK,
//    MediaStore.Audio.Media.CONTENT_TYPE,
//    MediaStore.Audio.Media.DATE_EXPIRES,
//    MediaStore.Audio.Media.DATE_MODIFIED,
//    MediaStore.Audio.Media.DATE_TAKEN,
//    MediaStore.Audio.Media.DISC_NUMBER,
//    MediaStore.Audio.Media.DISC_NUMBER,
//    MediaStore.Audio.Media.DISPLAY_NAME,
//    MediaStore.Audio.Media.GENRE_ID,
//    MediaStore.Audio.Media.MIME_TYPE,
//    MediaStore.Audio.Media.RESOLUTION,
//    MediaStore.Audio.Media.VOLUME_NAME,
//    MediaStore.Audio.Media.WRITER,
//    MediaStore.Audio.Media.XMP,
//)

val projection = arrayOf<String>(
    MediaStore.Audio.AudioColumns._ID,
    MediaStore.Audio.AudioColumns.DATE_ADDED,
    MediaStore.Audio.AudioColumns.DATE_MODIFIED,
    MediaStore.Audio.AudioColumns.TITLE,
    MediaStore.Audio.AudioColumns.TRACK,
    MediaStore.Audio.AudioColumns.YEAR,
    MediaStore.Audio.AudioColumns.DURATION,
    MediaStore.Audio.AudioColumns.ALBUM_ID,
    MediaStore.Audio.AudioColumns.ALBUM,
    MediaStore.Audio.AudioColumns.ARTIST_ID,
    MediaStore.Audio.AudioColumns.ARTIST,
    MediaStore.Audio.AudioColumns.COMPOSER,
    MediaStore.Audio.AudioColumns.SIZE,
    MediaStore.Audio.AudioColumns.DATA,
    MediaStore.Audio.AudioColumns.ALBUM_ID,
)

const val LocalMusicSourceTag = "LOCAL MUSIC SOURCE"