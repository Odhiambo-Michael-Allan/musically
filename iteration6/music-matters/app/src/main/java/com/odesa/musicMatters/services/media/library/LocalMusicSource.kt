package com.odesa.musicMatters.services.media.library

import android.content.Context
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.services.media.extensions.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Source of [MediaMetadataCompat] objects stored locally on the user's device.
 */
class LocalMusicSource( private val context: Context ) : AbstractMusicSource() {

    private var musicCatalog: List<MediaItem> = emptyList()

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

    private suspend fun updateCatalog(): List<MediaItem>? {
        return withContext( Dispatchers.IO ) {
            Timber.tag( LocalMusicSourceTag ).d( "READING MEDIA ITEMS FROM STORAGE" )
            val mediaItemList = mutableListOf<MediaItem>()
            try {
                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    MediaStore.Audio.Media.IS_MUSIC + " = 1",
                    null,
                    null
                )?.use {
                    while ( it.moveToNext() ) {
                        kotlin.runCatching {
                            MediaItem.Builder().from( it, context )
                        }.getOrNull() ?.also { mediaItemBuilder ->
                            mediaItemList.add( mediaItemBuilder.build() )
                        }
                    }
                }
            } catch ( exception: Exception ) {
                Timber.tag( LocalMusicSourceTag ).e( exception,
                    "Error occurred while updating music catalog" )
                return@withContext null
            }
            Timber.tag( LocalMusicSourceTag ).d( "NUMBER OF TRACKS RETRIEVED FROM STORAGE ${mediaItemList.size}" )
            mediaItemList
        }
    }
}

val projection = arrayOf(
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