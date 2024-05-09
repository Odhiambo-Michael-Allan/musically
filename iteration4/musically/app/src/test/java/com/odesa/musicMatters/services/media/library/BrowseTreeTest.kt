package com.odesa.musicMatters.services.media.library

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.fakes.FakeMusicSource
import com.odesa.musicMatters.services.media.extensions.ARTIST_KEY
import com.odesa.musicMatters.services.media.extensions.DATE_KEY
import com.odesa.musicMatters.services.media.extensions.toAlbum
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Calendar
import java.util.UUID

@RunWith( RobolectricTestRunner::class )
class BrowseTreeTest {

    private lateinit var musicSource: MusicSource
    private lateinit var browseTree: BrowseTree

    @Before
    fun setup() {
        musicSource = FakeMusicSource( musicList )
        browseTree = BrowseTree( musicSource )
    }

    @Test
    fun testTracksAreCorrectlyConfigured() {
        val trackList = browseTree[ MUSIC_MATTERS_TRACKS_ROOT ]
        assertNotNull( trackList )
        assertEquals( 11, trackList!!.size )
    }
    
    @Test
    fun testGenresAreCorrectlyConfigured() {
        val genresList = browseTree[ MUSIC_MATTERS_GENRES_ROOT ]
        assertNotNull( genresList )
        assertEquals( 4, genresList!!.size )
    }

    @Test
    fun testRecentlyAddedSongsAreCorrectlyConfigured() {
        val recentlyAddedSongs = browseTree[ MUSIC_MATTERS_RECENT_SONGS_ROOT ]
        assertNotNull( recentlyAddedSongs )
        assertEquals( musicList.size, recentlyAddedSongs!!.size )
        assertEquals( "Don't Believe the Hype", recentlyAddedSongs[ 0 ].mediaMetadata.title )
    }

    @Test
    fun testAlbumsAreLoadedCorrectly() {
        val albums = browseTree[ MUSIC_MATTERS_ALBUMS_ROOT ]
        assertNotNull( albums )
        assertEquals( 5, albums!!.size )
        assertTrue( albums.first().toAlbum().artists.isNotEmpty() )
    }

    @Test
    fun testSuggestedAlbumAreLoadedCorrectly() {
        val suggestedAlbums = browseTree[ MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT ]
        assertNotNull( suggestedAlbums )
        assertEquals( 5, suggestedAlbums!!.size )
    }

    @Test
    fun testArtistsAreLoadedCorrectly() {
        val artists = browseTree[ MUSIC_MATTERS_ARTISTS_ROOT ]
        assertNotNull( artists )
        assertEquals( 5, artists!!.size )
    }

    @Test
    fun testSuggestedArtistsAreLoadedCorrectly() {
        val suggestedArtists = browseTree[ MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT ]
        assertNotNull( suggestedArtists )
        assertEquals( 5, suggestedArtists!!.size )
    }
}

val calendar: Calendar = Calendar.getInstance()

val musicList = listOf(
    MediaItem.Builder().apply {
        setMediaId( "ich_hasse_dich" )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Ich hasse dich" )
            setAlbumTitle( "Speechless" )
            setArtist( "Jemand" )
            setGenre( "Folk" )
            setExtras(
                Bundle().apply {
                    putLong( DATE_KEY, calendar.timeInMillis )
                    putString( ARTIST_KEY, "Jemand" )
                }
            )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( "about_a_guy" )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "About a guy" )
            setAlbumTitle( "Tales from the Render Farm" )
            setArtist( "7 Developers and a Pastry Chef" )
            setGenre( "Folk" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Beat it" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Billie Jean" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Human Nature" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Bring the Noise" )
            setAlbumTitle( "It Takes a Nation of Millions to Hold Us Back" )
            setArtist( "Public Enemy" )
            setGenre( "Hip Hop" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Don't Believe the Hype" )
            setAlbumTitle( "It Takes a Nation of Millions to Hold Us Back" )
            setArtist( "Public Enemy" )
            setGenre( "Hip Hop" )
            val bundle = Bundle()
            calendar.add( Calendar.DAY_OF_WEEK, 1 )
            bundle.putLong( DATE_KEY, calendar.timeInMillis )
            setExtras( bundle )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Cold Lampin' with Flavor" )
            setAlbumTitle( "It Takes a Nation of Millions to Hold Us Back" )
            setArtist( "Public Enemy" )
            setGenre( "Hip Hop" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "1969" )
            setAlbumTitle( "The Stooges" )
            setArtist( "the Stooges" )
            setGenre( "Rock" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "I Wanna Be Your Dog" )
            setAlbumTitle( "The Stooges" )
            setArtist( "the Stooges" )
            setGenre( "Rock" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "We Will Fall" )
            setAlbumTitle( "The Stooges" )
            setArtist( "the Stooges" )
            setGenre( "Rock" )
        }.build() )
    }.build()
)