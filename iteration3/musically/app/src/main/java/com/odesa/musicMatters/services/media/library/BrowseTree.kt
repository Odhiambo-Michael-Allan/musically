package com.odesa.musicMatters.services.media.library

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.services.media.extensions.stringRep
import timber.log.Timber

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

    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val mediaIdToMediaItem = mutableMapOf<String, MediaItem>()

    /**
     * There's a single root note ( identified by the constant [MUSICALLY_BROWSABLE_ROOT] ). The root's
     * children are each album included in the [MusicSource], and the children of each album are the
     * songs on that album. ( See [BrowseTree.buildAlbumRoot] for more details. )
     */
    init {
        val rootList = mediaIdToChildren[ MUSICALLY_BROWSABLE_ROOT ] ?: mutableListOf()
        val recommendedCategoryMetadata = MediaMetadata.Builder().apply {
            setTitle( "RECOMMENDED" )
            setIsPlayable( false )
            setFolderType( MediaMetadata.FOLDER_TYPE_MIXED )
        }.build()

        rootList += MediaItem.Builder().apply {
            setMediaId( MUSICALLY_RECOMMENDED_ROOT )
            setMediaMetadata( recommendedCategoryMetadata )
        }.build()

        val albumsMetadata = MediaMetadata.Builder().apply {
            setTitle( "ALBUMS" )
            setIsPlayable( false )
            setFolderType( MediaMetadata.FOLDER_TYPE_ALBUMS )
        }.build()

        rootList += MediaItem.Builder().apply {
            setMediaId( MUSICALLY_ALBUMS_ROOT )
            setMediaMetadata( albumsMetadata )
        }.build()

        mediaIdToChildren[ MUSICALLY_BROWSABLE_ROOT ] = rootList

        val trackList = mediaIdToChildren[ MUSICALLY_TRACKS_ROOT ] ?: mutableListOf()

        musicSource.forEach { mediaItem ->
            mediaIdToMediaItem[ mediaItem.mediaId ] = mediaItem
            Timber.tag( BROWSE_TREE_TAG ).d( mediaItem.stringRep() )
            trackList.add( mediaItem )
        }

        mediaIdToChildren[ MUSICALLY_TRACKS_ROOT ] = trackList
    }

    /**
     * Provides access to the list of children with the 'get' operator
     * i.e.: 'browseTree[MUSICALLY_BROWSABLE_ROOT]'
     */
    operator fun get( mediaId: String ) = mediaIdToChildren[ mediaId ]

    /** Provides access to the media items by media id */
    fun getMediaItemById( mediaId: String ) = mediaIdToMediaItem[ mediaId ]

}

const val MUSICALLY_BROWSABLE_ROOT = "/"
const val MUSICALLY_ALBUMS_ROOT = "__ALBUMS__"
const val MUSICALLY_TRACKS_ROOT = "__TRACKS__"
const val MUSICALLY_GENRES_ROOT = "__GENRES__"
const val MUSICALLY_RECENT_ROOT = "__RECENT__"
const val MUSICALLY_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
const val BROWSE_TREE_TAG = "BROWSE-TREE-TAG"

