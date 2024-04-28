package com.odesa.musicMatters.services.media

import android.net.Uri

data class Album(
    val name: String,
    val artworkUri: Uri?
)

val testAlbums = List( 10 ) {
    Album(
        name = "Album $it",
        artworkUri = null
    )
}