package com.odesa.musicMatters.services.media

import android.net.Uri

data class Artist(
    val name: String,
    val artworkUri: Uri?,
)

val testArtists = List( 10 ) {
    Artist(
        name = "Artist $it",
        artworkUri = Uri.EMPTY
    )
}
