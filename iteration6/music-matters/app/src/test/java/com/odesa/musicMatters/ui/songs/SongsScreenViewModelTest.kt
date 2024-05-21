package com.odesa.musicMatters.ui.songs

import android.net.Uri
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.id1
import com.odesa.musicMatters.fakes.testMediaItems
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
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
class SongsScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var viewModel: SongsScreenViewModel
    private lateinit var playlistRepository: PlaylistRepository

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        viewModel = SongsScreenViewModel(
            settingsRepository = settingsRepository,
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", viewModel.uiState.value.language.settings )
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
    fun testMediaItemsAreCorrectlyLoadedFromTheMusicServiceConnection() {
        assertTrue( viewModel.uiState.value.isLoading )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoading )
        assertEquals( 6, viewModel.uiState.value.songs.size )
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
    fun testSongIsCorrectlyRemovedFromFavoriteList() = runTest {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, viewModel.uiState.value.favoriteSongIds.size )
        viewModel.addToFavorites( testSongs.first().id )
        assertEquals( testSongs.size - 1, viewModel.uiState.value.favoriteSongIds.size )
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
    fun testSongsAreSortedCorrectly() = runTest {
        musicServiceConnection.setIsInitialized()

        // -- Sort by title --
        // Ascending
        musicServiceConnection.setSongs( testSongsForSorting )
        assertEquals( "id1", viewModel.uiState.value.songs.first().id )
        // Descending
        viewModel.setSortSongsInReverse( true )
        assertEquals( "id5", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsInReverse( false )
        viewModel.setSortSongsBy( SortSongsBy.ALBUM )
        assertEquals( "id5", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.ARTIST )
        assertEquals( "id1", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.COMPOSER )
        assertEquals( "id5", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.DURATION )
        assertEquals( "id2", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.YEAR )
        assertEquals( "id5", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.DATE_ADDED )
        assertEquals( "id4", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.FILENAME )
        assertEquals( "id4", viewModel.uiState.value.songs.first().id )
        viewModel.setSortSongsBy( SortSongsBy.TRACK_NUMBER )
        assertEquals( "id5", viewModel.uiState.value.songs.first().id )
    }
}

val testSongsForSorting = listOf(
    Song(
        id = "id1",
        mediaUri = Uri.EMPTY,
        title = "song-1",
        albumTitle = "D",
        artists = setOf( "A", "B" ),
        artworkUri = null,
        composer = "A,B",
        dateModified = 354L,
        displayTitle = "song-1",
        duration = 60L,
        trackNumber = 324,
        year = 2022,
        genre = "Rap",
        size = 1L,
        mediaItem = MediaItem.EMPTY,
        path = "/path/to/song/7"
    ),
    Song(
        id = "id2",
        mediaUri = Uri.EMPTY,
        title = "song-2",
        albumTitle = "C",
        artists = setOf( "B", "C" ),
        artworkUri = null,
        composer = "B,C",
        dateModified = 754L,
        displayTitle = "song-2",
        duration = 4L,
        trackNumber = 235,
        year = 2002,
        genre = "Hip Hop",
        size = 2L,
        mediaItem = MediaItem.EMPTY,
        path = "/path/to/song/8"
    ),
    Song(
        id = "id3",
        mediaUri = Uri.EMPTY,
        title = "song-3",
        albumTitle = "B",
        artists = setOf( "C", "D" ),
        artworkUri = null,
        composer = "C,D",
        dateModified = 7976L,
        displayTitle = "song-3",
        duration = 7L,
        trackNumber = 443,
        year = 2007,
        genre = "RnB",
        size = 3L,
        mediaItem = MediaItem.EMPTY,
        path = "/path/to/song/6"
    ),
    Song(
        id = "id4",
        mediaUri = Uri.EMPTY,
        title = "song-4",
        albumTitle = "A",
        artists = setOf( "D", "E" ),
        artworkUri = null,
        composer = "D,E",
        dateModified = 200L,
        displayTitle = "song-4",
        duration = 4L,
        trackNumber = 234,
        year = 2004,
        genre = "Dance",
        size = 4L,
        mediaItem = MediaItem.EMPTY,
        path = "/path/to/song/1"
    ),
    Song(
        id = "id5",
        mediaUri = Uri.EMPTY,
        title = "song-5",
        albumTitle = "<unknown>",
        artists = setOf( "E", "F" ),
        artworkUri = null,
        composer = null,
        dateModified = 34245L,
        displayTitle = "song-3",
        duration = 89L,
        trackNumber =134,
        year = 1990,
        genre = "<unknown>",
        size = 5L,
        mediaItem = MediaItem.EMPTY,
        path = "/path/to/song/5"
    ),
)