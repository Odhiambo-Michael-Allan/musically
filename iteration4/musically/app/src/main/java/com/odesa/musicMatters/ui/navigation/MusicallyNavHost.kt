package com.odesa.musicMatters.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.albumArtists.AlbumArtistsScreen
import com.odesa.musicMatters.ui.albumArtists.AlbumArtistsViewModel
import com.odesa.musicMatters.ui.albumArtists.AlbumArtistsViewModelFactory
import com.odesa.musicMatters.ui.albums.AlbumsScreen
import com.odesa.musicMatters.ui.albums.AlbumsViewModel
import com.odesa.musicMatters.ui.albums.AlbumsViewModelFactory
import com.odesa.musicMatters.ui.artists.ArtistsScreen
import com.odesa.musicMatters.ui.artists.ArtistsViewModel
import com.odesa.musicMatters.ui.artists.ArtistsViewModelFactory
import com.odesa.musicMatters.ui.components.swipeable
import com.odesa.musicMatters.ui.folders.FoldersScreen
import com.odesa.musicMatters.ui.folders.FoldersViewModel
import com.odesa.musicMatters.ui.folders.FoldersViewModelFactory
import com.odesa.musicMatters.ui.forYou.ForYouScreen
import com.odesa.musicMatters.ui.forYou.ForYouViewModel
import com.odesa.musicMatters.ui.forYou.ForYouViewModelFactory
import com.odesa.musicMatters.ui.genres.GenreScreenViewModel
import com.odesa.musicMatters.ui.genres.GenreScreenViewModelFactory
import com.odesa.musicMatters.ui.genres.GenresScreen
import com.odesa.musicMatters.ui.playlists.PlaylistsScreen
import com.odesa.musicMatters.ui.playlists.PlaylistsViewModel
import com.odesa.musicMatters.ui.playlists.PlaylistsViewModelFactory
import com.odesa.musicMatters.ui.queue.QueueScreen
import com.odesa.musicMatters.ui.queue.QueueScreenViewModel
import com.odesa.musicMatters.ui.queue.QueueScreenViewModelFactory
import com.odesa.musicMatters.ui.settings.SettingsScreen
import com.odesa.musicMatters.ui.settings.SettingsViewModel
import com.odesa.musicMatters.ui.settings.SettingsViewModelFactory
import com.odesa.musicMatters.ui.songs.SongsScreen
import com.odesa.musicMatters.ui.songs.SongsScreenViewModel
import com.odesa.musicMatters.ui.songs.SongsViewModelFactory
import com.odesa.musicMatters.ui.tree.TreeScreen
import com.odesa.musicMatters.ui.tree.TreeViewModel
import com.odesa.musicMatters.ui.tree.TreeViewModelFactory

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun MusicallyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    musicServiceConnection: MusicServiceConnection,
    visibleTabs: Set<HomeTab>,
    language: Language,
    labelVisibility: HomePageBottomBarLabelVisibility,
    nowPlayingBottomBar: @Composable () -> Unit,
) {

    var showTabsSheet by remember { mutableStateOf( false ) }
    var currentTab by remember { mutableStateOf( visibleTabs.first().toDestination() ) }
    var navigationTriggeredFromBottomBar = false

    navController.addOnDestinationChangedListener { _, destination, _ ->
        Destination.entries.forEach {
            if ( !navigationTriggeredFromBottomBar ) {
                if ( destination.route == it.route.name )
                    currentTab = it
            }
        }
        navigationTriggeredFromBottomBar = false
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.ForYou.name
    ) {
        composable(
            Route.ForYou.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val forYouViewModel: ForYouViewModel = viewModel(
                factory = ForYouViewModelFactory(
                    settingsRepository
                )
            )
            ForYouScreen(
                viewModel = forYouViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Songs.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val songsScreenViewModel: SongsScreenViewModel = viewModel(
                factory = SongsViewModelFactory(
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository,
                    musicServiceConnection = musicServiceConnection
                )
            )
            SongsScreen(
                songsScreenViewModel = songsScreenViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Artists.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val artistsViewModel: ArtistsViewModel = viewModel(
                factory = ArtistsViewModelFactory()
            )
            ArtistsScreen(
                viewModel = artistsViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Albums.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val albumsViewModel: AlbumsViewModel = viewModel(
                factory = AlbumsViewModelFactory()
            )
            AlbumsScreen(
                viewModel = albumsViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.AlbumArtists.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val albumArtistsViewModel: AlbumArtistsViewModel = viewModel(
                factory = AlbumArtistsViewModelFactory()
            )
            AlbumArtistsScreen(
                viewModel = albumArtistsViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Genres.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val genreScreenViewModel: GenreScreenViewModel = viewModel(
                factory = GenreScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository
                )
            )
            GenresScreen(
                viewModel = genreScreenViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Folders.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val foldersViewModel: FoldersViewModel = viewModel(
                factory = FoldersViewModelFactory()
            )
            FoldersScreen(
                viewModel = foldersViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Playlists.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val playlistsViewModel: PlaylistsViewModel = viewModel(
                factory = PlaylistsViewModelFactory()
            )
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Tree.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
        ) {
            val treeViewModel: TreeViewModel = viewModel(
                factory = TreeViewModelFactory()
            )
            TreeScreen(
                viewModel = treeViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Route.Queue.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { SlideTransition.slideDown.exitTransition() },
        ) {
            val queueScreenViewModel: QueueScreenViewModel = viewModel(
                factory = QueueScreenViewModelFactory(
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository,
                    musicServiceConnection = musicServiceConnection
                )
            )
            QueueScreen(
                queueScreenViewModel = queueScreenViewModel,
                onBackArrowClick = { navController.navigateUp() }
            )
        }
        composable(
            Route.Settings.name,
            enterTransition= { ScaleTransition.scaleDown.enterTransition() },
            exitTransition = { ScaleTransition.scaleUp.exitTransition() }
        ) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(
                    settingsRepository
                )
            )
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
    Column {
        nowPlayingBottomBar()
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures {
                        showTabsSheet = true
                    }
                }
                .swipeable(
                    onSwipeUp = {
                        showTabsSheet = true
                    }
                )
        ) {
            Spacer( modifier = Modifier.width( 2.dp ) )
            visibleTabs.map { it.toDestination() }.forEach { tab ->
                val isSelected = currentTab == tab
                val label = tab.label( language )
                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                    onClick = {
                        showTabsSheet = isSelected
                        navigationTriggeredFromBottomBar = true
                        navController.navigate( tab.route )
                        currentTab = tab
                    },
                    icon = {
                        Crossfade(
                            targetState = isSelected,
                            label = "home-bottom-bar"
                        ) {
                            Icon(
                                imageVector = if ( it ) tab.selectedIcon() else tab.unselectedIcon(),
                                contentDescription = tab.iconContentDescription
                            )
                        }
                    },
                    label = when ( labelVisibility ) {
                        HomePageBottomBarLabelVisibility.INVISIBLE -> null
                        else -> ( {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = false,
                            )
                        } )
                    }
                )
            }
            Spacer( modifier = Modifier.width( 2.dp ) )
        }

        if ( showTabsSheet ) {
            val sheetState = rememberModalBottomSheetState()
            val orderedTabs = remember {
                setOf( *visibleTabs.map { it.toDestination() }.toTypedArray(),
                    *Destination.entries.toTypedArray() )
            }

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showTabsSheet = false }
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.padding( 6.dp ),
                    columns = GridCells.Fixed( visibleTabs.size ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.spacedBy( 8.dp ),
                ) {
                    items( orderedTabs.toList(), key = { it.ordinal } ) {
                        val isSelected = it == currentTab
                        val label = it.label( language )
                        val containerColor = when {
                            isSelected -> MaterialTheme.colorScheme.secondaryContainer
                            else -> Color.Unspecified
                        }
                        val contentColor = when {
                            isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> Color.Unspecified
                        }
                        Column (
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp, 0.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    navController.navigate(it.route)
                                    showTabsSheet = false
                                }
                                .background(containerColor)
                                .padding(0.dp, 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when {
                                isSelected -> Icon(
                                    imageVector = it.selectedIcon(),
                                    contentDescription = it.iconContentDescription,
                                    tint = contentColor
                                )
                                else -> Icon(
                                    imageVector = it.unselectedIcon(),
                                    contentDescription = it.iconContentDescription
                                )
                            }
                            Spacer( modifier = Modifier.height( 8.dp ) )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall.copy( color = contentColor ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer( modifier = Modifier.height( 48.dp ) )
            }
        }
    }

}

fun HomeTab.toDestination() = when( this ) {
    HomeTab.ForYou -> Destination.ForYou
    HomeTab.Songs -> Destination.Songs
    HomeTab.Tree -> Destination.Tree
    HomeTab.Playlists -> Destination.Playlists
    HomeTab.Folders -> Destination.Folders
    HomeTab.Artists -> Destination.Artists
    HomeTab.AlbumArtists -> Destination.AlbumArtists
    HomeTab.Albums -> Destination.Albums
    HomeTab.Genres -> Destination.Genres
}


