package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.core.data.FileAdapter
import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.data.playlists.impl.PlaylistStoreImpl
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.util.UUID

@RunWith( RobolectricTestRunner::class )
class PlaylistStoreImplTest {

    private val playlistFile = File( "playlists-json-test-file.json" )
    private val mostPlayedSongsFile = File( "most-played-songs-test-file.json" )
    private lateinit var playlistStore: PlaylistStore

    @Before
    fun setup() {
        playlistFile.createNewFile()
        mostPlayedSongsFile.createNewFile()
        playlistStore = PlaylistStoreImpl(
            playlistFile,
            mostPlayedSongsFile
        )
    }

    @After
    fun cleanup() {
        playlistFile.delete()
        mostPlayedSongsFile.delete()
    }

    @Test
    fun testWhenNoPlaylistsHadInitiallyBeenSavedFavoritesRecentsAndMostPlayedPlaylistsAreReturnedByDefault() = runTest {
        val playlists = playlistStore.fetchAllPlaylists()
        assertEquals( 3, playlists.size )
        assertEquals( 1, playlists.filter { it.title == "Favorites" }.size )
        assertEquals( 1, playlists.filter { it.title == "Most Played Songs" }.size )
        assertEquals( 1, playlists.filter { it.title == "Recently Played Songs" }.size )
    }

    @Test
    fun testFetchFavoriteSongsPlaylist() = runTest {
        val favoritesPlaylist = playlistStore.fetchFavoritesPlaylist()
        assertNotNull( favoritesPlaylist )
        assertEquals( 0, favoritesPlaylist.songIds.size )
    }

    @Test
    fun testAddToFavoriteSongsPlaylist() = runTest {
        playlistStore.addToFavorites( UUID.randomUUID().toString() )
        assertTrue( FileAdapter( playlistFile ).read().isNotEmpty() )
        assertEquals( 1, playlistStore.fetchFavoritesPlaylist().songIds.size )
        playlistStore.addSongIdToPlaylist( UUID.randomUUID().toString(), playlistStore.fetchFavoritesPlaylist() )
        assertEquals( 2, playlistStore.fetchFavoritesPlaylist().songIds.size )
    }

    @Test
    fun testRemoveFromFavoriteSongsPlaylist() = runTest {
        ( 0..5 ).forEach { _ ->
            playlistStore.addToFavorites( UUID.randomUUID().toString() )
        }
        assertEquals( 6, playlistStore.fetchFavoritesPlaylist().songIds.size )
        playlistStore.removeFromFavorites( playlistStore.fetchFavoritesPlaylist().songIds.first() )
        playlistStore.removeFromFavorites( playlistStore.fetchFavoritesPlaylist().songIds.first() )
        assertEquals( 4, playlistStore.fetchFavoritesPlaylist().songIds.size )
    }

    @Test
    fun testFetchRecentlyPlayedSongsPlaylist() = runTest {
        val recentSongsPlaylist = playlistStore.fetchRecentlyPlayedSongsPlaylist()
        assertNotNull( recentSongsPlaylist )
        assertEquals( 0, recentSongsPlaylist.songIds.size )
    }

    @Test
    fun testAddToRecentlyPlayedSongsPlaylist() = runTest {
        testSongs.forEach {
            playlistStore.addSongIdToRecentlyPlayedSongsPlaylist( it.id )
        }
        val recentSongsPlaylist = playlistStore.fetchRecentlyPlayedSongsPlaylist()
        assertEquals( testSongs.size, recentSongsPlaylist.songIds.size )
        playlistStore.addSongIdToRecentlyPlayedSongsPlaylist( testSongs.last().id )
        assertEquals( testSongs.size, playlistStore.fetchRecentlyPlayedSongsPlaylist().songIds.size )
        assertEquals( testSongs.last().id, playlistStore.fetchRecentlyPlayedSongsPlaylist().songIds.first() )
    }

    @Test
    fun testRemoveFromRecentlyPlayedSongsPlaylist() = runTest {
        testSongs.forEach {
            playlistStore.addSongIdToRecentlyPlayedSongsPlaylist( it.id )
        }
        playlistStore.removeFromRecentlyPlayedSongsPlaylist( testSongs.first().id )
        playlistStore.removeFromRecentlyPlayedSongsPlaylist( testSongs[3].id )
        val recentSongsPlaylist = playlistStore.fetchRecentlyPlayedSongsPlaylist()
        assertEquals( testSongs.size - 2, recentSongsPlaylist.songIds.size )
    }

