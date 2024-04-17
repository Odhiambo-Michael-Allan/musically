package com.odesa.musicMatters.services.media.library

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.odesa.musicMatters.fakes.FakeMusicSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
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
}

val musicList = listOf(
    MediaItem.Builder().apply {
        setMediaId( "ich_hasse_dich" )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Ich hasse dich" )
            setAlbumTitle( "Speechless" )
            setArtist( "Jemand" )
            setGenre( "Folk" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( "about_a_guy" )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "About a guy" )
            setAlbumTitle( "Tales from the Render Farm" )
            setArtist( "7 Developers and a Pastry Chef" )
            setGenre( "Folk" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Beat it" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Billie Jean" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Human Nature" )
            setAlbumTitle( "Thriller" )
            setArtist( "Michael Jackson" )
            setGenre( "Pop" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Bring the Noise" )
            setAlbumTitle( "It Takes a Nation of Millions to Hold Us Back" )
            setArtist( "Public Enemy" )
            setGenre( "Hip Hop" )
        }.build() )
    }.build(),
    MediaItem.Builder().apply {
        setMediaId( UUID.randomUUID().toString() )
        setMediaMetadata( MediaMetadata.Builder().apply {
            setTitle( "Don't Believe the Hype" )
            setAlbumTitle( "It Takes a Nation of Millions to Hold Us Back" )
            setArtist( "Public Enemy" )
            setGenre( "Hip Hop" )
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