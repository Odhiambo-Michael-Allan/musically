package com.odesa.musically.data.storage.database

import com.odesa.musically.services.audio.Song

interface MusicDatabase {
    suspend fun getSongs(): List<Song>
}