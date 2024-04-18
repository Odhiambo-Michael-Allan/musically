package com.odesa.musicMatters.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language

object RoutesParameters {
    const val GenreRouteGenreName = "genre-name"
    const val ArtistRouteArtistName = "artist-name"
    const val AlbumRouteAlbumName = "album-name"
    const val AlbumArtistRouteArtistName = "album-artist-name"
    const val PlaylistRoutePlaylistId = "playlist-id"
    const val SearchRouteInitialChip = "initial-chip"
}

sealed class Route( val name: String ) {
    data object ForYou : Route( "for-you" )
    data object Songs : Route( "songs" )
    data object Artists : Route( "artists" )
    data object Albums : Route( "albums" )
    data object AlbumArtists : Route( "album-artists" )
    data object Genres : Route( "genres" )
    data object Genre : Route( "genre" )
    data object Folders : Route( "folders" )
    data object Playlists : Route( "playlists" )
    data object Tree : Route( "tree" )
    data object Queue : Route( "queue" )
    data object Settings : Route( "settings" )
}

/**
 * Contract for information needed on every MusicMatters destination
 */
interface MusicMattersDestination {
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val route: Route
    val iconContentDescription: String

    fun getLabel( language: Language ): String
}

/**
 * Music Matters app navigation destinations
 */
object ForYou : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Face
    override val unselectedIcon = Icons.Outlined.Face
    override val route = Route.ForYou
    override val iconContentDescription = "${English.forYou}-tab-icon"

    override fun getLabel( language: Language ) = language.forYou
}

object Songs : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.MusicNote
    override val unselectedIcon = Icons.Outlined.MusicNote
    override val route = Route.Songs
    override val iconContentDescription = "${English.songs}-tab-icon"

    override fun getLabel( language: Language ) = language.songs
}

object Artists : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Group
    override val unselectedIcon = Icons.Outlined.Group
    override val route = Route.Artists
    override val iconContentDescription = "${English.artists}-tab-icon"

    override fun getLabel( language: Language ) = language.artists
}

object Albums : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Album
    override val unselectedIcon = Icons.Outlined.Album
    override val route = Route.Albums
    override val iconContentDescription = "${English.albums}-tab-icon"

    override fun getLabel( language: Language ) = language.albums
}

object Genres : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Tune
    override val unselectedIcon = Icons.Outlined.Tune
    override val route = Route.Genres
    override val iconContentDescription = "${English.genres}-tab-icon"

    override fun getLabel( language: Language ) = language.genres
}

object Genre : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Tune
    override val unselectedIcon = Icons.Outlined.Tune
    override val route = Route.Genre
    override val iconContentDescription = ""

    val routeWithArgs = "${route.name}/{${RoutesParameters.GenreRouteGenreName}}"
    val arguments = listOf(
        navArgument( RoutesParameters.GenreRouteGenreName ) {
            type = NavType.StringType
        }
    )

    override fun getLabel( language: Language ) = ""
}

object Folders : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.Folder
    override val unselectedIcon = Icons.Outlined.Folder
    override val route = Route.Folders
    override val iconContentDescription = "${English.folders}-tab-icon"

    override fun getLabel( language: Language ) = language.folders
}

object Playlists : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.QueueMusic
    override val unselectedIcon = Icons.Outlined.QueueMusic
    override val route = Route.Playlists
    override val iconContentDescription = "${English.playlists}-tab-icon"

    override fun getLabel( language: Language ) = language.playlists
}

object Tree : MusicMattersDestination {
    override val selectedIcon = Icons.Filled.AccountTree
    override val unselectedIcon = Icons.Outlined.AccountTree
    override val route = Route.Tree
    override val iconContentDescription = "${English.tree}-tab-icon"

    override fun getLabel( language: Language ) = language.tree

}

val SupportedDestinations = listOf(
    ForYou,
    Songs,
    Artists,
    Albums,
    Genres,
    Folders,
    Playlists,
    Tree,
)

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

fun NavHostController.navigateToGenreScreen( genreName: String ) =
    this.navigate( "${Genre.route.name}/$genreName" )

fun NavBackStackEntry.getRouteArgument( key: String ) = arguments?.getString( key )