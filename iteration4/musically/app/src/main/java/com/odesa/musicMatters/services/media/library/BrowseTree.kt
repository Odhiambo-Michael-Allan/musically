package com.odesa.musicMatters.services.media.library

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.extensions.DATE_KEY
import com.odesa.musicMatters.services.media.extensions.parseArtistStringIntoIndividualArtists
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

        val albumList = mediaIdToChildren[ MUSIC_MATTERS_ALBUMS_ROOT ] ?: mutableListOf()
        addAlbumMediaItemsTo( albumList )
        mediaIdToChildren[ MUSIC_MATTERS_ALBUMS_ROOT ] = albumList

        val recentSongsList = mediaIdToChildren[ MUSIC_MATTERS_RECENT_SONGS_ROOT ] ?: mutableListOf()
        addRecentSongMediaItemsTo( recentSongsList )
        mediaIdToChildren[ MUSIC_MATTERS_RECENT_SONGS_ROOT ] = recentSongsList

        val suggestedAlbumList = mediaIdToChildren[ MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT ] ?: mutableListOf()
        addSuggestedAlbumMediaItemsTo( suggestedAlbumList )
        mediaIdToChildren[ MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT ] = suggestedAlbumList

        val artistList = mediaIdToChildren[ MUSIC_MATTERS_ARTISTS_ROOT ] ?: mutableListOf()
        addArtistMediaItemsTo( artistList )
        mediaIdToChildren[ MUSIC_MATTERS_ARTISTS_ROOT ] = artistList

        val suggestedArtistsList = mediaIdToChildren[ MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT ] ?: mutableListOf()
        addSuggestedArtistMediaItemsTo( suggestedArtistsList )
        mediaIdToChildren[ MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT ] = suggestedArtistsList

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
//            Timber.tag( BROWSE_TREE_TAG ).d( mediaItem.stringRep() )
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

    private fun addAlbumMediaItemsTo( albumList: MutableList<MediaItem> ) {
        musicSource.forEach {  mediaItem ->
            val mediaItemAlbum = mediaItem.mediaMetadata.albumTitle ?: "<unknown>"
            val loadedAlbum = mediaItemAlbum.toString()
            val albumAlreadyExistsInAlbumList = findAlbumIn( albumList, loadedAlbum )
            if ( !albumAlreadyExistsInAlbumList ) {
                val albumMetadata = createAlbumMetadataUsing( loadedAlbum, mediaItem.mediaMetadata.artworkUri )
                val albumMediaItem = createAlbumMediaItemUsing( albumMetadata )
                albumList.add( albumMediaItem )
            }
        }
    }

    private fun findAlbumIn( albumList: MutableList<MediaItem>, album: String ): Boolean {
        albumList.forEach { albumMediaItem ->
            if ( albumMediaItem.mediaMetadata.title.toString().lowercase() == album.lowercase() )
                return true
        }
        return false
    }

    private fun createAlbumMetadataUsing( albumName: String, artworkUri: Uri? ) = MediaMetadata.Builder().apply {
        setTitle( albumName )
        setIsPlayable( false )
        setArtworkUri( artworkUri )
        setFolderType( MediaMetadata.FOLDER_TYPE_ALBUMS )
    }.build()

    private fun createAlbumMediaItemUsing( albumMetadata: MediaMetadata ) = MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata( albumMetadata )
    }.build()

    private fun addRecentSongMediaItemsTo( recentSongsList: MutableList<MediaItem> ) {
        val trackList = mediaIdToChildren[ MUSIC_MATTERS_TRACKS_ROOT ]
        trackList!!.sortByDescending { it.mediaMetadata.extras?.getLong( DATE_KEY ) }
        recentSongsList.addAll( trackList.subList( 0, 5 ) )
    }

    private fun addSuggestedAlbumMediaItemsTo( suggestedAlbumList: MutableList<MediaItem> ) {
        val albumList = mediaIdToChildren[ MUSIC_MATTERS_ALBUMS_ROOT ]!!.shuffled()
        val albumsWithArtwork = albumList.filter { it.mediaMetadata.artworkUri != null }
        when {
            albumsWithArtwork.size >= 6 -> suggestedAlbumList.addAll( albumsWithArtwork.subList( 0, 6 ) )
            else -> suggestedAlbumList.addAll( albumsWithArtwork.subList( 0, albumsWithArtwork.size ) )
        }
        if ( suggestedAlbumList.size < 6 ) {
            val albumsWithoutArtwork = albumList.filter { it.mediaMetadata.artworkUri == null }.toMutableList()
            while ( suggestedAlbumList.size < 6 && albumsWithoutArtwork.size > 0 ) {
                val album = albumsWithoutArtwork.removeAt( 0 )
                suggestedAlbumList.add( album )
            }

        }
    }

    private fun addArtistMediaItemsTo( artistList: MutableList<MediaItem> ) {
        musicSource.forEach {  mediaItem ->
            val splitArtists = mediaItem.parseArtistStringIntoIndividualArtists( artistTagSeparators ).toMutableSet()
            if ( splitArtists.isEmpty() ) splitArtists.add( mediaItem.mediaMetadata.artist?.toString() ?: "" )
            splitArtists.forEach { artist ->
                val artistAlreadyExistsInArtistList = findArtistIn( artistList, artist )
                if ( !artistAlreadyExistsInArtistList ) {
                    val artistMetadata = createArtistMetadataUsing( artist, mediaItem.mediaMetadata.artworkUri )
                    val artistMediaItem = createArtistMediaItemUsing( artistMetadata )
                    artistList.add( artistMediaItem )
                }
            }
        }
    }

    private fun findArtistIn( artistList: MutableList<MediaItem>, artist: String ): Boolean {
        artistList.forEach { artistMediaItem ->
            if ( artistMediaItem.mediaMetadata.title.toString().lowercase() == artist.lowercase() )
                return true
        }
        return false
    }

    private fun createArtistMetadataUsing( artistName: String, artworkUri: Uri? ) = MediaMetadata.Builder().apply {
        setTitle( artistName )
        setIsPlayable( false )
        setArtworkUri( artworkUri )
        setFolderType( MediaMetadata.FOLDER_TYPE_ARTISTS )
    }.build()

    private fun createArtistMediaItemUsing( artistMetadata: MediaMetadata ) = MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata( artistMetadata )
    }.build()

    private fun addSuggestedArtistMediaItemsTo( suggestedArtistList: MutableList<MediaItem> ) {
        val artistList = mediaIdToChildren[ MUSIC_MATTERS_ARTISTS_ROOT ]!!.shuffled()
        val artistsWithArtwork = artistList.filter { it.mediaMetadata.artworkUri != null }
        when {
            artistsWithArtwork.size >= 6 -> suggestedArtistList.addAll( artistsWithArtwork.subList( 0, 6 ) )
            else -> suggestedArtistList.addAll( artistsWithArtwork.subList( 0, artistsWithArtwork.size ) )
        }
        if ( suggestedArtistList.size < 6 ) {
            val artistsWithoutArtwork = artistList.filter { it.mediaMetadata.artworkUri == null }.toMutableList()
            while ( suggestedArtistList.size < 6 && artistsWithoutArtwork.size > 0 ) {
                val artist = artistsWithoutArtwork.removeAt( 0 )
                suggestedArtistList.add( artist )
            }

        }
    }

    /**
     * Provides access to the list of children with the 'get' operator
     * i.e.: 'browseTree[MUSIC_MATTERS_BROWSABLE_ROOT]'
     */
    operator fun get( mediaId: String ) = mediaIdToChildren[ mediaId ]

    /** Provides access to the media items by media id */
    fun getMediaItemById( mediaId: String ) = mediaIdToMediaItem[ mediaId ]

}

const val MUSIC_MATTERS_BROWSABLE_ROOT = "/"
const val MUSIC_MATTERS_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val MUSIC_MATTERS_TRACKS_ROOT = "__TRACKS__"
const val MUSIC_MATTERS_GENRES_ROOT = "__GENRES__"
const val MUSIC_MATTERS_RECENT_SONGS_ROOT = "__RECENT_SONGS__"

const val MUSIC_MATTERS_ARTISTS_ROOT = "__ARTISTS__"
const val MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT = "__SUGGESTED-ARTISTS__"

const val MUSIC_MATTERS_ALBUMS_ROOT = "__ALBUMS__"
const val MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT = "__SUGGESTED-ALBUMS__"

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
const val BROWSE_TREE_TAG = "BROWSE-TREE-TAG"

