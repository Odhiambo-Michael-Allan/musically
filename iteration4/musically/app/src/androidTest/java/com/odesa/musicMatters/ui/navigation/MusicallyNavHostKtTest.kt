package com.odesa.musicMatters.ui.navigation

import android.os.Bundle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.common.collect.ImmutableList
import com.odesa.musicMatters.data.FakePlaylistRepository
import com.odesa.musicMatters.data.FakeSettingsRepository
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.NOTHING_PLAYING
import com.odesa.musicMatters.services.media.connection.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MusicallyNavHostKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var musicServiceConnection: MusicServiceConnection

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        musicServiceConnection = FakeMusicServiceConnection()

        composeTestRule.setContent {
            navController = TestNavHostController( LocalContext.current )
            navController.navigatorProvider.addNavigator( ComposeNavigator() )
            MusicallyNavHost(
                navController = navController,
                settingsRepository = settingsRepository,
                musicServiceConnection = musicServiceConnection,
                visibleTabs = SettingsDefaults.homeTabs,
                labelVisibility = SettingsDefaults.homePageBottomBarLabelVisibility,
                language = SettingsDefaults.language,
                playlistRepository = playlistRepository
            ) {}
        }
    }

    @Test
    fun testForYouIsTheStartDestination() {
        composeTestRule
            .onNodeWithText( ForYou.getLabel( English ) )
            .assertIsDisplayed()
    }

    @Test
    fun testNavigateToSongsScreen() {
        composeTestRule.onRoot().printToLog( "test-navigate-to-songs-screen" )
        composeTestRule
            .onNodeWithContentDescription(
                Songs.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Songs.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToAlbumsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Albums.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Albums.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToArtistsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Artists.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Artists.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToGenresScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Genres.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Genres.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToFoldersScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Folders.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Folders.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToPlaylistsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Playlists.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Playlists.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToTreeScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Tree.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Tree.getLabel( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    private fun showBottomSheet() {
        composeTestRule
            .onNodeWithContentDescription(
                ForYou.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
    }
}

class FakeMusicServiceConnection : MusicServiceConnection {

    private val _nowPlaying = MutableStateFlow( NOTHING_PLAYING )
    override val nowPlaying = _nowPlaying.asStateFlow()

    override val playbackState: StateFlow<PlaybackState>
        get() = TODO("Not yet implemented")
    override val queueSize: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val currentlyPlayingMediaItemIndex: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val isPlaying: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val player: Player?
        get() = TODO("Not yet implemented")
    override val mediaItemsInQueue: StateFlow<List<MediaItem>>
        get() = TODO("Not yet implemented")
    override var isInitialized: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    override val isInitializedListeners: MutableList<() -> Unit>
        get() = TODO("Not yet implemented")

    override fun runWhenInitialized(fn: () -> Unit) {}

    override suspend fun getChildren( parentId: String ): ImmutableList<MediaItem> =
        ImmutableList.of<MediaItem>()

    override suspend fun sendCommand(command: String, parameters: Bundle?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: (Int, Bundle?) -> Unit
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun playMediaItem(
        mediaItem: MediaItem,
        mediaItems: List<MediaItem>,
        shuffle: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun setPlaybackSpeed(playbackSpeed: Float) {
        TODO("Not yet implemented")
    }

    override fun setPlaybackPitch(playbackPitch: Float) {
        TODO("Not yet implemented")
    }

    override fun setRepeatMode(repeatMode: Int) {
        TODO("Not yet implemented")
    }

    override fun shuffleSongsInQueue() {
        TODO("Not yet implemented")
    }

    override fun moveMediaItem(from: Int, to: Int) {
        TODO("Not yet implemented")
    }

    override fun clearQueue() {
        TODO("Not yet implemented")
    }

}