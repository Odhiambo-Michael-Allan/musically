package com.odesa.musically.services.media.library

import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import com.odesa.musically.services.media.extensions.album
import com.odesa.musically.services.media.extensions.artist
import com.odesa.musically.services.media.extensions.genre
import com.odesa.musically.services.media.extensions.id
import com.odesa.musically.services.media.extensions.title
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * A set of Android integration tests for ( primarily ) [AbstractMusicSource]. The tests all use an
 * extension of [AbstractMusicSource] which is defined at the bottom of this file: [TestMusicSource]
 */
@RunWith( RobolectricTestRunner::class )
class AbstractMusicSourceTest {

    private val musicList = listOf<MediaMetadataCompat>(
        MediaMetadataCompat.Builder().apply {
            id = 1L
            title = "Ich hasse dich"
            album = "Speechless"
            artist = "Jemand"
            genre = "Folk"
        }.build(),

        MediaMetadataCompat.Builder().apply {
            id = 2L
            title = "About a Guy"
            album = "Tales from the Render Farm"
            artist = "7 Developers and a Pastry Chef"
            genre = "Rock"
        }.build()
    )

    private lateinit var testSource: TestMusicSource

    @Before
    fun setup() {
        testSource = TestMusicSource( musicList )
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

class TestMusicSource(
    private val music: List<MediaMetadataCompat>
) : AbstractMusicSource(), Iterable<MediaMetadataCompat> by music {

    override suspend fun load() = Unit

    fun prepare() {
        state = STATE_INITIALIZED
    }

    fun error() {
        state = STATE_ERROR
    }


}