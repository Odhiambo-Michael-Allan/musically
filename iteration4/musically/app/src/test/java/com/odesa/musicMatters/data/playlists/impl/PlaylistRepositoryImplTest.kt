package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.fakes.FakePlaylistStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class PlaylistRepositoryImplTest {

    private lateinit var playlistStore: PlaylistStore
    private lateinit var playlistRepository: PlaylistRepository

    @Before
    fun setup() {
        playlistStore = FakePlaylistStore()
        playlistRepository = PlaylistRepositoryImpl( playlistStore )
    }

    @Test
    fun whenNoFavoritesPlaylistHasPreviouslyBeenSaved_emptyPlaylistIsReturned() {
        val favoritesPlaylist = playlistRepository.favoritesPlaylist.value
        assertEquals( 0, favoritesPlaylist.numberOfTracks )
    }

    @Test
    fun testSongIdsAreCorrectlyAddedToFavoritesPlaylist() = runTest {
        for ( i in 0 until 100 )
            playlistRepository.addToFavorites( UUID.randomUUID().toString() )
        assertEquals( 100, playlistRepository.favoritesPlaylist.value.numberOfTracks )
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
        assertEquals( 99, playlistRepository.favoritesPlaylist.value.numberOfTracks )
        playlistRepository.removeFromFavorites( songIdsToBeAdded.last() )
        assertEquals( 98, playlistRepository.favoritesPlaylist.value.numberOfTracks )
    }

    @Test
    fun testFavoriteSongIdIsCorrectlyIdentified() = runTest {
        val songIdsToBeAdded = List( 10 ) {
            UUID.randomUUID().toString()
        }
        songIdsToBeAdded.forEach {
            playlistRepository.addToFavorites( it )
        }
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.first() ) )
        assertTrue( playlistRepository.isFavorite( songIdsToBeAdded.last() ) )
        assertFalse( playlistRepository.isFavorite( "random_string" ) )
    }

    @Test
    fun testSavePlaylist() = runTest {
        val songIdsToBeAdded = List( 100 ) {
            UUID.randomUUID().toString()
        }.toSet()
        val playlistsToBeAdded = List( 100 ) {
            Playlist(
                id = UUID.randomUUID().toString(),
                title = "Playlist $it",
                songIds = songIdsToBeAdded.toSet(),
                numberOfTracks = songIdsToBeAdded.size
            )
        }
        playlistsToBeAdded.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( playlistsToBeAdded.size, playlistRepository.playlists.value.size )
    }
}