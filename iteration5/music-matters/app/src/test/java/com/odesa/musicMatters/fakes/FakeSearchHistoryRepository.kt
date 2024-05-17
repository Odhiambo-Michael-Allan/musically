package com.odesa.musicMatters.fakes

import com.odesa.musicMatters.data.search.SearchHistoryItem
import com.odesa.musicMatters.data.search.SearchHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSearchHistoryRepository : SearchHistoryRepository {

    private val _searchHistory = MutableStateFlow( mutableListOf<SearchHistoryItem>() )
    override val searchHistory = _searchHistory.asStateFlow()

    override suspend fun saveSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        _searchHistory.value.add( searchHistoryItem )
    }

    override suspend fun deleteSearchHistoryItem( searchHistoryItem: SearchHistoryItem ) {
        _searchHistory.value.remove( searchHistoryItem )
    }
}