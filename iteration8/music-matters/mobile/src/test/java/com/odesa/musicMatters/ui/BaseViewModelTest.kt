package com.odesa.musicMatters.ui

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.connection.FakeMusicServiceConnection
import com.odesa.musicMatters.core.datatesting.playlist.FakePlaylistRepository
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.repository.FakeSettingsRepository
import com.odesa.musicMatters.core.model.Playlist
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class BaseViewModelTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var viewModel: BaseViewModel

    private lateinit var currentPlaylists: List<Playlist>

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        viewModel = BaseViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        )
    }

    @Test
    fun testPlaylistsAreUpdatedCorrectly() = runTest {
        viewModel.addOnPlaylistsChangeListener {
            currentPlaylists = it
        }
        assertEquals( 1, currentPlaylists.size )
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( 1 + testPlaylists.size, currentPlaylists.size )
    }

    @Test
    fun testPlaylistIsRenamedCorrectly() = runTest {
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        viewModel.addOnPlaylistsChangeListener {
            currentPlaylists = it
        }
        viewModel.renamePlaylist( testPlaylists.first(), "chill-rnb" )
        assertTrue( currentPlaylists.map { it.title }.contains( "chill-rnb" ) )
    }

}