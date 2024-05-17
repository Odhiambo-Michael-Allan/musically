package com.odesa.musicMatters.fakes

import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.data.search.SearchHistoryStore

class FakeSearchHistoryStore : SearchHistoryStore {

    private val searchHistory = mutableListOf<SearchHistoryItem>()

    override fun fetchSearchHistory() = searchHistory

    override suspend fun saveSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        searchHistory.add( 0, searchHistoryItem )
    }

    override suspend fun deleteSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        searchHistory.remove( searchHistoryItem )
    }
}