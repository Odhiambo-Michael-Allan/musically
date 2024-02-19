package com.odesa.musically.services.media.library

import android.os.Bundle
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * A set of Android integration tests for ( primarily ) [AbstractMusicSource]. The tests all use an
 * extension of [AbstractMusicSource] which is defined at the bottom of this file: [FakeMusicSource]
 */
@RunWith( RobolectricTestRunner::class )
class AbstractMusicSourceTest {

    private val musicList = listOf<MediaItem>(
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
                setGenre( "FoRocklk" )
            }.build() )
        }.build()
    )

    private lateinit var testSource: FakeMusicSource

    @Before
    fun setup() {
        testSource = FakeMusicSource( musicList )
    }


    @Test
    fun testWhenSourceSuccessfullyInitializes_listenersAreNotified() {
        var waiting = true
        testSource.whenReady {
            assertTrue( it )
            waiting = false
        }
        assertTrue( waiting )
        testSource.prepare()
        assertFalse( waiting )
    }

    @Test
    fun testWhenErrorOccursWhileInitializing_listenersAreNotified() {
        var waiting = true
        testSource.whenReady {
            assertFalse( it )
            waiting = false
        }
        assertTrue( waiting )
        testSource.error()
        assertFalse( waiting )
    }

    @Test
    fun testSearchByGenre() {
        testSource.prepare()
        val searchQuery = "Rock"
        val searchExtras = Bundle().apply {
            putString( MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE )
            putString( MediaStore.EXTRA_MEDIA_GENRE, searchQuery )
        }
        val result = testSource.search( searchQuery, searchExtras )
        assertEquals( 1, result.size )
    }

    @Test
    fun testSearchByMedia() {
        testSource.prepare()
        val searchQuery = "About a Guy"
        val searchExtras = Bundle().apply {
            putString( MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Media.ENTRY_CONTENT_TYPE )
            putString( MediaStore.EXTRA_MEDIA_TITLE, searchQuery )
            putString( MediaStore.EXTRA_MEDIA_ALBUM, "Tales from the Render Farm" )
            putString( MediaStore.EXTRA_MEDIA_ARTIST, "7 Developers and a Pastry Chef" )
        }
        val result = testSource.search( searchQuery, searchExtras )
        assertEquals( 1, result.size )
    }

    @Test
    fun testSearchByMedia_noMatches() {
        testSource.prepare()
        val searchQuery = "Kotlin in 31 Days"
        val searchExtras = Bundle().apply {
            putString( MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Media.ENTRY_CONTENT_TYPE )
            putString( MediaStore.EXTRA_MEDIA_TITLE, searchQuery )
            putString( MediaStore.EXTRA_MEDIA_ALBUM, "Delegated by lazy" )
            putString( MediaStore.EXTRA_MEDIA_ARTIST, "Brainiest Jet" )
        }
        val result = testSource.search( searchQuery, searchExtras )
        assertEquals( 0, result.size )
    }

    @Test
    fun testSearchByKeyword_fallback() {
        testSource.prepare()
        val searchQuery = "hasse"
        val searchExtras = Bundle.EMPTY
        val result = testSource.search( searchQuery, searchExtras )
        assertEquals( 1, result.size )
    }
}

class FakeMusicSource (
    private val music: List<MediaItem>
) : AbstractMusicSource(), Iterable<MediaItem> by music {

    override suspend fun load() = Unit

    fun prepare() {
        state = STATE_INITIALIZED
    }

    fun error() {
        state = STATE_ERROR
    }
}