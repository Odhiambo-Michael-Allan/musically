package com.odesa.musically.data.storage.database.impl

import com.odesa.musically.data.songs.impl.testSongs
import com.odesa.musically.data.storage.database.MusicDatabase

class FakeMusicDatabase() : MusicDatabase {
    override suspend fun getSongs() = testSongs

}