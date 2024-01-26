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
    data object Home : Route( "home" )
    data object Settings : Route( "settings" )
}
fun NavHostController.navigate( route: Route ) = navigate( route.name )
//fun NavBackStackEntry.getRouteArgument( key: String ) =
//    arguments?.getString( key )?.let { RoutesBuilder.decodeParam( it ) }