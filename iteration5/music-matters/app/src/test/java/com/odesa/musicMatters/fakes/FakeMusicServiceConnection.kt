package com.odesa.musicMatters.fakes

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.NOTHING_PLAYING
import com.odesa.musicMatters.services.media.connection.PlaybackState
import com.odesa.musicMatters.services.media.extensions.ALBUM_TITLE_KEY
import com.odesa.musicMatters.services.media.extensions.ARTIST_KEY
import com.odesa.musicMatters.services.media.extensions.PATH_KEY
import com.odesa.musicMatters.services.media.extensions.toAlbum
import com.odesa.musicMatters.services.media.extensions.toArtist
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.ui.tree.testPaths
import com.odesa.musicMatters.utils.subListNonStrict
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID


class FakeMusicServiceConnection : MusicServiceConnection {

    private val _nowPlaying = MutableStateFlow( NOTHING_PLAYING )
    override val nowPlaying = _nowPlaying.asStateFlow()

    private val _playbackState = MutableStateFlow( PlaybackState() )
    override val playbackState = _playbackState.asStateFlow()

    private val _currentlyPlayingMediaItemIndex = MutableStateFlow( 0 )
    override val currentlyPlayingMediaItemIndex = _currentlyPlayingMediaItemIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow( false )
    override val isPlaying = _isPlaying.asStateFlow()

    override val player: Player? = null

    private val _mediaItemsInQueue = MutableStateFlow( emptyList<MediaItem>() )
    override val mediaItemsInQueue = _mediaItemsInQueue.asStateFlow()

    private val _cachedSongs = MutableStateFlow( trackList )
    override val cachedSongs = _cachedSongs.asStateFlow()

    private val _cachedGenres = MutableStateFlow( genreList )
    override val cachedGenres = _cachedGenres.asStateFlow()

    private val _cachedRecentlyAddedSongs = MutableStateFlow( trackList.subListNonStrict( 5 ) )
    override val cachedRecentlyAddedSongs = _cachedRecentlyAddedSongs.asStateFlow()

    private val _cachedArtists = MutableStateFlow( artistList )
    override val cachedArtists = _cachedArtists.asStateFlow()

    private val _cachedSuggestedArtists = MutableStateFlow( artistList.subListNonStrict( 6 ) )
    override val cachedSuggestedArtists = _cachedSuggestedArtists.asStateFlow()

    private val _cachedAlbums = MutableStateFlow( albumList )
    override val cachedAlbums = _cachedAlbums.asStateFlow()

    private val _cachedSuggestedAlbums = MutableStateFlow( albumList.subListNonStrict( 6 ) )
    override val cachedSuggestedAlbums = _cachedSuggestedAlbums.asStateFlow()

    private val _isInitializing = MutableStateFlow( true )
    override var isInitializing = _isInitializing.asStateFlow()

    fun setIsInitialized() {
        _isInitializing.value = false
    }

    override suspend fun playMediaItem(
        mediaItem: MediaItem,
        mediaItems: List<MediaItem>,
        shuffle: Boolean ) {
        _mediaItemsInQueue.value = mediaItems
    }

    override fun setPlaybackSpeed( playbackSpeed: Float ) {}

    override fun setPlaybackPitch( playbackPitch: Float ) {}
    override fun setRepeatMode( repeatMode: Int ) {}
    override fun shuffleSongsInQueue() {}
    override fun moveMediaItem( from: Int, to: Int ) {}
    override fun clearQueue() {
        _mediaItemsInQueue.value = emptyList()
    }

    override fun mediaItemIsPresentInQueue( mediaItem: MediaItem ): Boolean {
        TODO( "Not yet implemented" )
    }

    override fun playNext( mediaItem: MediaItem ) {
        TODO("Not yet implemented")
    }

    override fun addToQueue( mediaItem: MediaItem ) {
        TODO("Not yet implemented")
    }

    override fun searchSongsMatching(query: String): List<Song> {
        TODO("Not yet implemented")
    }

    fun setNowPlaying( mediaItem: MediaItem ) {
        _nowPlaying.value = mediaItem
    }

    fun setPlaybackState( playbackState: PlaybackState ) {
        _playbackState.value = playbackState
    }

    fun setMediaItems( mediaItemList: List<MediaItem> ) {
        _mediaItemsInQueue.value = mediaItemList
    }

    fun setCurrentMediaItemIndex( index: Int ) {
        _currentlyPlayingMediaItemIndex.value = index
    }

    fun setIsPlaying( isPlaying: Boolean ) {
        _isPlaying.value = isPlaying
    }

}

val id1 = UUID.randomUUID().toString()
val id2 = UUID.randomUUID().toString()
val id3 = UUID.randomUUID().toString()
val id4 = UUID.randomUUID().toString()
val id5 = UUID.randomUUID().toString()
val id6 = UUID.randomUUID().toString()

val testMediaItems: List<MediaItem> = listOf(
    MediaItem.Builder().setMediaId( id1 ).build(),
    MediaItem.Builder().setMediaId( id2 ).build(),
    MediaItem.Builder().setMediaId( id3 ).build()
)

val genreList: List<MediaItem> = listOf(
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Hip Hop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Pop" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "RnB" )
            }.build()
        )
    }.build()
)

val trackList: List<Song> = listOf(
    MediaItem.Builder().apply {
        setMediaId( id1 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "You Right" )
                setGenre( "RnB" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-1" )
                    putString( PATH_KEY, testPaths.first() )
                    putString( ARTIST_KEY, "Doja Cat, The Weekend" )
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id2 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Best Man" )
                setGenre( "Hip Hop" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-2" )
                    putString( PATH_KEY, testPaths[1] )
                    putString( ARTIST_KEY, "Travis Scott, Quavo" )
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id3 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Huncho Jack" )
                setGenre( "Hip Hop" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-2" )
                    putString( PATH_KEY, testPaths[2] )
                    putString( ARTIST_KEY, "Travis Scott, Quavo" )
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id4 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Lean on" )
                setGenre( "Dance" )
                setAlbumTitle( "Album-3" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-3" )
                    putString( PATH_KEY, testPaths[3] )
                    putString( ARTIST_KEY, "Major Lazer & DJ Snake")
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id5 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Scared To Be Lonely" )
                setGenre( "House" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-4" )
                    putString( PATH_KEY, testPaths[4] )
                    putString( ARTIST_KEY, "Martin Garrix, Dua Lipa" )
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( id6 ).setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "High off life" )
                setGenre( "House" )
                val extras = Bundle().apply {
                    putString( ALBUM_TITLE_KEY, "Album-4" )
                    putString( PATH_KEY, testPaths[4] )
                    putString( ARTIST_KEY, "Martin Garrix" )
                }
                setExtras( extras )
            }.build()
        )
    }.build(),
).map { it.toSong( artistTagSeparators ) }


val albumList: List<Album> = listOf(
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller1" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller2" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Thriller3" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Album-4" )
                setArtist( "Travis Scott" )
            }.build()
        )
    }.build()
).map { it.toAlbum() }

val artistList: List<Artist> = listOf(
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist1" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist2" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist3" )
                setArtworkUri( Uri.EMPTY )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "artist4" )
            }.build()
        )
    }.build(),
    MediaItem.Builder().apply {
        setMediaMetadata(
            MediaMetadata.Builder().apply {
                setTitle( "Travis Scott" )
            }.build()
        )
    }.build()
).map { it.toArtist() }

