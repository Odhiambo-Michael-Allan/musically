package com.odesa.musically.services.media.extensions

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.support.v4.media.MediaMetadataCompat
import timber.log.Timber

/**
 * Useful extensions for [MediaMetadataCompat]
 */
inline val MediaMetadataCompat.id: Long
    get() = getString( MediaMetadataCompat.METADATA_KEY_MEDIA_ID ).toLong()

inline val MediaMetadataCompat.title: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_TITLE )

inline val MediaMetadataCompat.album: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_ALBUM )

inline val MediaMetadataCompat.albumArtist: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST )

inline val MediaMetadataCompat.artist: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_ARTIST )

inline val MediaMetadataCompat.genre: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_GENRE )

inline val MediaMetadataCompat.duration: Long
    get() = getLong( MediaMetadataCompat.METADATA_KEY_DURATION )

inline val MediaMetadataCompat.trackNumber: Long
    get() = getLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER )

inline val MediaMetadataCompat.year: Long
    get() = getLong( MediaMetadataCompat.METADATA_KEY_YEAR )

inline val MediaMetadataCompat.composer: String
    get() = getString( MediaMetadataCompat.METADATA_KEY_COMPOSER )

inline val MediaMetadataCompat.path: String
    get() = getString( METADATA_KEY_PATH )

inline val MediaMetadataCompat.size: Long
    get() = getLong( METADATA_KEY_SIZE )

inline val MediaMetadataCompat.dateModified: Long
    get() = getLong( METADATA_KEY_DATE_MODIFIED )

inline val MediaMetadataCompat.mediaUri: Uri
    get() = buildMediaUri()

inline val MediaMetadataCompat.artworkUri: Uri?
    get() = buildArtworkUri()

inline val MediaMetadataCompat.albumId: Long
    get() = getLong( METADATA_KEY_ALBUM_ID )

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an item that is
 * [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE]
 */
inline val MediaMetadataCompat.flag
    get() = this.getLong( METADATA_KEY_BROWSABLE_OR_PLAYABLE )

fun MediaMetadataCompat.buildArtworkUri(): Uri? = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    .buildUpon()
    .run {
        appendPath( id.toString() )
        appendPath( "albumart" )
        build()
    }

fun MediaMetadataCompat.buildMediaUri() = ContentUris.withAppendedId(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
)

fun MediaMetadataCompat.parseArtistStringIntoIndividualArtists( artistString: String,
                                                                separators: Set<String> ) =
    artistString.split( *separators.toTypedArray() )
        .mapNotNull { x -> x.trim().takeIf { it.isNotEmpty() } }
        .toSet()


/**
 * Useful extensions for [MediaMetadataCompat.Builder]
 */

// These do not have getters, so create a message for the error.
const val NO_GET = "Property does not have a 'get'"

inline var MediaMetadataCompat.Builder.id: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value.toString() )
    }

inline var MediaMetadataCompat.Builder.title: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_TITLE, value )
    }

inline var MediaMetadataCompat.Builder.album: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_ALBUM, value )
    }

inline var MediaMetadataCompat.Builder.albumArtist: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, value )
    }

inline var MediaMetadataCompat.Builder.artist: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_ARTIST, value )
    }

inline var MediaMetadataCompat.Builder.genre: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_GENRE, value )
    }

inline var MediaMetadataCompat.Builder.duration: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( MediaMetadataCompat.METADATA_KEY_DURATION, value )
    }

inline var MediaMetadataCompat.Builder.trackNumber: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value ?: 0L )
    }

inline var MediaMetadataCompat.Builder.year: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( MediaMetadataCompat.METADATA_KEY_YEAR, value )
    }

inline var MediaMetadataCompat.Builder.composer: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( MediaMetadataCompat.METADATA_KEY_COMPOSER, value )
    }

inline var MediaMetadataCompat.Builder.path: String
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putString( METADATA_KEY_PATH, value )
    }

