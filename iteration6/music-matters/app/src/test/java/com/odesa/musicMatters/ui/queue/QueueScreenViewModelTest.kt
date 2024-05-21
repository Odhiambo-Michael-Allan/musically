package com.odesa.musicMatters.ui.queue

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.id1
import com.odesa.musicMatters.fakes.testMediaItems
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.extensions.toSong
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
    private lateinit var viewModel: QueueScreenViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        viewModel = QueueScreenViewModel(
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
            musicServiceConnection = musicServiceConnection
        )
    }

    @Test
    fun testMediaItemsChange() {
        musicServiceConnection.setMediaItems( emptyList() )
        assertEquals( 0, viewModel.uiState.value.songsInQueue.size )
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( testMediaItems.size,
            viewModel.uiState.value.songsInQueue.size )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals("", viewModel.uiState.value.currentlyPlayingSongId)
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, viewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, viewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, viewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testAddSongToFavorites() {
        viewModel.addToFavorites( testSongs.first().id )
        assertEquals( 1, viewModel.uiState.value.favoriteSongIds.size )
        viewModel.addToFavorites( testSongs.first().id )
        assertEquals( 0, viewModel.uiState.value.favoriteSongIds.size )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals(0, viewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals(
            testSongs.size,
            viewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testClearQueue() {
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( testMediaItems.size, viewModel.uiState.value.songsInQueue.size )
        viewModel.clearQueue()
        assertEquals( 0, viewModel.uiState.value.songsInQueue.size )
    }

    @Test
    fun testSaveCurrentPlaylist() {
        val playlistName = "playlist-1"
        musicServiceConnection.setMediaItems( testMediaItems )
        viewModel.createPlaylist( playlistName, testMediaItems.map { it.toSong( artistTagSeparators ) } )
        assertEquals( 4, playlistRepository.playlists.value.size )
    }

    @Test
    fun testCurrentlyPlayingSongIndexIsCorrectlyUpdated() {
        assertEquals( 0,
            viewModel.uiState.value.currentlyPlayingSongIndex )
        musicServiceConnection.setCurrentMediaItemIndex( 10 )
        assertEquals( 10,
            viewModel.uiState.value.currentlyPlayingSongIndex )
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