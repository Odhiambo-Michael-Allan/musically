package com.odesa.musicMatters.core.data.playlists

import com.odesa.musicMatters.core.model.Playlist

interface PlaylistStore {
    fun fetchAllPlaylists(): List<Playlist>

    fun fetchFavoritesPlaylist(): Playlist
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )

    fun fetchRecentlyPlayedSongsPlaylist(): Playlist
    suspend fun addSongIdToRecentlyPlayedSongsPlaylist( songId: String )
    suspend fun removeFromRecentlyPlayedSongsPlaylist( songId: String )

    fun fetchMostPlayedSongsPlaylist(): Playlist
    suspend fun addToMostPlayedSongsPlaylist( songId: String )
    suspend fun removeFromMostPlayedSongsPlaylist( songId: String )

    fun fetchEditablePlaylists(): List<Playlist>
    suspend fun savePlaylist( playlist: Playlist )
    suspend fun deletePlaylist( playlist: Playlist )

    suspend fun addSongIdToPlaylist( songId: String, playlist: Playlist )
    fun fetchMostPlayedSongsMap(): Map<String, Int>
    suspend fun renamePlaylist( playlist: Playlist, newTitle: String )
}