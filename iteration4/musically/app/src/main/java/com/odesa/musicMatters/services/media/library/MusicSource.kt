package com.odesa.musicMatters.services.media.library

import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_MEDIA_GENRE
import androidx.annotation.IntDef
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.services.media.extensions.containsIgnoreCase
import timber.log.Timber

/**
 * Interface used by [MusicService] for looking up [MediaMetadataCompat] objects.
 * Because Kotlin provides methods such as [Iterable.find] and [Iterable.filter], this is a
 * convenient interface to have on sources.
 */
interface MusicSource : Iterable<MediaItem> {

    /**
     * Begins loading the data for this music source
     */
    suspend fun load()

    /**
     * Method will perform a given action after this [MusicSource] is ready to be used.
     *
     * @param performAction A lambda expression to be called with a boolean parameter when the
     * source is ready. 'true' indicates the source was successfully prepared. 'false' indicates
     * an error occurred.
     */
    fun whenReady( performAction: ( Boolean ) -> Unit ): Boolean

    fun search( query: String, extras: Bundle ): List<MediaItem>
}

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention( AnnotationRetention.RUNTIME )
annotation class State

/**
 * State indicating the source was created, but no initialization has performed
 */
const val STATE_CREATED = 1

/**
 * State indicating initialization of the source is in progress
 */
const val STATE_INITIALIZING = 2

/**
 * State indicating the source has been initialized and is ready to be used
 */
const val STATE_INITIALIZED = 3

/**
 * State indicating an error has occurred.
 */
const val STATE_ERROR = 4

/**
 * Base class for music sources in Musically.
 */
abstract class AbstractMusicSource : MusicSource {

    @State
    var state: Int = STATE_CREATED
        set( value ) {
            if ( value == STATE_INITIALIZED || value == STATE_ERROR ) {
                synchronized( onReadyListeners ) {
                    Timber.tag(BROWSE_TREE_TAG).d( "STATE CHANGED, NOTIFYING LISTENERS" )
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener( state == STATE_INITIALIZED )
                    }
                }
            } else
                field = value
        }

    private val onReadyListeners = mutableListOf<( Boolean ) -> Unit>()

    /**
     * Performs an action when this MusicSource is ready. This method is *not* threadsafe. Ensure
     * actions and state changes are only performed on a single thread.
     */
    override fun whenReady( performAction: ( Boolean ) -> Unit ): Boolean {
        return when ( state ) {
            STATE_CREATED, STATE_INITIALIZING -> {
                Timber.tag(BROWSE_TREE_TAG).d( "ADDING ACTION TO BE PERFORMED LATER - whenReady" )
                onReadyListeners += performAction
                false
            }

            else -> {
                Timber.tag(BROWSE_TREE_TAG).d( "PERFORMING ACTION IN - whenReady" )
                performAction( state != STATE_ERROR )
                true
            }
        }
    }


    override fun search( query: String, extras: Bundle ): List<MediaItem> {
        // First attempt to search with the "focus" that's provided in the extras.
        val focusSearchResult = when ( extras[ MediaStore.EXTRA_MEDIA_FOCUS ] ) {
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE -> {
                // For a Genre focused search, only genre is set.
                val genre = extras[ EXTRA_MEDIA_GENRE ]
                Timber.tag(BROWSE_TREE_TAG).d(  "Focused genre search: '$genre" )
                filter { song ->
                    song.mediaMetadata.genre?.toString() == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE -> {
                // For an Artist focused search, only the artist is set.
                val artist = extras[ MediaStore.EXTRA_MEDIA_ARTIST ]
                Timber.tag(BROWSE_TREE_TAG).d( "Focused artist search: artist = $artist" )
                filter { song ->
                    isArtist( song, artist )
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE -> {
                // For an album focused search, album and artist are set
                val artist = extras[ MediaStore.EXTRA_MEDIA_ARTIST ]
                val album = extras[ MediaStore.EXTRA_MEDIA_ALBUM ]
                Timber.tag(BROWSE_TREE_TAG).d( "Focused album search: album = '$album' artist = '$artist' ")
                filter { song ->
                    ( isArtist( song, artist ) && song.mediaMetadata.albumTitle?.toString() == album )
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                // For a Song ( aka Media ) focused search, title, album and artist are set.
                val title = extras[ MediaStore.EXTRA_MEDIA_TITLE ]
                val album = extras[ MediaStore.EXTRA_MEDIA_ALBUM ]
                val artist = extras[ MediaStore.EXTRA_MEDIA_ARTIST ]
                Timber.tag(BROWSE_TREE_TAG).d( "Focused media search: title = '$title' album = '$album' artist = '$artist'" )
                filter { song ->
                    isArtist( song, artist )
                            && song.mediaMetadata.albumTitle?.toString() == album
                            && song.mediaMetadata.title?.toString() == title
                }
            }
            else -> {
                // There isn't a focus, so no results yet.
                emptyList()
            }
        }

        // If there weren't any results from the focused search ( or if there wasn't a focus to
        // begin with ), try to find any matches given the 'query' provided, searching against
        // a few of the fields.
        // In this example, we're just checking a few fields with the provided query, but in a
        // more complex app, more logic could be used to find fuzzy matches, etc..
        if ( focusSearchResult.isEmpty() ) {
            return if ( query.isNotBlank() ) {
                Timber.tag(BROWSE_TREE_TAG).d( "Unfocused search for '$query'" )
                filter { song ->
                    song.mediaMetadata.title?.toString().containsIgnoreCase( query )
                            || song.mediaMetadata.genre?.toString().containsIgnoreCase( query )
                }
            } else {
                return focusSearchResult
            }
        }
        return focusSearchResult
    }

}

fun isArtist( mediaItem: MediaItem, artist: Any? ): Boolean {
    return mediaItem.mediaMetadata.artist?.toString() == artist
            || mediaItem.mediaMetadata.albumArtist?.toString() == artist
}

private const val TAG = "MUSIC-SOURCE"