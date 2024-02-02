package com.odesa.musically.data.songs.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.storage.database.MusicDatabase
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.services.audio.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

class SongsRepositoryImpl(
    private val preferenceStore: PreferenceStore,
    private val musicDatabase: MusicDatabase,
) : SongsRepository {

    private val _isLoadingSongs = MutableStateFlow( true )
    override val isLoadingSongs = _isLoadingSongs.asStateFlow()

    private val _sortSongsInReverse = MutableStateFlow( preferenceStore.getSortSongsInReverse() )
    override val sortSongsInReverse = _sortSongsInReverse.asStateFlow()

    private val _sortSongsBy = MutableStateFlow( preferenceStore.getSortSongsBy() )
    override val sortSongsBy = _sortSongsBy.asStateFlow()

    override suspend fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        preferenceStore.setSortSongsInReverse( sortSongsInReverse )
        _sortSongsInReverse.update {
            sortSongsInReverse
        }
    }

    override suspend fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
        preferenceStore.setSortSongsBy( sortSongsBy )
        _sortSongsBy.update {
            sortSongsBy
        }
    }

    override suspend fun getSongs(): List<Song> {
        _isLoadingSongs.update { true }
        val songs = musicDatabase.getSongs()
        _isLoadingSongs.update { false }
        return songs
    }
}

enum class SortSongsBy {
    CUSTOM,
    TITLE,
    ARTIST,
    ALBUM,
    DURATION,
    DATE_ADDED,
    DATE_MODIFIED,
    COMPOSER,
    ALBUM_ARTIST,
    YEAR,
    FILENAME,
    TRACK_NUMBER,
}

@RequiresApi( Build.VERSION_CODES.O )
val testSongs = List( 100 ) {
    Song(
        id = Math.random().toLong(),
        title = "You Right",
        trackNumber = 3,
        year = LocalDateTime.now().year,
        duration = 180000L,
        album = "Best of Levels",
        artists = setOf( "Doja Cat", "The Weekend" ),
        composers = setOf( "Amala Zandile Dlamini", "Lukasz Gottwald",
            "Abel \"The Weekend\" Tesfaye" ),
        dateAdded = 1000000,
        dateModified = 1000000,
        size = 1000,
        path = "/storage/emulated/0/Music/Telegram/DojaCat - You Right.mp3",
    )
}