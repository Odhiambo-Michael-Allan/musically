package com.odesa.musically.data.songs

import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.services.audio.Song
import kotlinx.coroutines.flow.StateFlow

interface SongsRepository {
    val isLoadingSongs: StateFlow<Boolean>
    val sortSongsInReverse: StateFlow<Boolean>
    val sortSongsBy: StateFlow<SortSongsBy>

    suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean )
    suspend fun setSortSongsBy( sortSongsBy: SortSongsBy )
    suspend fun getSongs(): List<Song>
}