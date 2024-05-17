package com.odesa.musicMatters.data.search.impl

import com.odesa.musicMatters.data.playlists.testPlaylists
import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.data.search.SearchHistoryStore
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testArtists
import com.odesa.musicMatters.ui.components.testGenres
import com.odesa.musicMatters.ui.search.SearchFilter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.util.UUID

@RunWith( RobolectricTestRunner::class )
class SearchHistoryStoreImplTest {

    private val searchHistoryFile = File( "search-history-test-file.json" )
    private lateinit var searchHistoryStore: SearchHistoryStore

    @Before
    fun setup() {
        searchHistoryFile.createNewFile()
        searchHistoryStore = SearchHistoryStoreImpl(
            searchHistoryFile
        )
    }

    @After
    fun cleanup() {
        searchHistoryFile.delete()
    }

    @Test
    fun testSearchHistoryItemsAreCorrectlySaved() = runTest {
        testSearchHistoryItems.forEach {
            searchHistoryStore.saveSearchHistoryItem( it )
        }
        assertEquals( testSearchHistoryItems.size, searchHistoryStore.fetchSearchHistory().size )
    }

    @Test
    fun testSearchHistoryItemsAreSortedCorrectly() = runTest {
        testSearchHistoryItems.forEach {
            searchHistoryStore.saveSearchHistoryItem( it )
        }
        assertEquals( testSearchHistoryItems.last().id,
            searchHistoryStore.fetchSearchHistory().first().id )
    }

    @Test
    fun testSearchHistoryItemsAreDeletedCorrectly() = runTest {
        testSearchHistoryItems.forEach {
            searchHistoryStore.saveSearchHistoryItem( it )
        }
        searchHistoryStore.deleteSearchHistoryItem( testSearchHistoryItems.first() )
        searchHistoryStore.deleteSearchHistoryItem( testSearchHistoryItems.last() )
        assertEquals( testSearchHistoryItems.size - 2,
            searchHistoryStore.fetchSearchHistory().size
        )
    }
}

val testSearchHistoryItems = listOf(
    SearchHistoryItem(
        id = UUID.randomUUID().toString(),
        category = SearchFilter.SONG
    ),
    SearchHistoryItem(
        id = testAlbums.first().name,
        category = SearchFilter.ALBUM
    ),
    SearchHistoryItem(
        id = testArtists.first().name,
        category = SearchFilter.ARTIST
    ),
    SearchHistoryItem(
        id = testGenres.first().name,
        category = SearchFilter.GENRE
    ),
    SearchHistoryItem(
        id = testPlaylists.first().id,
        category = SearchFilter.PLAYLIST
    )
)