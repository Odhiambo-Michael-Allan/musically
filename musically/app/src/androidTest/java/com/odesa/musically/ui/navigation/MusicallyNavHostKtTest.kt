package com.odesa.musically.ui.navigation

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
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.odesa.musically.data.FakeSettingsRepository
import com.odesa.musically.data.FakeSongsRepository
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MusicallyNavHostKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var songsRepository: SongsRepository

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        songsRepository = FakeSongsRepository()

        composeTestRule.setContent {
            navController = TestNavHostController( LocalContext.current )
            navController.navigatorProvider.addNavigator( ComposeNavigator() )
            MusicallyNavHost(
                navController = navController,
                settingsRepository = settingsRepository,
                songsRepository = songsRepository,
                visibleTabs = SettingsDefaults.homeTabs,
                labelVisibility = SettingsDefaults.homePageBottomBarLabelVisibility,
                language = SettingsDefaults.language,
            ) {}
        }
    }

    @Test
    fun testForYouIsTheStartDestination() {
        composeTestRule
            .onNodeWithText( Destination.ForYou.label( English ) )
            .assertIsDisplayed()
    }

    @Test
    fun testNavigateToSongsScreen() {
        composeTestRule.onRoot().printToLog( "test-navigate-to-songs-screen" )
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Songs.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Songs.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToAlbumsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Albums.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Albums.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToArtistsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Artists.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Artists.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToAlbumArtistsScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Destination.AlbumArtists.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.AlbumArtists.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToGenresScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Genres.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Genres.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToFoldersScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Folders.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Folders.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToPlaylistsScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Playlists.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Playlists.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    @Test
    fun testNavigateToTreeScreen() {
        showBottomSheet()
        composeTestRule
            .onNodeWithContentDescription(
                Destination.Tree.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
        composeTestRule
            .onNode(
                hasText( Destination.Tree.label( English ) ) and
                hasParent( hasContentDescription( "top-app-bar" ) ),
                useUnmergedTree = true
            ).assertIsDisplayed()
    }

    private fun showBottomSheet() {
        composeTestRule
            .onNodeWithContentDescription(
                Destination.ForYou.iconContentDescription,
                useUnmergedTree = true
            ).performClick()
    }
}