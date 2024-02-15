package com.odesa.musically.services.media.extensions

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes
import timber.log.Timber

/**
 * Useful extensions for [MediaMetadataCompat]
 */
inline val MediaMetadataCompat.id
    get() = getString( MediaMetadataCompat.METADATA_KEY_MEDIA_ID ).toLong()

inline val MediaMetadataCompat.title: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_TITLE )

inline val MediaMetadataCompat.album: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_ALBUM )

inline val MediaMetadataCompat.albumArtist: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST )

inline val MediaMetadataCompat.artist: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_ARTIST )

inline val MediaMetadataCompat.genre: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_GENRE )

inline val MediaMetadataCompat.duration
    get() = getLong( MediaMetadataCompat.METADATA_KEY_DURATION )

inline val MediaMetadataCompat.trackNumber
    get() = getLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER )

inline val MediaMetadataCompat.year
    get() = getLong( MediaMetadataCompat.METADATA_KEY_YEAR )

inline val MediaMetadataCompat.composer: String?
    get() = getString( MediaMetadataCompat.METADATA_KEY_COMPOSER )

inline val MediaMetadataCompat.path: String?
    get() = getString( METADATA_KEY_PATH )

inline val MediaMetadataCompat.size
    get() = getLong( METADATA_KEY_SIZE )

inline val MediaMetadataCompat.dateModified
    get() = getLong( METADATA_KEY_DATE_MODIFIED )

inline val MediaMetadataCompat.mediaUri: Uri
    get() = buildMediaUri()

inline val MediaMetadataCompat.artworkUri: Uri?
    get() = buildArtworkUri()

inline val MediaMetadataCompat.albumId
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
        putLong( MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value )
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
        putLong( METADATA_KEY_DATE_MODIFIED, value )
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
        putLong( METADATA_KEY_ALBUM_ID, value )
    }

fun MediaMetadataCompat.Builder.from( cursor: Cursor ): MediaMetadataCompat.Builder {
    val _id = cursor.getNullableLongFrom( AudioColumns._ID ) ?: UNKNOWN_LONG_VALUE
    id = _id
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ID: $_id" )
    val _title = cursor.getNullableStringFrom( AudioColumns.TITLE ) ?: UNKNOWN_STRING_VALUE
    title = _title
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "TITLE: $_title" )
    val _trackNumber = cursor.getNullableLongFrom( AudioColumns.TRACK ) ?: UNKNOWN_LONG_VALUE
    trackNumber = _trackNumber
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "TRACK NUMBER: $_trackNumber" )
    val _year = cursor.getNullableLongFrom( AudioColumns.YEAR ) ?: UNKNOWN_LONG_VALUE
    year = _year
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "YEAR: $_year" )
    val _duration = cursor.getNullableLongFrom( AudioColumns.DURATION ) ?: UNKNOWN_LONG_VALUE
    duration = _duration
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "DURATION: $_duration" )
    val _album = cursor.getNullableStringFrom( AudioColumns.ALBUM ) ?: UNKNOWN_STRING_VALUE
    album = _album
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM: $_album" )
    val _albumId = cursor.getNullableLongFrom( AudioColumns.ALBUM_ID ) ?: UNKNOWN_LONG_VALUE
    albumId = _albumId
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM ID: $_albumId" )
    val _albumArtist = cursor.getNullableStringFrom( AudioColumns.ALBUM_ARTIST ) ?: UNKNOWN_STRING_VALUE
    albumArtist = _albumArtist
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ALBUM ARTIST: $_albumArtist" )
    val _artist = cursor.getNullableStringFrom( AudioColumns.ARTIST ) ?: UNKNOWN_STRING_VALUE
    artist = _artist
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "ARTIST: $_artist" )
    val _composer = cursor.getNullableStringFrom( AudioColumns.COMPOSER ) ?: UNKNOWN_STRING_VALUE
    composer = _composer
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "COMPOSER: $_composer" )
    val _dateModified = cursor.getNullableLongFrom( AudioColumns.DATE_MODIFIED ) ?: UNKNOWN_LONG_VALUE
    dateModified = _dateModified
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "DATE MODIFIED: $_dateModified" )
    val _size = cursor.getNullableLongFrom( AudioColumns.SIZE ) ?: UNKNOWN_LONG_VALUE
    size = _size
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "SIZE: $_size" )
    val _path = cursor.getNullableStringFrom( AudioColumns.DATA ) ?: UNKNOWN_STRING_VALUE
    path = _path
    Timber.tag( MediaMetadataCompatBuilderTag ).d( "PATH: $_path" )
    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ) {
        val _genre = cursor.getNullableStringFrom( AudioColumns.GENRE ) ?: UNKNOWN_STRING_VALUE
        genre = _genre
        Timber.tag( MediaMetadataCompatBuilderTag ).d( "GENRE: $_genre" )
    }
    return this
}

fun MediaMetadataCompat.toMediaItem(): com.google.android.exoplayer2.MediaItem {
    return with ( com.google.android.exoplayer2.MediaItem.Builder() ) {
        setMediaId( mediaUri.toString() )
        setUri( mediaUri )
        setMimeType( MimeTypes.AUDIO_MPEG )
        setMediaMetadata( toMediaItemMetadata() )
    }.build()
}

fun MediaMetadataCompat.toMediaItemMetadata(): com.google.android.exoplayer2.MediaMetadata {
    return with ( MediaMetadata.Builder() ) {
        setTitle( title )
        setDisplayTitle( title )
        setAlbumArtist( artist )
        setAlbumTitle( album )
        setComposer( composer )
        setArtworkUri( artworkUri )
        val extras = Bundle()
        extras.putLong( MediaMetadataCompat.METADATA_KEY_DURATION, duration )
        setExtras( extras )
    }.build()
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
const val UNKNOWN_LONG_VALUE = 0L
const val UNKNOWN_STRING_VALUE = "<unknown>"

