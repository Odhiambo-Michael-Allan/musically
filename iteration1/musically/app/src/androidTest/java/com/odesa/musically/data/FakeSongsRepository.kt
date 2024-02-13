package com.odesa.musically.data

import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.songs.impl.testSongs
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.audio.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSongsRepository : SongsRepository {

    private val _isLoadingSongs = MutableStateFlow( true )
    override val isLoadingSongs = _isLoadingSongs.asStateFlow()

    private val _sortSongsInReverse = MutableStateFlow( SettingsDefaults.sortSongsInReverse )
    override val sortSongsInReverse = _sortSongsInReverse.asStateFlow()

    private val _sortSongsBy = MutableStateFlow( SettingsDefaults.sortSongsBy )
    override val sortSongsBy = _sortSongsBy.asStateFlow()


    override suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        _sortSongsInReverse.value = sortSongsInReverse
    }

    override suspend fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
        _sortSongsBy.value = sortSongsBy
    }

    override suspend fun getSongs(): List<Song> {
        _isLoadingSongs.value = true
        val songs = testSongs
        _isLoadingSongs.value = false
        return songs
    }

}