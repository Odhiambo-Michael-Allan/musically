package com.odesa.musicMatters.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.core.data.search.SearchHistoryRepository
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.SearchFilter

import com.odesa.musicMatters.ui.album.AlbumScreen
import com.odesa.musicMatters.ui.album.AlbumScreenViewModel
import com.odesa.musicMatters.ui.album.AlbumScreenViewModelFactory
import com.odesa.musicMatters.ui.albums.AlbumsScreen
import com.odesa.musicMatters.ui.albums.AlbumsScreenViewModel
import com.odesa.musicMatters.ui.albums.AlbumsViewModelFactory
import com.odesa.musicMatters.ui.artist.ArtistScreen
import com.odesa.musicMatters.ui.artist.ArtistScreenViewModel
import com.odesa.musicMatters.ui.artist.ArtistScreenViewModelFactory
import com.odesa.musicMatters.ui.artists.ArtistsScreen
import com.odesa.musicMatters.ui.artists.ArtistsScreenViewModel
import com.odesa.musicMatters.ui.artists.ArtistsViewModelFactory
import com.odesa.musicMatters.ui.components.BottomSheetMenuItem
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

import com.odesa.musicMatters.ui.search.SearchScreen
import com.odesa.musicMatters.ui.search.SearchScreenViewModel
import com.odesa.musicMatters.ui.search.SearchScreenViewModelFactory
import com.odesa.musicMatters.ui.settings.SettingsScreen
import com.odesa.musicMatters.ui.settings.SettingsViewModel
import com.odesa.musicMatters.ui.settings.SettingsViewModelFactory
import com.odesa.musicMatters.ui.songs.SongsScreen
import com.odesa.musicMatters.ui.songs.SongsScreenViewModel
import com.odesa.musicMatters.ui.songs.SongsViewModelFactory
import com.odesa.musicMatters.ui.tree.TreeScreen
import com.odesa.musicMatters.ui.tree.TreeScreenViewModel
import com.odesa.musicMatters.ui.tree.TreeViewModelFactory
import com.odesa.musicMatters.ui.utils.getSearchFilterFrom
import com.odesa.musicMatters.ui.utils.shareSong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicMattersNavHost(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
    musicServiceConnection: MusicServiceConnection,
    language: Language,
    labelVisibility: HomePageBottomBarLabelVisibility,
    nowPlayingBottomBar: @Composable () -> Unit,
) {

    var currentTabName by rememberSaveable { mutableStateOf( ForYou.route.name ) }
    var currentlySelectedMoreTab by rememberSaveable { mutableStateOf( "" ) }
    var showMoreDestinationsBottomSheet by remember { mutableStateOf( false ) }
    val context = LocalContext.current

    navController.addOnDestinationChangedListener { _, destination, _ ->
        TOP_LEVEL_DESTINATIONS.forEach {
            if ( destination.route == it.route.name )
                currentTabName = it.route.name
        }
        currentlySelectedMoreTab = ""
        MORE_DESTINATIONS.forEach {
            if ( destination.route == it.route.name )
                currentlySelectedMoreTab = it.route.name
        }
    }


    Column {
        NavHost(
            modifier = Modifier
                .weight(1f),
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
                    onNavigateToSearch = { navController.navigateToSearchScreen( "--" ) },
                    onSuggestedAlbumClick = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
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
                    viewModel = songsScreenViewModel,
                    onSettingsClicked = { navController.navigate( Route.Settings.name ) },
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
                    onNavigateToSearch = { navController.navigateToSearchScreen( SearchFilter.SONG.name ) }
                )
            }
            composable(
                route = Route.Artists.name,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) {
                val artistsScreenViewModel: ArtistsScreenViewModel = viewModel(
                    factory = ArtistsViewModelFactory(
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository
                    )
                )
                ArtistsScreen(
                    viewModel = artistsScreenViewModel,
                    onArtistClick = { navController.navigateToArtistScreen( it ) },
                    onNavigateToSearch = { navController.navigateToSearchScreen( SearchFilter.ARTIST.name ) },
                    onSettingsClicked = { navController.navigate( Route.Settings.name ) }
                )
            }
            composable(
                route = Artist.route.name,
                arguments = Artist.arguments,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) { navBackStackEntry ->
                // Retrieve the passed argument
                val artistName = navBackStackEntry.getRouteArgument(
                    RouteParameters.ARTIST_ROUTE_ARTIST_NAME
                ) ?: ""
                val artistScreenViewModel: ArtistScreenViewModel = viewModel(
                    factory = ArtistScreenViewModelFactory(
                        artistName = artistName,
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository
                    )
                )
                ArtistScreen(
                    artistName = artistName,
                    viewModel = artistScreenViewModel,
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onNavigateBack = { navController.navigateUp() },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
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
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository,
                    )
                )
                AlbumsScreen(
                    viewModel = albumsScreenViewModel,
                    onAlbumClick = { navController.navigateToAlbumScreen( it ) },
                    onNavigateToSearch = { navController.navigateToSearchScreen( SearchFilter.ALBUM.name ) },
                    onSettingsClicked = { navController.navigate( Route.Settings.name ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) }
                )
            }
            composable(
                route = Album.routeWithArgs,
                arguments = Album.arguments,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) { navBackStackEntry ->
                // Retrieve the passed argument
                val albumName = navBackStackEntry.getRouteArgument(
                    RouteParameters.ALBUM_ROUTE_ALBUM_NAME ) ?: ""
                val albumScreenViewModel: AlbumScreenViewModel = viewModel(
                    factory = AlbumScreenViewModelFactory(
                        albumName = albumName,
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository
                    )
                )

                AlbumScreen(
                    albumName = albumName,
                    viewModel = albumScreenViewModel,
                    onNavigateBack = { navController.navigateUp() },
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
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
                    onNavigateToSearch = { navController.navigateToSearchScreen( SearchFilter.GENRE.name ) },
                    onSettingsClicked = { navController.navigate( Route.Settings.name ) }
                )
            }
            composable(
                route = Genre.routeWithArgs,
                arguments = Genre.arguments,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) { navBackStackEntry ->
                // Retrieve the passed argument
                val genreName = navBackStackEntry.getRouteArgument(
                    RouteParameters.GENRE_ROUTE_GENRE_NAME ) ?: ""

                val genreScreenViewModel: GenreScreenViewModel = viewModel(
                    factory = GenreScreenViewModelFactory(
                        genreName = genreName,
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository,
                    )
                )
                GenreScreen(
                    genreName = genreName,
                    viewModel = genreScreenViewModel,
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onNavigateBack = { navController.navigateUp() },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
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
                    onNavigateToSearch = { navController.navigateToSearchScreen( SearchFilter.PLAYLIST.name ) },
                    onSettingsClicked = { navController.navigate( Route.Settings.name ) }
                )
            }
            composable(
                route = Playlist.routeWithArgs,
                arguments = Playlist.arguments,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) { navBackStackEntry ->
                val playlistId = navBackStackEntry.getRouteArgument(
                    RouteParameters.PLAYLIST_ROUTE_PLAYLIST_ID
                ) ?: ""
                val playlistName = navBackStackEntry.getRouteArgument(
                    RouteParameters.PLAYLIST_ROUTE_PLAYLIST_NAME
                ) ?: ""
                val playlistScreenViewModel: PlaylistScreenViewModel = viewModel(
                    factory = PlaylistScreenViewModelFactory(
                        playlistId = playlistId,
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistsRepository = playlistRepository
                    )
                )

                PlaylistScreen(
                    playlistTitle = playlistName,
                    viewModel = playlistScreenViewModel,
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onNavigateBack = { navController.navigateUp() },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
                )
            }
            composable(
                route = Route.Tree.name,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) {
                val treeScreenViewModel: TreeScreenViewModel = viewModel(
                    factory = TreeViewModelFactory(
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository
                    )
                )
                TreeScreen(
                    viewModel = treeScreenViewModel,
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
                    onNavigateToSearch = { navController.navigateToSearchScreen( "--" ) },
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
                    viewModel = queueScreenViewModel,
                    onViewArtist = { navController.navigateToArtistScreen( it ) },
                    onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                    onBackArrowClick = { navController.navigateUp() },
                    onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
                )
            }
            composable(
                route = Search.routeWithArgs,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
                exitTransition = { FadeTransition.exitTransition() }
            ) { navBackStackEntry ->
                val searchFilterName = navBackStackEntry.getRouteArgument(
                    RouteParameters.SEARCH_ROUTE_SEARCH_FILTER
                ) ?: ""

                val searchScreenViewModel: SearchScreenViewModel = viewModel(
                    factory = SearchScreenViewModelFactory(
                        musicServiceConnection = musicServiceConnection,
                        settingsRepository = settingsRepository,
                        playlistRepository = playlistRepository,
                        searchHistoryRepository = searchHistoryRepository
                    )
                )
                SearchScreen(
                    viewModel = searchScreenViewModel,
                    initialSearchFilter = getSearchFilterFrom( searchFilterName ),
                    onAlbumClick = { navController.navigateToAlbumScreen( it.title ) },
                    onArtistClick = { navController.navigateToArtistScreen( it.name ) },
                    onGenreClick = { navController.navigateToGenreScreen( it.name ) },
                    onPlaylistClick = { navController.navigateToPlaylistScreen( it.id, it.title ) }
                ) {
                    navController.navigateUp()
                }
            }
            composable(
                Route.Settings.name,
                enterTransition = { SlideTransition.slideUp.enterTransition() },
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
            ) {
                Spacer( modifier = Modifier.width( 2.dp ) )
                TOP_LEVEL_DESTINATIONS.forEach { tab ->
                    val isSelected = currentTabName == tab.route.name
                    val label = tab.getLabel( language )
                    NavigationBarItem(
                        selected = isSelected,
                        alwaysShowLabel = labelVisibility == HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
                        onClick = {
                            if ( tab == Library ) showMoreDestinationsBottomSheet = true
                            else {
                                currentTabName = tab.route.name
                                navController.navigate( tab.route )
                            }
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

            if ( showMoreDestinationsBottomSheet ) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true ),
                    onDismissRequest = { showMoreDestinationsBottomSheet = false }
                ) {
                    MORE_DESTINATIONS.forEach {
                        BottomSheetMenuItem(
                            leadingIcon = it.selectedIcon,
                            leadingIconContentDescription = it.iconContentDescription,
                            label = it.getLabel( language ),
                            isSelected = currentlySelectedMoreTab == it.route.name
                        ) {
                            currentlySelectedMoreTab = it.route.name
                            showMoreDestinationsBottomSheet = false
                            currentTabName = Library.route.name
                            navController.navigate( it.route )
                        }
                    }
                }
            }
        }
    }

}




