package com.odesa.musically.ui.helpers

import androidx.navigation.NavHostController

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
    data object Settings : Route( "settings" )
}
fun NavHostController.navigate( route: Route ) = navigate( route.name )
//fun NavBackStackEntry.getRouteArgument( key: String ) =
//    arguments?.getString( key )?.let { RoutesBuilder.decodeParam( it ) }