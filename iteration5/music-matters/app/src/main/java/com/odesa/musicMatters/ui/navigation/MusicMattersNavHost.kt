package com.odesa.musicMatters.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.HomePageBottomBarLabelVisibility
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.search.SearchHistoryRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
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
import com.odesa.musicMatters.ui.components.swipeable
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

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun MusicMattersNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
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

    val context = LocalContext.current

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
                onNavigateToSearch = { navController.navigate( Route.Search.name ) },
                onSuggestedAlbumClick = { navController.navigateToAlbumScreen( it.name ) },
                onSuggestedArtistClick = { navController.navigateToArtistScreen( it.name ) },
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
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
                onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
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
                    settingsRepository = settingsRepository
                )
            )
            ArtistsScreen(
                viewModel = artistsScreenViewModel,
                onArtistClick = { navController.navigateToArtistScreen( it ) },
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Artist.route.name,
            arguments = Artist.arguments,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
        ) { navBackStackEntry ->
            val artistScreenViewModel: ArtistScreenViewModel = viewModel(
                factory = ArtistScreenViewModelFactory(
                    musicServiceConnection = musicServiceConnection,
                    settingsRepository = settingsRepository,
                    playlistRepository = playlistRepository
                )
            )
            // Retrieve the passed argument
            val artist = navBackStackEntry.getRouteArgument(
                RouteParameters.ARTIST_ROUTE_ARTIST_NAME
            ) ?: ""
            artistScreenViewModel.loadSongsBy( artist )
            ArtistScreen(
                artistName = artist,
                viewModel = artistScreenViewModel,
                onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                onViewArtist = { navController.navigateToArtistScreen( it ) },
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
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
            enterTransition = { SlideTransition.slideUp.enterTransition() },
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
                viewModel = albumScreenViewModel,
                onNavigateBack = { navController.navigateUp() },
                onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                onViewArtist = { navController.navigateToArtistScreen( it ) },
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
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
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Genre.routeWithArgs,
            arguments = Genre.arguments,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
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
                viewModel = genreScreenViewModel,
                onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                onViewArtist = { navController.navigateToArtistScreen( it ) },
                onNavigateBack = { navController.navigateUp() },
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
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
                onSettingsClicked = { navController.navigate( Route.Settings.name ) }
            )
        }
        composable(
            route = Playlist.routeWithArgs,
            arguments = Playlist.arguments,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
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
                onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                onViewArtist = { navController.navigateToArtistScreen( it ) },
                onNavigateBack = { navController.navigateUp() },
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
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
                onAddToQueue = { addToQueue( context, musicServiceConnection, it ) },
                onPlayNext = { playNext( context, musicServiceConnection, it ) },
                onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
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
                onPlayNext = {
                    playNext( context, musicServiceConnection, it )
                },
                onViewArtist = { navController.navigateToArtistScreen( it ) },
                onViewAlbum = { navController.navigateToAlbumScreen( it ) },
                onAddToQueue = {
                    addToQueue( context, musicServiceConnection, it )
                },
                onBackArrowClick = { navController.navigateUp() },
                onShareSong = { uri, errorMessage -> shareSong( context, uri, errorMessage ) },
            )
        }
        composable(
            Route.Search.name,
            enterTransition = { SlideTransition.slideUp.enterTransition() },
            exitTransition = { FadeTransition.exitTransition() }
        ) {
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
                onAlbumClick = { navController.navigateToAlbumScreen( it.name ) },
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

private fun displayToastWithMessage( context: Context, message: String ) = Toast.makeText(
    context,
    message,
    Toast.LENGTH_SHORT
).show()

private fun playNext(
    context: Context,
    musicServiceConnection: MusicServiceConnection,
    mediaItem: MediaItem
) {
    if ( musicServiceConnection.nowPlaying.value.mediaId == mediaItem.mediaId ) {
        displayToastWithMessage( context, "Song is already playing" )
    } else {
        musicServiceConnection.playNext( mediaItem )
        displayToastWithMessage(
            context, "${mediaItem.mediaMetadata.title} will play next"
        )
    }
}

private fun addToQueue(
    context: Context,
    musicServiceConnection: MusicServiceConnection,
    mediaItem: MediaItem
) {
    if ( musicServiceConnection.mediaItemIsPresentInQueue( mediaItem ) ) {
        displayToastWithMessage( context, "Song is already in queue" )
    } else {
        musicServiceConnection.addToQueue( mediaItem )
        displayToastWithMessage(
            context,
            "Added ${mediaItem.mediaMetadata.title} to queue"
        )
    }
}

private fun shareSong( context: Context, uri: Uri, localizedErrorMessage: String ) {
    try {
        val intent = createShareSongIntent( context, uri )
        context.startActivity( intent )
    } catch ( exception: Exception ) {
        displayToastWithMessage(
            context,
            localizedErrorMessage
        )
    }
}

private fun createShareSongIntent( context: Context, uri: Uri ) = Intent( Intent.ACTION_SEND ).apply {
    addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION )
    putExtra( Intent.EXTRA_STREAM, uri )
    type = context.contentResolver.getType( uri )
}

private fun HomeTab.toDestination() = when( this ) {
    HomeTab.ForYou -> ForYou
    HomeTab.Songs -> Songs
    HomeTab.Tree -> Tree
    HomeTab.Playlists -> Playlists
    HomeTab.Artists -> Artists
    HomeTab.Albums -> Albums
    HomeTab.Genres -> Genres
}


