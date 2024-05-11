package com.odesa.musicMatters.data.playlists

import kotlinx.coroutines.flow.StateFlow


interface PlaylistRepository {

    val favoritesPlaylist: StateFlow<Playlist>
    val recentlyPlayedSongsPlaylist: StateFlow<Playlist>
    val mostPlayedSongsPlaylist: StateFlow<Playlist>
    val playlists: StateFlow<List<Playlist>>
    val mostPlayedSongsMap: StateFlow<Map<String, Int>>

    fun isFavorite( songId: String ): Boolean
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )

    suspend fun addToRecentlyPlayedSongsPlaylist( songId: String )
    suspend fun removeFromRecentlyPlayedSongsPlaylist( songId: String )

    suspend fun addToMostPlayedPlaylist( songId: String )
    suspend fun removeFromMostPlayedPlaylist( songId: String )

    suspend fun savePlaylist( playlist: Playlist )
    suspend fun deletePlaylist( playlist: Playlist )

    suspend fun addSongIdToPlaylist( songId: String, playlistId: String )

}