inline var MediaMetadataCompat.Builder.size: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( METADATA_KEY_SIZE, value )
    }

inline var MediaMetadataCompat.Builder.dateModified: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( METADATA_KEY_DATE_MODIFIED, value ?: 0L )
    }

inline var MediaMetadataCompat.Builder.flag: Int
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( METADATA_KEY_BROWSABLE_OR_PLAYABLE, value.toLong() )
    }

inline var MediaMetadataCompat.Builder.albumId: Long
    @Deprecated( NO_GET, level = DeprecationLevel.ERROR )
    get() = throw IllegalAccessException( "Cannot get from MediaMetadataCompat.Builder" )
    set( value ) {
        putLong( METADATA_KEY_ALBUM_ID, value ?: 0L )
    }

fun MediaMetadataCompat.Builder.from( cursor: Cursor ): MediaMetadataCompat.Builder {
    val _id = cursor.getLongFrom( AudioColumns._ID )
    id = _id
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ID: $_id" )
    val _title = cursor.getStringFrom( AudioColumns.TITLE )
    title = _title
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "TITLE: $_title" )
    val _trackNumber = cursor.getLongNullableFrom( AudioColumns.TRACK ) ?: 0L
    trackNumber = _trackNumber
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "TRACK NUMBER: $_trackNumber" )
    val _year = cursor.getLongNullableFrom( AudioColumns.YEAR ) ?: 0L
    year = _year
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "YEAR: $_year" )
    val _duration = cursor.getLongFrom( AudioColumns.DURATION )
    duration = _duration
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "DURATION: $_duration" )
    val _album = cursor.getStringNullableFrom( AudioColumns.ALBUM ) ?: ""
    album = _album
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM: $_album" )
    val _albumId = cursor.getLongNullableFrom( AudioColumns.ALBUM_ID ) ?: 0L
    albumId = _albumId
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM ID: $_albumId" )
    val _albumArtist = cursor.getStringNullableFrom( AudioColumns.ALBUM_ARTIST ) ?: ""
    albumArtist = _albumArtist
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM ARTIST: $_albumArtist" )
    val _artist = cursor.getStringNullableFrom( AudioColumns.ARTIST ) ?: ""
    artist = _artist
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ARTIST: $_artist" )
    val _composer = cursor.getStringNullableFrom( AudioColumns.COMPOSER ) ?: ""
    composer = _composer
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "COMPOSER: $_composer" )
    val _dateModified = cursor.getLongNullableFrom( AudioColumns.DATE_MODIFIED ) ?: 0L
    dateModified = _dateModified
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "DATE MODIFIED: $_dateModified" )
    val _size = cursor.getLongNullableFrom( AudioColumns.SIZE ) ?: 0L
    size = _size
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "SIZE: $_size" )
    val _path = cursor.getStringNullableFrom( AudioColumns.DATA ) ?: ""
    path = _path
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "PATH: $_path" )
    val _genre = cursor.getStringNullableFrom( AudioColumns.GENRE ) ?: ""
    genre = _genre
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "GENRE: $_genre" )
    return this
}


const val METADATA_KEY_PATH = "METADATA_KEY_PATH"
const val METADATA_KEY_SIZE = "METADATA_KEY_SIZE"
const val METADATA_KEY_DATE_MODIFIED = "METADATA_KEY_DATE_MODIFIED"
const val METADATA_KEY_ALBUM_ID = "METADATA_KEY_ALBUM_ID"

/**
 * Custom property that holds whether an item is [MediaItem.FLAG_BROWSABLE] or
 * [MediaItem.FLAG_PLAYABLE]
 */
const val METADATA_KEY_BROWSABLE_OR_PLAYABLE = "com.odesa.musically.media.METADATA_KEY_PLAYABLE"

const val MediaMetadataCompatBuilderTag = "MEDIA-METADATA-COMPAT-BUILDER-TAG"

