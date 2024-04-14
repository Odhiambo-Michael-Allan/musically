package com.odesa.musicMatters.data.playlists

import kotlinx.coroutines.flow.StateFlow


interface PlaylistRepository {

    val favoritesPlaylist: StateFlow<Playlist>
    val playlists: StateFlow<Set<Playlist>>

//    fun search( playlistIds: List<String>, terms: String, limit: Int = 7 )
//    fun sort(playlistIds: List<String>, by: SortPlaylistBy, reverse: Boolean ): List<String>
//    fun createPlaylist( title: String, songIds: List<String> ): Playlist
//    suspend fun deletePlaylist( playlistId: String )
//    suspend fun updatePlaylist( playlistId: String, songIds: List<String> )
    fun isFavorite( songId: String ): Boolean
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )
    suspend fun savePlaylist( playlist: Playlist )
    suspend fun deletePlaylist( playlist: Playlist )
//    suspend fun renamePlaylist( playlist: Playlist, newTitle: String )
}