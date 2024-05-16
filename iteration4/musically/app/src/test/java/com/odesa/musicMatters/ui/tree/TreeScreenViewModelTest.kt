package com.odesa.musicMatters.ui.tree

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.trackList
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
        musicServiceConnection.runWhenInitialized {
            assertEquals( 5, viewModel.uiState.value.tree.keys.size )
            assertEquals( trackList.size, viewModel.uiState.value.songsCount )
            assertFalse( viewModel.uiState.value.isConstructingTree )
        }
        musicServiceConnection.isInitialized = true
    }

    @Test
    fun testCurrentlyPlayingSongIdIsCorrectlyUpdated() {
        musicServiceConnection.setNowPlaying( trackList.first() )
        assertEquals( trackList.first().mediaId, viewModel.uiState.value.currentlyPlayingSongId )
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

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
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
    fun testPlaylistsAreCorrectlyUpdated() = runTest {
        assertEquals( 1, viewModel.uiState.value.playlists.size )
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( testPlaylists.size + 1, viewModel.uiState.value.playlists.size )
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
}

val testPaths = listOf(
    "/storage/emulated/0/Music/Madeon/All My Friends/Madeon - All My Friends.mp3",
    "/storage/emulated/0/Music/Bea Miller/elated!/Bea Miller, Aminé - FEEL SOMETHING DIFFERENT.mp3",
    "/storage/emulated/0/Music/Tove Lo/Queen Of The Clouds/Tove Lo - Talking Body.mp3",
    "/storage/emulated/0/Music/Flume/Skin/Flume, Tove Lo - Say It.mp3",
    "/storage/emulated/0/Music/Sean Paul/I'm Still in Love with You/Sean Paul, Sasha - I'm Still in Love with You (feat. Sasha).mp3",
    "/storage/emulated/0/Music/Sean Paul/Tek Weh Yuh Heart/Sean Paul, Tory Lanez - Tek Weh Yuh Heart.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake, J Balvin, Tyga - Loco Contigo.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake - Magenta Riddim.mp3",
    "/storage/emulated/0/Music/DJ Snake/Carte Blanche/DJ Snake, Eptic - SouthSide.mp3",
    "/storage/emulated/0/Music/Sean Paul/Calling On Me/Sean Paul, Tove Lo - Calling On Me.mp3"
)