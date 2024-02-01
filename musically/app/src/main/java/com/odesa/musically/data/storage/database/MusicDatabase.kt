package com.odesa.musically.data.storage.database

import com.odesa.musically.services.audio.Song

interface MusicDatabase {
    val songs: List<Song>
}