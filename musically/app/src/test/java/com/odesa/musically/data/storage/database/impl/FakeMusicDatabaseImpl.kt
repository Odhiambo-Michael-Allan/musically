package com.odesa.musically.data.storage.database.impl

import com.odesa.musically.data.songs.impl.testSongs
import com.odesa.musically.data.storage.database.MusicDatabase
import com.odesa.musically.services.audio.Song

class FakeMusicDatabase() : MusicDatabase {
    override val songs: List<Song>
        get() = testSongs

}