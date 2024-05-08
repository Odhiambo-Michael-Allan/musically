package com.odesa.musicMatters.ui.tree

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.trackList
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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