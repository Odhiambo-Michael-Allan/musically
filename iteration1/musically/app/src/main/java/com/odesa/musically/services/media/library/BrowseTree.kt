package com.odesa.musically.services.media.library

import android.media.browse.MediaBrowser.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.odesa.musically.services.media.extensions.album
import com.odesa.musically.services.media.extensions.albumArtist
import com.odesa.musically.services.media.extensions.albumId
import com.odesa.musically.services.media.extensions.artist
import com.odesa.musically.services.media.extensions.composer
import com.odesa.musically.services.media.extensions.dateModified
import com.odesa.musically.services.media.extensions.duration
import com.odesa.musically.services.media.extensions.flag
import com.odesa.musically.services.media.extensions.id
import com.odesa.musically.services.media.extensions.path
import com.odesa.musically.services.media.extensions.size
import com.odesa.musically.services.media.extensions.title
import com.odesa.musically.services.media.extensions.trackNumber
import com.odesa.musically.services.media.extensions.year

/**
 * Represents a tree of media that's used by [MusicService.onLoadChildren].
 * [BrowseTree] maps a media id ( see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID] ) to one ( or
 * more ) [MediaMetadataCompat] objects, which are children of that media id.
 *
 * For example, given the following conceptual tree:
 *
 * root
 * +-- Albums
 * |     +-- Album_A
 * |      |     +-- Song_1
 * |      |     +-- Song_2
 * ...
 * +-- Artists
 * ...
 *
 * Requesting 'browseTree["root"]' would return a list that included "Albums", "Artists", and any
 * other direct children. Taking the media ID of "Albums" ( "Albums" in this example ),
 * 'browseTree["Albums"]' would return a singl item list "Album_A", and finally,
 * 'browseTree["Album_A"]' would return "Song_1" and "Song_2". Since those are leaf nodes,
 * requesting 'browseTree["Song_1"]' would return null ( there aren't any children of it ).
 */
class BrowseTree(
    musicSource: MusicSource,
) {

    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    /**
     * In this example, there's a single root node ( identified by the constant
     * [MUSICALLY_BROWSABLE_ROOT] ). The root's children are each album included in the
     * [MusicSource], and the children of each album are the songs on that album.
     * ( See [BrowseTree.buildAlbumRoot] for more details. )
     */
    init {
        val trackList = mutableListOf<MediaMetadataCompat>()
        musicSource.forEach { mediaMetadataCompat ->
            trackList.add( mediaMetadataCompat )
            val albumId = mediaMetadataCompat.albumId.toString()
            val albumChildren = mediaIdToChildren[ albumId ]
                ?: buildAlbumRoot( mediaMetadataCompat )
            albumChildren += mediaMetadataCompat
        }
        mediaIdToChildren[ MUSICALLY_TRACKS_ROOT ] = trackList
    }

    /**
     * Provide access to the list of children with the 'get' operator. i.e.:
     * 'browseTree[UAMP_BROWSABLE_ROOT]
     */
    operator fun get( mediaId: String ) = mediaIdToChildren[ mediaId ]!!

    /**
     * Builds a node, under the root, that represents an album, given a [MediaMetadataCompat] object
     * that's one of the songs on that album, marking the item as [MediaItem.FLAG_BROWSABLE], since
     * it will have child node(s) AKA at least 1 song.
     */
    private fun buildAlbumRoot( mediaMetadataCompat: MediaMetadataCompat ) :
            MutableList<MediaMetadataCompat> {
        val albumMetadata = MediaMetadataCompat.Builder().apply {
            id = mediaMetadataCompat.albumId
            title = mediaMetadataCompat.album
            artist = mediaMetadataCompat.albumArtist
            flag = MediaItem.FLAG_BROWSABLE
            dateModified = 0L
            size = 0L
            path = ""
            albumArtist = ""
            composer = ""
            albumId = 0L
            trackNumber = 1L
            year = 0L
            duration = 0L
        }.build()
        addAlbumMetadataToAlbumsList( albumMetadata )
        // Insert the album's root with an empty list for its children, and return the list
        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[ albumMetadata.id.toString() ] = it
        }
    }

    private fun addAlbumMetadataToAlbumsList( albumMetadata: MediaMetadataCompat ) {
        val currentAlbums = mediaIdToChildren[ MUSICALLY_ALBUMS_ROOT ] ?: mutableListOf()
        currentAlbums += albumMetadata
        mediaIdToChildren[ MUSICALLY_ALBUMS_ROOT ] = currentAlbums
    }

}

const val MUSICALLY_BROWSABLE_ROOT = "/"
const val MUSICALLY_ALBUMS_ROOT = "__ALBUMS__"
const val MUSICALLY_TRACKS_ROOT = "__TRACKS__"
const val MUSICALLY_GENRES_ROOT = "__GENRES__"
const val MUSICALLY_RECENT_ROOT = "__RECENT__"
const val MUSICALLY_RECOMMENDED_ROOT = "__RECOMMENDED__"

const val BROWSE_TREE_TAG = "BROWSE-TREE-TAG"

