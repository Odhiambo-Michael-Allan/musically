package com.odesa.musicMatters.services.media

import android.net.Uri

data class Album(
    val name: String,
    val artists: Set<String>,
    val artworkUri: Uri?
)

enum class AlbumSortBy {
    CUSTOM,
    ALBUM_NAME,
    ARTIST_NAME,
    TRACKS_COUNT,
}

val testAlbums = List( 10 ) {
    Album(
        name = "Album $it",
        artists = setOf( "Travis Scott", "Amber Mark" ),
        artworkUri = null
    )
}