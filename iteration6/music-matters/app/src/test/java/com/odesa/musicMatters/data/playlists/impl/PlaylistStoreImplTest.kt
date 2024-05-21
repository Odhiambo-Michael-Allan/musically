package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.services.media.testSongs
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
        customPlaylists.forEach {
            playlistStore.saveCustomPlaylist( it )
        }
        assertEquals( 20, playlistStore.fetchAllCustomPlaylists().size )
    }

    @Test
    fun testDeleteCustomPlaylist() = runTest {
        customPlaylists.forEach {
            playlistStore.saveCustomPlaylist( it )
        }
        playlistStore.deleteCustomPlaylist( customPlaylists.first() )
        playlistStore.deleteCustomPlaylist( customPlaylists[2] )
        assertEquals( 21, playlistStore.fetchAllPlaylists().size )
    }

    @Test
    fun testAddSongToCustomPlaylist() = runTest {
        customPlaylists.forEach {
            playlistStore.saveCustomPlaylist( it )
        }
        testSongs.forEach {
            playlistStore.addSongIdToPlaylist( it.id, customPlaylists.first() )
        }
        testSongs.forEach {
            playlistStore.addSongIdToPlaylist( it.id, customPlaylists.last() )
        }
        playlistStore.addSongIdToPlaylist( testSongs.first().id, customPlaylists[1] )
        playlistStore.addSongIdToPlaylist( testSongs[1].id, customPlaylists[1] )
        val storedCustomPlaylists = playlistStore.fetchAllCustomPlaylists()
        assertEquals( testSongs.size, storedCustomPlaylists.find { it.id == customPlaylists.first().id }!!.songIds.size )
        assertEquals( 2, storedCustomPlaylists.find { it.id == customPlaylists[1].id }!!.songIds.size )
        assertEquals( testSongs.size, storedCustomPlaylists.find { it.id == customPlaylists.last().id }!!.songIds.size )
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

}

val customPlaylists = List( 20 ) {
    Playlist(
        id = UUID.randomUUID().toString() + "$it",
        title = "Playlist-$it",
        songIds = emptyList()
    )
}.toMutableList()