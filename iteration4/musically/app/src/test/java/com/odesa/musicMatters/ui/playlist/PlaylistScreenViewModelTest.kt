package com.odesa.musicMatters.ui.playlist

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.playlists.PlaylistRepository
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
    private lateinit var playlistScreenViewModel: PlaylistScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        playlistScreenViewModel = PlaylistScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLoadSongsInPlaylistWithGivenId() = runTest {
        trackList.subList( 0, 3 ).forEach {
            playlistRepository.addToFavorites( it.mediaId )
        }
        playlistScreenViewModel.loadSongsInPlaylistWithId( playlistRepository.favoritesPlaylist.value.id )
        musicServiceConnection.runWhenInitialized {
            assertEquals( 3, playlistScreenViewModel.uiState.value.songsInPlaylist.size )
            assertFalse( playlistScreenViewModel.uiState.value.isLoadingSongsInPlaylist )
        }
        musicServiceConnection.isInitialized = true
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", playlistScreenViewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, playlistScreenViewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, playlistScreenViewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, playlistScreenViewModel.uiState.value.favoriteSongIds.size )
    }

}