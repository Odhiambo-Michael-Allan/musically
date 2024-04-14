package com.odesa.musicMatters.ui.queue

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.id1
import com.odesa.musicMatters.fakes.testMediaItems
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class QueueScreenViewModelTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var queueScreenViewModel: QueueScreenViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        queueScreenViewModel = QueueScreenViewModel(
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
            musicServiceConnection = musicServiceConnection
        )
    }

    @Test
    fun testMediaItemsChange() {
        musicServiceConnection.setMediaItems( emptyList() )
        assertEquals( 0, queueScreenViewModel.uiState.value.songsInQueue.size )
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( testMediaItems.size,
            queueScreenViewModel.uiState.value.songsInQueue.size )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals("", queueScreenViewModel.uiState.value.currentlyPlayingSongId)
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, queueScreenViewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, queueScreenViewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, queueScreenViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testAddSongToFavorites() {
        queueScreenViewModel.addToFavorites( testSongs.first().id )
        assertEquals( 1, queueScreenViewModel.uiState.value.favoriteSongIds.size )
        queueScreenViewModel.addToFavorites( testSongs.first().id )
        assertEquals( 0, queueScreenViewModel.uiState.value.favoriteSongIds.size )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals(0, queueScreenViewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals(
            testSongs.size,
            queueScreenViewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testClearQueue() {
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( testMediaItems.size, queueScreenViewModel.uiState.value.songsInQueue.size )
        queueScreenViewModel.clearQueue()
        assertEquals( 0, queueScreenViewModel.uiState.value.songsInQueue.size )
    }

    @Test
    fun testSaveCurrentPlaylist() {
        val playlistName = "playlist-1"
        musicServiceConnection.setMediaItems( testMediaItems )
        queueScreenViewModel.saveCurrentPlaylist( playlistName )
        assertEquals( testMediaItems.size, playlistRepository.playlists.value.size )
        assertEquals( playlistName, playlistRepository.playlists.value.first().title )
    }

}