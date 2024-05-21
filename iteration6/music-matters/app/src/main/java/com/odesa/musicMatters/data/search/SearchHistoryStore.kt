package com.odesa.musicMatters.data.search

import com.odesa.musicMatters.ui.search.SearchFilter
import org.json.JSONObject

interface SearchHistoryStore {
    fun fetchSearchHistory(): List<SearchHistoryItem>
    suspend fun saveSearchHistoryItem( searchHistoryItem: SearchHistoryItem )
    suspend fun deleteSearchHistoryItem( searchHistoryItem: SearchHistoryItem )
}

data class SearchHistoryItem(
    val id: String,
    val category: SearchFilter
) {

    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put( SEARCH_HISTORY_ITEM_ID_KEY, id )
        jsonObject.put( SEARCH_HISTORY_ITEM_CATEGORY_KEY, category.name )
        return jsonObject
    }

    companion object {
        private const val SEARCH_HISTORY_ITEM_ID_KEY = "id"
        private const val SEARCH_HISTORY_ITEM_CATEGORY_KEY = "category"

        fun fromJSONObject( serialized: JSONObject ): SearchHistoryItem? {
            serialized.apply {
                if ( has( SEARCH_HISTORY_ITEM_ID_KEY ) && has( SEARCH_HISTORY_ITEM_CATEGORY_KEY ) ) {
                    return SearchHistoryItem(
                        id = serialized.getString( SEARCH_HISTORY_ITEM_ID_KEY ),
                        category = getSearchFilterFrom( serialized.getString( SEARCH_HISTORY_ITEM_CATEGORY_KEY ) )
                    )
                }
            }
            return null
        }
    }
}

private fun getSearchFilterFrom( name: String ) = when ( name ) {
    SearchFilter.SONG.name -> SearchFilter.SONG
    SearchFilter.ALBUM.name -> SearchFilter.ALBUM
    SearchFilter.ARTIST.name -> SearchFilter.ARTIST
    SearchFilter.GENRE.name -> SearchFilter.GENRE
    else -> SearchFilter.PLAYLIST
}