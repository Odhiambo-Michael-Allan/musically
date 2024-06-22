package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.data.playlists.impl.PlaylistRepositoryImpl
import com.odesa.musicMatters.core.datatesting.playlist.FakePlaylistStore
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith( RobolectricTestRunner::class )
class PlaylistRepositoryImplTest {

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
    fun testSongIdsAreCorrectlyAddedToFavoritesPlaylist() {
        for ( i in 0 until 100 )
            playlistRepository.addToFavorites( UUID.randomUUID().toString() )
        assertEquals( 100, playlistRepository.favoritesPlaylist.value.songIds.size )
    }

    @Test
    fun testSongIdsAreCorrectlyRemovedFromFavoritesPlaylist() {
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
    fun testFavoriteSongIdIsCorrectlyIdentified() {
        songIdsToBeAdded.forEach {
            playlistRepository.addToFavorites( it )
        }
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.first() ) )
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.last() ) )
        assertFalse( playlistRepository.isFavorite( "random_string" ) )
    }

    @Test
    fun testAddToRecentlyPlayedSongsPlaylist() {
        songIdsToBeAdded.forEach {
            playlistRepository.addToRecentlyPlayedSongsPlaylist( it )
        }
        assertEquals( songIdsToBeAdded.size, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
        playlistRepository.addToRecentlyPlayedSongsPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
        assertEquals( songIdsToBeAdded.last(), playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.first() )
    }

    @Test
    fun testRemoveFromRecentlyPlayedSongsPlaylist() {
        songIdsToBeAdded.forEach {
            playlistRepository.addToRecentlyPlayedSongsPlaylist( it )
        }
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( songIdsToBeAdded.first() )
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size - 2, playlistRepository.recentlyPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testAddToMostPlayedPlaylist() {
        songIdsToBeAdded.forEach {
            playlistRepository.addToMostPlayedPlaylist( it )
        }
        assertEquals( songIdsToBeAdded.size, playlistRepository.mostPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testRemoveFromMostPlayedPlaylist() {
        songIdsToBeAdded.forEach {
            playlistRepository.addToMostPlayedPlaylist( it )
        }
        playlistRepository.removeFromMostPlayedPlaylist( songIdsToBeAdded.first() )
        playlistRepository.removeFromMostPlayedPlaylist( songIdsToBeAdded.last() )
        assertEquals( songIdsToBeAdded.size - 2, playlistRepository.mostPlayedSongsPlaylist.value.songIds.size )
    }

    @Test
    fun testSavePlaylist() {
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( testPlaylists.size + 3, playlistRepository.playlists.value.size )
    }

    @Test
    fun testDeletePlaylist() {
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        playlistRepository.deletePlaylist( testPlaylists.first() )
        playlistRepository.deletePlaylist( testPlaylists.last() )
        assertEquals( ( testPlaylists.size - 2 ) + 3, playlistRepository.playlists.value.size )
    }

    @Test
    fun testFetchMostPlayedSongsMap() {
        assertEquals( 0, playlistRepository.mostPlayedSongsMap.value.size )
    }

    @Test
    fun testMostPlayedSongsMapIsCorrectlyUpdated() {
        testSongs.forEach {
            playlistStore.addToMostPlayedSongsPlaylist( it.id )
        }
        assertEquals( testSongs.size, playlistRepository.mostPlayedSongsMap.value.size )
        playlistStore.removeFromMostPlayedSongsPlaylist( testSongs.first().id )
        assertEquals( testSongs.size - 1, playlistRepository.mostPlayedSongsMap.value.size )
    }

    @Test
    fun testSongIsCorrectlyRemovedFromFavoriteList() {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, playlistRepository.favoritesPlaylist.value.songIds.size )
        playlistRepository.addToFavorites( testSongs.first().id )
        assertEquals( testSongs.size - 1, playlistRepository.favoritesPlaylist.value.songIds.size )
    }

    @Test
    fun testPlaylistsAreRenamedCorrectly() {
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        playlistRepository.renamePlaylist( testPlaylists.first(), "mob-deep" )
        assertTrue( playlistStore.fetchAllPlaylists().map { it.title }.contains( "mob-deep" ) )
        assertTrue( playlistRepository.playlists.value.map { it.title }.contains( "mob-deep" ) )
    }

    @Test
    fun testSongsIdsAreCorrectlyAddedToCurrentPlayingQueuePlaylist() {
        assertTrue( playlistRepository.currentPlayingQueuePlaylist.value.songIds.isEmpty() )
        playlistRepository.saveCurrentQueue( songIdsToBeAdded )
        assertEquals(
            songIdsToBeAdded.size,
            playlistStore.fetchCurrentPlayingQueue().songIds.size
        )
        assertEquals(
            songIdsToBeAdded.size,
            playlistRepository.currentPlayingQueuePlaylist.value.songIds.size
        )
    }

    @Test
    fun testClearCurrentPlayingQueuePlaylist() {
        playlistRepository.saveCurrentQueue( songIdsToBeAdded )
        playlistRepository.clearCurrentPlayingQueuePlaylist()
        assertTrue( playlistRepository.currentPlayingQueuePlaylist.value.songIds.isEmpty() )
    }
}

val songIdsToBeAdded = testSongs.map { it.id }