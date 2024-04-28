package com.odesa.musicMatters.ui.forYou

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class ForYouScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var playlistRepository: FakePlaylistRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var forYouScreenViewModel: ForYouScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        settingsRepository = FakeSettingsRepository()
        forYouScreenViewModel = ForYouScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository,
            settingsRepository = settingsRepository
        )
    }

    @Test
    fun testRecentlyAddedSongsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertEquals( 0, forYouScreenViewModel.uiState.value.recentlyAddedSongs.size )
        assertTrue( forYouScreenViewModel.uiState.value.isLoadingRecentSongs )
        musicServiceConnection.runWhenInitialized {
            assertEquals( 5, forYouScreenViewModel.uiState.value.recentlyAddedSongs.size )
        }
        musicServiceConnection.isInitialized = true
    }

    @Test
    fun testSuggestedAlbumsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertEquals( 0, forYouScreenViewModel.uiState.value.suggestedAlbums.size )
        assertTrue( forYouScreenViewModel.uiState.value.isLoadingSuggestedAlbums )
        musicServiceConnection.runWhenInitialized {
            assertEquals( 4, forYouScreenViewModel.uiState.value.suggestedAlbums.size )
        }
        musicServiceConnection.isInitialized = true
    }

//    @Test
//    fun testMostPlayedSongsAreCorrectlyUpdated() = runTest {
//        musicServiceConnection.isInitialized = true
//        assertEquals( 0, forYouScreenViewModel.uiState.value.mostPlayedSongs.size )
//        assertFalse( forYouScreenViewModel.uiState.value.isLoadingMostPlayedSongs )
//        trackList.subList( 0, 4 ).forEach {
//            playlistRepository.addToMostPlayedPlaylist( it.mediaId )
//        }
//        assertEquals( 4, forYouScreenViewModel.uiState.value.mostPlayedSongs.size )
//        playlistRepository.addToMostPlayedPlaylist( trackList.last().mediaId )
//        assertEquals( 5, forYouScreenViewModel.uiState.value.mostPlayedSongs.size )
//        playlistRepository.removeFromMostPlayedPlaylist( trackList.first().mediaId )
//        playlistRepository.removeFromMostPlayedPlaylist( trackList.last().mediaId )
//        assertEquals( 4, forYouScreenViewModel.uiState.value.mostPlayedSongs.size )
//    }

    @Test
    fun testSuggestedArtistsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertEquals( 0, forYouScreenViewModel.uiState.value.suggestedArtists.size )
        assertTrue( forYouScreenViewModel.uiState.value.isLoadingSuggestedArtists )
        musicServiceConnection.isInitialized = true
        musicServiceConnection.runWhenInitialized {
            assertEquals( 5, forYouScreenViewModel.uiState.value.suggestedArtists.size )
        }
    }
}