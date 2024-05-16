package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.fakes.FakePlaylistStore
import com.odesa.musicMatters.services.media.testSongs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith( RobolectricTestRunner::class )
class PlaylistRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var playlistStore: PlaylistStore
    private lateinit var playlistRepository: PlaylistRepository

    @Before
    fun setup() {
        playlistStore = FakePlaylistStore()
        playlistRepository = PlaylistRepositoryImpl( playlistStore )
    }

    @Test
    fun testPlaylistRepositoryInitiallyHasThreePlaylists_favoritesRecentsAndMostPlayedPlaylist() {
        assertEquals( 3, playlistRepository.playlists.value.size )
    }

    @Test
    fun whenNoFavoritesPlaylistHasPreviouslyBeenSaved_emptyPlaylistIsReturned() {
        val favoritesPlaylist = playlistRepository.favoritesPlaylist.value
        assertEquals( 0, favoritesPlaylist.songIds.size )
    }

    @Test
    fun testSongIdsAreCorrectlyAddedToFavoritesPlaylist() = runTest {
        for ( i in 0 until 100 )
            playlistRepository.addToFavorites( UUID.randomUUID().toString() )
        assertEquals( 100, playlistRepository.favoritesPlaylist.value.songIds.size )
    }

    @Test
    fun testSongIdsAreCorrectlyRemovedFromFavoritesPlaylist() = runTest {
        val songIdsToBeAdded = List( 100 ) {
            UUID.randomUUID().toString()
        }
        songIdsToBeAdded.forEach {
            playlistRepository.addToFavorites( it )
        }
        playlistRepository.removeFromFavorites( songIdsToBeAdded.first() )
        assertEquals( 99, playlistRepository.favoritesPlaylist.value.songIds.size )
        playlistRepository.removeFromFavorites( songIdsToBeAdded.last() )
        assertEquals( 98, playlistRepository.favoritesPlaylist.value.songIds.size )
    }

    @Test
    fun testFavoriteSongIdIsCorrectlyIdentified() = runTest {
        songIdsToBeAdded.forEach {
            playlistRepository.addToFavorites( it )
        }
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.first() ) )
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.last() ) )
        assertFalse( playlistRepository.isFavorite( "random_string" ) )
    }

    @Test
    fun testAddToRecentlyPlayedSongsPlaylist() = runTest {
        songIdsToBeAdded.forEach {
            playlistRepository.addToRecentlyPlayedSongsPlaylist( it )
        }
        assertEquals( songIdsToBeAdded.size, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
        playlistRepository.addToRecentlyPlayedSongsPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
        assertEquals( songIdsToBeAdded.last(), playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.first() )
    }

    @Test
    fun testRemoveFromRecentlyPlayedSongsPlaylist() = runTest {
        songIdsToBeAdded.forEach {
            playlistRepository.addToRecentlyPlayedSongsPlaylist( it )
        }
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( songIdsToBeAdded.first() )
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size - 2, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testAddToMostPlayedPlaylist() = runTest {
        songIdsToBeAdded.forEach {
            playlistRepository.addToMostPlayedPlaylist( it )
        }
        assertEquals( songIdsToBeAdded.size, playlistRepository.mostPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testRemoveFromMostPlayedPlaylist() = runTest {
        songIdsToBeAdded.forEach {
            playlistRepository.addToMostPlayedPlaylist( it )
        }
        playlistRepository.removeFromMostPlayedPlaylist( songIdsToBeAdded.first() )
        playlistRepository.removeFromMostPlayedPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size - 2, playlistRepository.mostPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testSavePlaylist() = runTest {
        customPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( customPlaylists.size + 3, playlistRepository.playlists.value.size )
    }

    @Test
    fun testDeletePlaylist() = runTest {
        customPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        playlistRepository.deletePlaylist( customPlaylists.first() )
        playlistRepository.deletePlaylist( customPlaylists.last() )
        assertEquals( ( customPlaylists.size - 2 ) + 3, playlistRepository.playlists.value.size )
    }

    @Test
    fun testFetchMostPlayedSongsMap() {
        assertEquals( 0, playlistRepository.mostPlayedSongsMap.value.size )
    }

    @Test
    fun testMostPlayedSongsMapIsCorrectlyUpdated() = runTest {
        testSongs.forEach {
            playlistStore.addToMostPlayedSongsPlaylist( it.id )
        }
        assertEquals( testSongs.size, playlistRepository.mostPlayedSongsMap.value.size )
        playlistStore.removeFromMostPlayedSongsPlaylist( testSongs.first().id )
        assertEquals( testSongs.size - 1, playlistRepository.mostPlayedSongsMap.value.size )
    }

    @Test
    fun testSongIsCorrectlyRemovedFromFavoriteList() = runTest {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, playlistRepository.favoritesPlaylist.value.songIds.size )
        playlistRepository.addToFavorites( testSongs.first().id )
        assertEquals( testSongs.size - 1, playlistRepository.favoritesPlaylist.value.songIds.size )
    }
}

val songIdsToBeAdded = List( 10 ) {
    UUID.randomUUID().toString()
}