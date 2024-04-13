package com.odesa.musicMatters.ui.navigation

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language

object RoutesParameters {
    const val ArtistRouteArtistName = "artistName"
    const val AlbumRouteAlbumName = "albumName"
    const val AlbumArtistRouteArtistName = "albumArtistName"
    const val PlaylistRoutePlaylistId = "playlistId"
    const val SearchRouteInitialChip = "initialChip"
}

sealed class Route( val name: String ) {
    data object ForYou : Route( "for-you" )
    data object Songs : Route( "songs" )
    data object Artists : Route( "artists" )
    data object Albums : Route( "albums" )
    data object AlbumArtists : Route( "album-artists" )
    data object Genres : Route( "genres" )
    data object Folders : Route( "folders" )
    data object Playlists : Route( "playlists" )
    data object Tree : Route( "tree" )
    data object Queue : Route( "queue" )
    data object Settings : Route( "settings" )
}

enum class Destination(
    val label: ( Language ) -> String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector,
    val route: Route,
    val iconContentDescription: String
) {
    ForYou(
        label = { it.forYou },
        selectedIcon = { Icons.Filled.Face },
        unselectedIcon = { Icons.Outlined.Face },
        route = Route.ForYou,
        iconContentDescription = "${English.forYou}-tab-icon"
    ),
    Songs(
        label = { it.songs },
        selectedIcon = { Icons.Filled.MusicNote },
        unselectedIcon = { Icons.Outlined.MusicNote },
        route = Route.Songs,
        iconContentDescription = "${English.songs}-tab-icon"
    ),
    Artists(
        label = { it.artists },
        selectedIcon = { Icons.Filled.Group },
        unselectedIcon = { Icons.Outlined.Group },
        route = Route.Artists,
        iconContentDescription = "${English.artists}-tab-icon"
    ),
    Albums(
        label = { it.albums },
        selectedIcon = { Icons.Filled.Album },
        unselectedIcon = { Icons.Outlined.Album },
        route = Route.Albums,
        iconContentDescription = "${English.albums}-tab-icon"
    ),
    AlbumArtists(
        label = { it.albumArtists },
        selectedIcon = { Icons.Filled.SupervisorAccount },
        unselectedIcon = { Icons.Outlined.SupervisorAccount },
        route = Route.AlbumArtists,
        iconContentDescription = "${English.albumArtists}-tab-icon"
    ),
    Genres(
        label = { it.genres },
        selectedIcon = { Icons.Filled.Tune },
        unselectedIcon = { Icons.Outlined.Tune },
        route = Route.Genres,
        iconContentDescription = "${English.genres}-tab-icon"
    ),
    Folders(
        label = { it.folders },
        selectedIcon = { Icons.Filled.Folder },
        unselectedIcon = { Icons.Outlined.Folder },
        route = Route.Folders,
        iconContentDescription = "${English.folders}-tab-icon"
    ),
    Playlists(
        label = { it.playlists },
        selectedIcon = { Icons.Filled.QueueMusic },
        unselectedIcon = { Icons.Outlined.QueueMusic },
        route = Route.Playlists,
        iconContentDescription = "${English.playlists}-tab-icon"
    ),
    Tree(
        label = { it.tree },
        selectedIcon = { Icons.Filled.AccountTree },
        unselectedIcon = { Icons.Outlined.AccountTree },
        route = Route.Tree,
        iconContentDescription = "${English.tree}-tab-icon"
    )
}
fun NavHostController.navigate( route: Route ) = navigateSingleTopTo( route.name )

fun NavHostController.navigateSingleTopTo( route: String ) =
    this.navigate( route ) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }