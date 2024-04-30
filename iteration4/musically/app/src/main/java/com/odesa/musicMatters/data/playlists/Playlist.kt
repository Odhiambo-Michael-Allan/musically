package com.odesa.musicMatters.data.playlists

import com.odesa.musicMatters.utils.toList
import org.json.JSONArray
import org.json.JSONObject

data class Playlist(
    val id: String,
    val title: String,
    val songIds: List<String>
) {

    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put( PLAYLIST_ID_KEY, id )
        jsonObject.put( PLAYLIST_TITLE_KEY, title )
        jsonObject.put( PLAYLIST_SONGS_KEY, JSONArray( songIds ) )
        return jsonObject
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "id"
        private const val PLAYLIST_TITLE_KEY = "title"
        private const val PLAYLIST_SONGS_KEY = "songs"
        private const val PLAYLIST_NUMBER_OF_TRACKS_KEY = "n_tracks"

        fun fromJSONObject( serialized: JSONObject ) = Playlist(
            id = serialized.getString( PLAYLIST_ID_KEY ),
            title = serialized.getString( PLAYLIST_TITLE_KEY ),
            songIds = serialized.getJSONArray( PLAYLIST_SONGS_KEY ).toList { getString( it ) },
        )

    }
}


