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
import com.odesa.musicMatters.ui.album.AlbumScreen
import com.odesa.musicMatters.ui.album.AlbumScreenViewModel
import com.odesa.musicMatters.ui.album.AlbumScreenViewModelFactory
import com.odesa.musicMatters.ui.albums.AlbumsScreen
import com.odesa.musicMatters.ui.albums.AlbumsScreenViewModel
import com.odesa.musicMatters.ui.albums.AlbumsViewModelFactory
import com.odesa.musicMatters.ui.artists.ArtistsScreen
import com.odesa.musicMatters.ui.artists.ArtistsViewModel
import com.odesa.musicMatters.ui.artists.ArtistsViewModelFactory
import com.odesa.musicMatters.ui.components.swipeable
import com.odesa.musicMatters.ui.folders.FoldersScreen
import com.odesa.musicMatters.ui.folders.FoldersViewModel
import com.odesa.musicMatters.ui.folders.FoldersViewModelFactory
import com.odesa.musicMatters.ui.forYou.ForYouScreen
import com.odesa.musicMatters.ui.forYou.ForYouScreenViewModel
import com.odesa.musicMatters.ui.forYou.ForYouViewModelFactory
import com.odesa.musicMatters.ui.genre.GenreScreen
import com.odesa.musicMatters.ui.genre.GenreScreenViewModel
import com.odesa.musicMatters.ui.genre.GenreScreenViewModelFactory
import com.odesa.musicMatters.ui.genres.GenresScreen
import com.odesa.musicMatters.ui.genres.GenresScreenViewModel
import com.odesa.musicMatters.ui.genres.GenresScreenViewModelFactory
import com.odesa.musicMatters.ui.playlist.PlaylistScreen
import com.odesa.musicMatters.ui.playlist.PlaylistScreenViewModel
import com.odesa.musicMatters.ui.playlist.PlaylistScreenViewModelFactory
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
        SupportedDestinations.forEach {
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
            exitTransition = { FadeTransition.exitTransition() }
        ) {
            val forYouScreenViewModel: ForYouScreenViewModel = viewModel(
                factory = ForYouViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    playlistRepository = playlistRepository,
                    settingsRepository = settingsRepository
                )
            )
            ForYouScreen(
                viewModel = forYouScreenViewModel,
                onSettingsClicked = { navController.navigate( Route.Settings.name ) },
                onSuggestedAlbumClick = { navController.navigateToAlbumScreen( it.name ) }
            )
        }
        composable(
            route = Route.Songs.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
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
            exitTransition = { FadeTransition.exitTransition() }
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
            exitTransition = { FadeTransition.exitTransition() }
        ) {
            val albumsScreenViewModel: AlbumsScreenViewModel = viewModel(
                factory = AlbumsViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository
                )
            )
            AlbumsScreen(
                viewModel = albumsScreenViewModel,
                onAlbumClick = { navController.navigateToAlbumScreen( it ) },
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Album.routeWithArgs,
            arguments = Album.arguments,
            enterTransition = { SlideTransition.slideLeft.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
        ) { navBackStackEntry ->
            val albumScreenViewModel: AlbumScreenViewModel = viewModel(
                factory = AlbumScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository
                )
            )
            // Retrieve the passed argument
            val albumName = navBackStackEntry.getRouteArgument(
                RouteParameters.ALBUM_ROUTE_ALBUM_NAME ) ?: ""
            albumScreenViewModel.loadSongsInAlbum( albumName )
            AlbumScreen(
                albumName = albumName,
                albumScreenViewModel = albumScreenViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = Route.Genres.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
        ) {
            val genresScreenViewModel: GenresScreenViewModel = viewModel(
                factory = GenresScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository
                )
            )
            GenresScreen(
                viewModel = genresScreenViewModel,
                onGenreClick = { navController.navigateToGenreScreen( it ) },
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Genre.routeWithArgs,
            arguments = Genre.arguments,
            enterTransition = { SlideTransition.slideLeft.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() },
        ) { navBackStackEntry ->
            val genreScreenViewModel: GenreScreenViewModel = viewModel(
                factory = GenreScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository,
                )
            )
            // Retrieve the passed argument
            val genreName = navBackStackEntry.getRouteArgument(
                RouteParameters.GENRE_ROUTE_GENRE_NAME ) ?: ""
            genreScreenViewModel.loadSongsWithGenre( genreName )
            GenreScreen(
                genreName = genreName,
                genreScreenViewModel = genreScreenViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = Route.Folders.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
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
            exitTransition = { FadeTransition.exitTransition() }
        ) {
            val playlistsViewModel: PlaylistsViewModel = viewModel(
                factory = PlaylistsViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository,
                )
            )
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                onPlaylistClick = { playlistId, playlistName -> navController.navigateToPlaylistScreen( playlistId, playlistName ) },
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Playlist.routeWithArgs,
            arguments = Playlist.arguments,
            enterTransition = { SlideTransition.slideLeft.enterTransition() },
            exitTransition = { SlideTransition.slideRight.exitTransition() }
        ) { navBackStackEntry ->
            val playlistScreenViewModel: PlaylistScreenViewModel = viewModel(
                factory = PlaylistScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository,
                    playlistsRepository = playlistRepository
                )
            )
            val playlistId = navBackStackEntry.getRouteArgument(
                RouteParameters.PLAYLIST_ROUTE_PLAYLIST_ID
            ) ?: ""
            val playlistName = navBackStackEntry.getRouteArgument(
                RouteParameters.PLAYLIST_ROUTE_PLAYLIST_NAME
            ) ?: ""
            playlistScreenViewModel.loadSongsInPlaylistWithId( playlistId )
            PlaylistScreen(
                playlistTitle = playlistName,
                viewModel = playlistScreenViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = Route.Tree.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
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
            exitTransition = { FadeTransition.exitTransition() }
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
            exitTransition = { FadeTransition.exitTransition() }
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
                val label = tab.getLabel( language )
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
                                imageVector = if ( it ) tab.selectedIcon else tab.unselectedIcon,
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
                    *SupportedDestinations.toTypedArray() )
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
                    items( orderedTabs.toList(), key = { orderedTabs.indexOf( it ) } ) {
                        val isSelected = it == currentTab
                        val label = it.getLabel( language )
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
                                    imageVector = it.selectedIcon,
                                    contentDescription = it.iconContentDescription,
                                    tint = contentColor
                                )
                                else -> Icon(
                                    imageVector = it.unselectedIcon,
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
    HomeTab.ForYou -> ForYou
    HomeTab.Songs -> Songs
    HomeTab.Tree -> Tree
    HomeTab.Playlists -> Playlists
    HomeTab.Folders -> Folders
    HomeTab.Artists -> Artists
    HomeTab.Albums -> Albums
    HomeTab.Genres -> Genres
}


