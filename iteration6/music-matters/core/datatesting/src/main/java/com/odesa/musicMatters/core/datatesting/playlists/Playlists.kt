package com.odesa.musicMatters.core.datatesting.playlists

import com.odesa.musicMatters.core.model.Playlist
import java.util.UUID

val testPlaylists = List( 20 ) {
    Playlist(
        id = UUID.randomUUID().toString() + "$it",
        title = "Playlist-$it",
        songIds = emptyList()
    )
}.toMutableList()