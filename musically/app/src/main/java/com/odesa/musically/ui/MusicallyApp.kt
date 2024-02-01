package com.odesa.musically.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.storage.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musically.data.storage.preferences.HomeTab
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.albumArtists.AlbumArtistsScreen
import com.odesa.musically.ui.albumArtists.AlbumArtistsViewModel
import com.odesa.musically.ui.albumArtists.AlbumArtistsViewModelFactory
import com.odesa.musically.ui.albums.AlbumsScreen
import com.odesa.musically.ui.albums.AlbumsViewModel
import com.odesa.musically.ui.albums.AlbumsViewModelFactory
import com.odesa.musically.ui.artists.ArtistsScreen
import com.odesa.musically.ui.artists.ArtistsViewModel
import com.odesa.musically.ui.artists.ArtistsViewModelFactory
import com.odesa.musically.ui.components.swipeable
import com.odesa.musically.ui.folders.FoldersScreen
import com.odesa.musically.ui.folders.FoldersViewModel
import com.odesa.musically.ui.folders.FoldersViewModelFactory
import com.odesa.musically.ui.forYou.ForYouScreen
import com.odesa.musically.ui.forYou.ForYouViewModel
import com.odesa.musically.ui.forYou.ForYouViewModelFactory
import com.odesa.musically.ui.genres.GenresScreen
import com.odesa.musically.ui.genres.GenresViewModel
import com.odesa.musically.ui.genres.GenresViewModelFactory
import com.odesa.musically.ui.helpers.Route
import com.odesa.musically.ui.helpers.ScaleTransition
import com.odesa.musically.ui.helpers.SlideTransition
import com.odesa.musically.ui.helpers.navigate
import com.odesa.musically.ui.playlists.PlaylistsScreen
import com.odesa.musically.ui.playlists.PlaylistsViewModel
import com.odesa.musically.ui.playlists.PlaylistsViewModelFactory
import com.odesa.musically.ui.settings.SettingsScreen
import com.odesa.musically.ui.settings.SettingsViewModel
import com.odesa.musically.ui.settings.SettingsViewModelFactory
import com.odesa.musically.ui.songs.SongsScreen
import com.odesa.musically.ui.songs.SongsViewModel
import com.odesa.musically.ui.songs.SongsViewModelFactory
import com.odesa.musically.ui.theme.MusicallyTheme
import com.odesa.musically.ui.tree.TreeScreen
import com.odesa.musically.ui.tree.TreeViewModel
import com.odesa.musically.ui.tree.TreeViewModelFactory

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    songsRepository: SongsRepository
) {
    val themeMode by settingsRepository.themeMode.collectAsState()
    val primaryColorName by settingsRepository.primaryColorName.collectAsState()
    val font by settingsRepository.font.collectAsState()
    val fontScale by settingsRepository.fontScale.collectAsState()
    val useMaterialYou by settingsRepository.useMaterialYou.collectAsState()

    MusicallyTheme(
        themeMode = themeMode,
        primaryColorName = primaryColorName,
        fontName = font.name,
        fontScale = fontScale,
        useMaterialYou = useMaterialYou
    ) {
        Surface( color = MaterialTheme.colorScheme.background ) {
            MusicallyAppContent(
                navController = navController,
                settingsRepository = settingsRepository,
                songsRepository = songsRepository,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    songsRepository: SongsRepository,
) {
    var showTabsSheet by remember { mutableStateOf( false ) }
    val homeTabs by settingsRepository.homeTabs.collectAsState()
    var currentTab by remember { mutableStateOf( homeTabs.first().toHomeUiPage() ) }
    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(
            modifier = Modifier.weight( 1f ),
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
                val songsViewModel: SongsViewModel = viewModel(
                    factory = SongsViewModelFactory( settingsRepository, songsRepository )
                )
                SongsScreen(
                    viewModel = songsViewModel,
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
                val genresViewModel: GenresViewModel = viewModel(
                    factory = GenresViewModelFactory()
                )
                GenresScreen(
                    viewModel = genresViewModel,
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
            homeTabs.map { it.toHomeUiPage() }.forEach { tab ->
                val isSelected = currentTab == tab
                val label = tab.label( language )
                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                    onClick = {
                        showTabsSheet = isSelected
                        currentTab = tab
                        navController.navigate( tab.route )
                    },
                    icon = {
                        Crossfade(
                            targetState = isSelected,
                            label = "home-bottom-bar"
                        ) {
                            Icon(
                                imageVector = if ( it ) tab.selectedIcon() else tab.unselectedIcon(),
                                contentDescription = label
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
                setOf( *homeTabs.map { it.toHomeUiPage() }.toTypedArray(), *HomeUiPage.entries.toTypedArray() )
            }

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showTabsSheet = false }
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.padding( 6.dp ),
                    columns = GridCells.Fixed( homeTabs.size ),
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
                                    currentTab = it
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
                                    contentDescription = label,
                                    tint = contentColor
                                )
                                else -> Icon(
                                    imageVector = it.unselectedIcon(),
                                    contentDescription = label
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

fun HomeTab.toHomeUiPage() = when( this ) {
    HomeTab.ForYou -> HomeUiPage.ForYou
    HomeTab.Songs -> HomeUiPage.Songs
    HomeTab.Tree -> HomeUiPage.Tree
    HomeTab.Playlists -> HomeUiPage.Playlists
    HomeTab.Folders -> HomeUiPage.Folders
    HomeTab.Artists -> HomeUiPage.Artists
    HomeTab.AlbumArtists -> HomeUiPage.AlbumArtists
    HomeTab.Albums -> HomeUiPage.Albums
    HomeTab.Genres -> HomeUiPage.Genres
}

enum class HomeUiPage(
    val label: (Language) -> String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector,
    val route: Route
) {
    ForYou(
        label = { it.forYou },
        selectedIcon = { Icons.Filled.Face },
        unselectedIcon = { Icons.Outlined.Face },
        route = Route.ForYou
    ),
    Songs(
        label = { it.songs },
        selectedIcon = { Icons.Filled.MusicNote },
        unselectedIcon = { Icons.Outlined.MusicNote },
        route = Route.Songs
    ),
    Artists(
        label = { it.artists },
        selectedIcon = { Icons.Filled.Group },
        unselectedIcon = { Icons.Outlined.Group },
        route = Route.Artists
    ),
    Albums(
        label = { it.albums },
        selectedIcon = { Icons.Filled.Album },
        unselectedIcon = { Icons.Outlined.Album },
        route = Route.Albums
    ),
    AlbumArtists(
        label = { it.albumArtists },
        selectedIcon = { Icons.Filled.SupervisorAccount },
        unselectedIcon = { Icons.Outlined.SupervisorAccount },
        route = Route.AlbumArtists
    ),
    Genres(
        label = { it.genres },
        selectedIcon = { Icons.Filled.Tune },
        unselectedIcon = { Icons.Outlined.Tune },
        route = Route.Genres
    ),
    Folders(
        label = { it.folders },
        selectedIcon = { Icons.Filled.Folder },
        unselectedIcon = { Icons.Outlined.Folder },
        route = Route.Folders
    ),
    Playlists(
        label = { it.playlists },
        selectedIcon = { Icons.Filled.QueueMusic },
        unselectedIcon = { Icons.Outlined.QueueMusic },
        route = Route.Playlists
    ),
    Tree(
        label = { it.tree },
        selectedIcon = { Icons.Filled.AccountTree },
        unselectedIcon = { Icons.Outlined.AccountTree },
        route = Route.Tree
    )
}