package com.odesa.musically.data.songs.impl

import com.odesa.musically.data.preferences.storage.FakePreferencesStoreImpl
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.storage.database.MusicDatabase
import com.odesa.musically.data.storage.database.impl.FakeMusicDatabase
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SongsRepositoryImplTest {

    private lateinit var preferencesStore: PreferenceStore
    private lateinit var musicDatabase: MusicDatabase
    private lateinit var songsRepository: SongsRepository

    @Before
    fun setup() {
        preferencesStore = FakePreferencesStoreImpl()
        musicDatabase = FakeMusicDatabase()
        songsRepository = SongsRepositoryImpl( preferencesStore, musicDatabase  )
    }

    @Test
    fun testSetSortSongsInReverse() = runTest {
        assertFalse( songsRepository.sortSongsInReverse.value )
        songsRepository.setSortSongsInReverse( true )
        assertTrue( preferencesStore.getSortSongsInReverse() )
        assertTrue( songsRepository.sortSongsInReverse.value )
        songsRepository.setSortSongsInReverse( false )
        assertFalse( preferencesStore.getSortSongsInReverse() )
        assertFalse( songsRepository.sortSongsInReverse.value )
    }

    @Test
    fun testSetSortSongsBy() = runTest {
        assertEquals( SettingsDefaults.sortSongsBy, songsRepository.sortSongsBy.value )
        SortSongsBy.entries.forEach {
            songsRepository.setSortSongsBy( it )
            assertEquals( it, preferencesStore.getSortSongsBy() )
            assertEquals( it, songsRepository.sortSongsBy.value )
        }
    }


    @Test
    fun testRetrieveSongs() = runTest {
        assertFalse( songsRepository.isLoadingSongs.value )
        assertEquals( 100, songsRepository.getSongs().size )
        assertFalse( songsRepository.isLoadingSongs.value )
    }
}