package com.odesa.musicMatters.services.media.library

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.services.media.extensions.stringRep
import timber.log.Timber
import java.util.UUID

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
 * 'browseTree["Albums"]' would return a single item list "Album_A", and finally,
 * 'browseTree["Album_A"]' would return "Song_1" and "Song_2". Since those are leaf nodes,
 * requesting 'browseTree["Song_1"]' would return null ( there aren't any children of it ).
 */
class BrowseTree(
    private val musicSource: MusicSource,
) {

    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val mediaIdToMediaItem = mutableMapOf<String, MediaItem>()

    /**
     * There's a single root note ( identified by the constant [MUSICALLY_BROWSABLE_ROOT] ). The root's
     * children are each album included in the [MusicSource], and the children of each album are the
     * songs on that album. ( See [BrowseTree.buildAlbumRoot] for more details. )
     */
    init {

        val rootList = mediaIdToChildren[ MUSIC_MATTERS_BROWSABLE_ROOT ] ?: mutableListOf()
        addRootMediaItemsTo( rootList )
        mediaIdToChildren[ MUSIC_MATTERS_BROWSABLE_ROOT ] = rootList

        val trackList = mediaIdToChildren[ MUSIC_MATTERS_TRACKS_ROOT ] ?: mutableListOf()
        addTrackMediaItemsTo( trackList )
        mediaIdToChildren[ MUSIC_MATTERS_TRACKS_ROOT ] = trackList

        val genreList = mediaIdToChildren[ MUSIC_MATTERS_GENRES_ROOT ] ?: mutableListOf()
        addGenreMediaItemsTo( genreList )
        mediaIdToChildren[ MUSIC_MATTERS_GENRES_ROOT ] = genreList

    }

    private fun addRootMediaItemsTo( rootList: MutableList<MediaItem> ) {
        addRecommendedMediaItemTo( rootList )
        addAlbumsMediaItemTo( rootList )
        addGenresMediaItemTo( rootList )
    }

    private fun addRecommendedMediaItemTo( rootList: MutableList<MediaItem> ) {
        val recommendedCategoryMetadata = MediaMetadata.Builder().apply {
            setTitle( "RECOMMENDED" )
            setIsPlayable( false )
            setFolderType( MediaMetadata.FOLDER_TYPE_MIXED )
        }.build()

        rootList += MediaItem.Builder().apply {
            setMediaId( MUSIC_MATTERS_RECOMMENDED_ROOT )
            setMediaMetadata( recommendedCategoryMetadata )
        }.build()
    }

    private fun addAlbumsMediaItemTo( rootList: MutableList<MediaItem> ) {
        val albumsMetadata = MediaMetadata.Builder().apply {
            setTitle( "ALBUMS" )
            setIsPlayable( false )
            setFolderType( MediaMetadata.FOLDER_TYPE_ALBUMS )
        }.build()

        rootList += MediaItem.Builder().apply {
            setMediaId( MUSIC_MATTERS_ALBUMS_ROOT )
            setMediaMetadata( albumsMetadata )
        }.build()
    }

    private fun addGenresMediaItemTo( rootList: MutableList<MediaItem> ) {
        val genresMetadata = MediaMetadata.Builder().apply {
            setTitle( "GENRES" )
            setIsPlayable( false )
            setFolderType( MediaMetadata.FOLDER_TYPE_GENRES )
        }.build()

        rootList += MediaItem.Builder().apply {
            setMediaId( MUSIC_MATTERS_GENRES_ROOT )
                .setMediaMetadata( genresMetadata )
        }.build()
    }

    private fun addTrackMediaItemsTo( trackList: MutableList<MediaItem> ) {
        musicSource.forEach { mediaItem ->
            mediaIdToMediaItem[ mediaItem.mediaId ] = mediaItem
            Timber.tag( BROWSE_TREE_TAG ).d( mediaItem.stringRep() )
            trackList.add( mediaItem )
        }
    }

    private fun addGenreMediaItemsTo( genreList: MutableList<MediaItem> ) {
        musicSource.forEach { mediaItem ->
            val mediaItemGenre = mediaItem.mediaMetadata.genre ?: "<unknown>"
            val loadedGenre = mediaItemGenre.toString()
            val genreAlreadyExistsInGenreList = findGenreIn( genreList, loadedGenre )
            if ( !genreAlreadyExistsInGenreList ) {
                val genreMetadata = createGenreMetadataUsing( loadedGenre )
                val genreMediaItem = createGenreMediaItemUsing( genreMetadata )
                genreList.add( genreMediaItem )
            }
        }
    }

    private fun findGenreIn( genreList: MutableList<MediaItem>, genre: String ): Boolean {
        genreList.forEach { genreMediaItem ->
            if ( genreMediaItem.mediaMetadata.title.toString().lowercase() == genre.lowercase() )
                return true
        }
        return false
    }

    private fun createGenreMetadataUsing( genre: String ) = MediaMetadata.Builder().apply {
        setTitle( genre )
        setIsPlayable( false )
        setFolderType( MediaMetadata.FOLDER_TYPE_GENRES )
    }.build()

    private fun createGenreMediaItemUsing( genreMetadata: MediaMetadata ) = MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata( genreMetadata )
    }.build()

    /**
     * Provides access to the list of children with the 'get' operator
     * i.e.: 'browseTree[MUSIC_MATTERS_BROWSABLE_ROOT]'
     */
    operator fun get( mediaId: String ) = mediaIdToChildren[ mediaId ]

    /** Provides access to the media items by media id */
    fun getMediaItemById( mediaId: String ) = mediaIdToMediaItem[ mediaId ]

}

const val MUSIC_MATTERS_BROWSABLE_ROOT = "/"
const val MUSIC_MATTERS_ALBUMS_ROOT = "__ALBUMS__"
const val MUSIC_MATTERS_TRACKS_ROOT = "__TRACKS__"
const val MUSIC_MATTERS_GENRES_ROOT = "__GENRES__"
const val MUSIC_MATTERS_RECENT_ROOT = "__RECENT__"
const val MUSIC_MATTERS_ARTISTS_ROOT = "__ARTISTS__"
const val MUSIC_MATTERS_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
const val BROWSE_TREE_TAG = "BROWSE-TREE-TAG"

