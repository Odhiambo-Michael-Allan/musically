package com.odesa.musicMatters.ui.forYou

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.trackList
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
class ForYouScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var playlistRepository: FakePlaylistRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: ForYouScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        settingsRepository = FakeSettingsRepository()
        viewModel = ForYouScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository,
            settingsRepository = settingsRepository
        )
    }

    @Test
    fun testRecentlyAddedSongsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertTrue( viewModel.uiState.value.isLoadingRecentSongs )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoadingRecentSongs )
        assertEquals( 5, viewModel.uiState.value.recentlyAddedSongs.size )
    }

    @Test
    fun testSuggestedAlbumsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertTrue( viewModel.uiState.value.isLoadingSuggestedAlbums )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoadingSuggestedAlbums )
        assertEquals( 4, viewModel.uiState.value.suggestedAlbums.size )
    }

    @Test
    fun testSuggestedArtistsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertTrue( viewModel.uiState.value.isLoadingSuggestedArtists )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoadingSuggestedArtists )
        assertEquals( 5, viewModel.uiState.value.suggestedArtists.size )
    }

    @Test
    fun testRecentlyPlayedSongsAreCorrectlyUpdated() = runTest {
        musicServiceConnection.setIsInitialized()
        assertEquals( 0, viewModel.uiState.value.recentlyPlayedSongs.size )
        assertFalse( viewModel.uiState.value.isLoadingRecentlyPlayedSongs )
        trackList.forEach {
            playlistRepository.addToRecentlyPlayedSongsPlaylist( it.id )
        }
        assertEquals( 5, viewModel.uiState.value.recentlyPlayedSongs.size )
        assertFalse( viewModel.uiState.value.isLoadingRecentlyPlayedSongs )
        playlistRepository.addToRecentlyPlayedSongsPlaylist( trackList.last().id )
        assertEquals( trackList.last().id, viewModel.uiState.value.recentlyPlayedSongs.first().id )
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( trackList.first().id )
        playlistRepository.removeFromRecentlyPlayedSongsPlaylist( trackList.last().id )
        assertEquals( 4, viewModel.uiState.value.recentlyPlayedSongs.size )
    }

    @Test
    fun testShuffleAndPlay() = runTest {
        musicServiceConnection.setIsInitialized()
        viewModel.shuffleAndPlay()
        assertEquals( trackList.size, musicServiceConnection.mediaItemsInQueue.value.size )
    }


}