    @Test
    fun testFetchMostPlayedSongsPlaylist() = runTest {
        val mostPlayedSongsPlaylist = playlistStore.fetchMostPlayedSongsPlaylist()
        assertNotNull( mostPlayedSongsPlaylist )
        assertEquals( 0, mostPlayedSongsPlaylist.songIds.size )
    }

    @Test
    fun testAddToMostPlayedSongsPlaylist() = runTest {
        testSongs.forEach {
            playlistStore.addToMostPlayedSongsPlaylist( it.id )
        }
        val mostPlayedSongsPlaylist = playlistStore.fetchMostPlayedSongsPlaylist()
        assertEquals( testSongs.size, mostPlayedSongsPlaylist.songIds.size )
    }

    @Test
    fun testRemoveFromMostPlayedSongsPlaylist() = runTest {
        testSongs.forEach {
            playlistStore.addToMostPlayedSongsPlaylist( it.id )
        }
        playlistStore.removeFromMostPlayedSongsPlaylist( testSongs.first().id )
        assertEquals( testSongs.size - 1, playlistStore.fetchMostPlayedSongsPlaylist().songIds.size )
    }

    @Test
    fun testSongsInMostPlayedSongsPlaylistAreSortedInDescendingOrderFromMostPlayedToLeastPlayed() = runTest {
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.first().id )
        playlistStore.addToMostPlayedSongsPlaylist( testSongs[1].id )
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.last().id )
        ( 0..5 ).forEach { _ ->
            playlistStore.addToMostPlayedSongsPlaylist( testSongs.first().id )
        }
        ( 0..3 ).forEach { _ ->
            playlistStore.addToMostPlayedSongsPlaylist( testSongs.last().id )
        }
        val mostPlayedSongsPlaylist = playlistStore.fetchMostPlayedSongsPlaylist()
        assertEquals( testSongs.first().id, mostPlayedSongsPlaylist.songIds.first() )
        assertEquals( testSongs.last().id, mostPlayedSongsPlaylist.songIds.elementAt( 1 ) )
    }

    @Test
    fun testSaveCustomPlaylist() = runTest {
        testPlaylists.forEach {
            playlistStore.savePlaylist( it )
        }
        assertEquals( 23, playlistStore.fetchAllPlaylists().size )
    }

    @Test
    fun testDeleteCustomPlaylist() = runTest {
        testPlaylists.forEach {
            playlistStore.savePlaylist( it )
        }
        playlistStore.deletePlaylist( testPlaylists.first() )
        playlistStore.deletePlaylist( testPlaylists[2] )
        assertEquals( 21, playlistStore.fetchAllPlaylists().size )
    }

    @Test
    fun testAddSongToCustomPlaylist() = runTest {
        testPlaylists.forEach {
            playlistStore.savePlaylist( it )
        }
        testSongs.forEach {
            playlistStore.addSongIdToPlaylist( it.id, testPlaylists.first() )
        }
        testSongs.forEach {
            playlistStore.addSongIdToPlaylist( it.id, testPlaylists.last() )
        }
        playlistStore.addSongIdToPlaylist( testSongs.first().id, testPlaylists[1] )
        playlistStore.addSongIdToPlaylist( testSongs[1].id, testPlaylists[1] )
        val storedCustomPlaylists = playlistStore.fetchAllPlaylists()
        assertEquals( testSongs.size, storedCustomPlaylists.find { it.id == testPlaylists.first().id }!!.songIds.size )
        assertEquals( 2, storedCustomPlaylists.find { it.id == testPlaylists[1].id }!!.songIds.size )
        assertEquals( testSongs.size, storedCustomPlaylists.find { it.id == testPlaylists.last().id }!!.songIds.size )
    }

    @Test
    fun testFetchMostPlayedSongsMap() = runTest {
        testSongs.forEach {
            playlistStore.addToMostPlayedSongsPlaylist( it.id )
        }
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.first().id )
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.first().id )
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.first().id )
        playlistStore.addToMostPlayedSongsPlaylist( testSongs.last().id )
        val mostPlayedSongsMap = playlistStore.fetchMostPlayedSongsMap()
        assertEquals( 4, mostPlayedSongsMap[ testSongs.first().id ] )
        assertEquals( 2, mostPlayedSongsMap[ testSongs.last().id ] )
    }

    @Test
    fun testPlaylistsAreCorrectlyRenamed() = runTest {
        testPlaylists.forEach {
            playlistStore.savePlaylist( it )
        }
        playlistStore.renamePlaylist( testPlaylists.first(), "mob-deep" )
        val playlists = playlistStore.fetchAllPlaylists()
        assertTrue( playlists.map { it.title }.contains( "mob-deep" ) )
    }

}

