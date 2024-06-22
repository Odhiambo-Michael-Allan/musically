package com.odesa.musicMatters.ui.tree

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.connection.FakeMusicServiceConnection
import com.odesa.musicMatters.core.datatesting.playlist.FakePlaylistRepository
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.repository.FakeSettingsRepository
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.datatesting.tree.testPaths
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.Belarusian
import com.odesa.musicMatters.core.i8n.Chinese
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.French
import com.odesa.musicMatters.core.i8n.German
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.i8n.Spanish
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.io.path.Path
import kotlin.io.path.name

@RunWith( RobolectricTestRunner::class )
class TreeScreenViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var viewModel: TreeScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        viewModel = TreeScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testTreeIsProperlyConstructed() {
        assertEquals( 5, viewModel.uiState.value.tree.keys.size )
        assertEquals( testSongs.size, viewModel.uiState.value.songsCount )
        assertTrue( viewModel.uiState.value.isConstructingTree )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isConstructingTree )
    }

    @Test
    fun testCurrentlyPlayingSongIdIsCorrectlyUpdated() {
        musicServiceConnection.setNowPlaying( testSongs.first().mediaItem )
        assertEquals( testSongs.first().mediaItem.mediaId, viewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings",
            viewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = viewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
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
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, viewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, viewModel.uiState.value.favoriteSongIds.size )
    }

    @Test
    fun testDisabledTreePathsChange() = runTest {
        assertEquals( 0, viewModel.uiState.value.disabledTreePaths.size )
        settingsRepository.setCurrentlyDisabledTreePaths( testPaths.subList( 0, 3 ) )
        assertEquals( 3, viewModel.uiState.value.disabledTreePaths.size )
    }

    @Test
    fun testTogglePath() = runTest {
        settingsRepository.setCurrentlyDisabledTreePaths( testPaths )
        viewModel.togglePath( testPaths.first() )
        assertEquals( testPaths.size - 1, viewModel.uiState.value.disabledTreePaths.size )
        assertEquals( testPaths.size - 1, settingsRepository.currentlyDisabledTreePaths.value.size )
        viewModel.togglePath( testPaths.first() )
        assertEquals( testPaths.size, viewModel.uiState.value.disabledTreePaths.size )
        assertEquals( testPaths.size, settingsRepository.currentlyDisabledTreePaths.value.size )
    }

    @Test
    fun testPath() {
        val path1 = Path( testPaths.first() )
        val path2 = Path( testPaths[1] )
        val path3 = Path( testPaths[2] )
        val path4 = Path( testPaths[3] )
        val path5 = Path( testPaths[4] )
        assertEquals(
            "/storage/emulated/0/Music/Madeon/All My Friends",
            path1.directoryName()
        )
        assertEquals(
            "Madeon - All My Friends.mp3".trim(),
            path1.fileName.name
        )
        assertEquals(
            "/storage/emulated/0/Music/Bea Miller/elated!",
            path2.directoryName()
        )
        assertEquals(
            "/storage/emulated/0/Music/Tove Lo/Queen Of The Clouds",
            path3.directoryName()
        )
        assertEquals(
            "/storage/emulated/0/Music/Flume/Skin",
            path4.directoryName()
        )
        assertEquals(
            "/storage/emulated/0/Music/Sean Paul/I'm Still in Love with You",
            path5.directoryName()
        )
    }

    @Test
    fun testPlaylistsAreCorrectlyUpdated() = runTest {
        assertEquals( 1, viewModel.uiState.value.playlists.size )
        testPlaylists.forEach { playlistRepository.savePlaylist( it ) }
        assertEquals( 1 + testPlaylists.size, viewModel.uiState.value.playlists.size )
    }
}

