package com.odesa.musicMatters.data.search

import kotlinx.coroutines.flow.StateFlow

interface SearchHistoryRepository {
    val searchHistory: StateFlow<List<SearchHistoryItem>>

    suspend fun saveSearchHistoryItem( searchHistoryItem: SearchHistoryItem )
    suspend fun deleteSearchHistoryItem( searchHistoryItem: SearchHistoryItem )

}