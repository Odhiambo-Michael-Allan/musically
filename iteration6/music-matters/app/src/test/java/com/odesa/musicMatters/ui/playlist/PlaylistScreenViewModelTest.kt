package com.odesa.musicMatters.ui.playlist

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.id1
import com.odesa.musicMatters.fakes.testMediaItems
import com.odesa.musicMatters.fakes.trackList
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

@RunWith( RobolectricTestRunner::class )
class PlaylistScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var viewModel: PlaylistScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        viewModel = PlaylistScreenViewModel(
            playlistId = playlistRepository.favoritesPlaylist.value.id,
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLoadSongsInPlaylistWithGivenId() = runTest {
        assertEquals( 0, viewModel.uiState.value.songsInPlaylist.size )
        assertTrue( viewModel.uiState.value.isLoadingSongsInPlaylist )
        trackList.subList( 0, 3 ).forEach {
            playlistRepository.addToFavorites( it.id )
        }
        musicServiceConnection.setIsInitialized()
        assertEquals( 3, viewModel.uiState.value.songsInPlaylist.size )
        assertFalse( viewModel.uiState.value.isLoadingSongsInPlaylist )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", viewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, viewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, viewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, viewModel.uiState.value.favoriteSongIds.size )
    }

    @Test
    fun testPlaylistsAreCorrectlyUpdated() = runTest {
        assertEquals( 1, viewModel.uiState.value.playlists.size )
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( testPlaylists.size + 1, viewModel.uiState.value.playlists.size )
    }

}