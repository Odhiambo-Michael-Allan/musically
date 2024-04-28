package com.odesa.musicMatters.data.playlists

interface PlaylistStore {
    fun fetchAllPlaylists(): Set<Playlist>

    fun fetchFavoritesPlaylist(): Playlist
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )

    fun fetchRecentSongsPlaylist(): Playlist
    suspend fun addSongIdToRecentSongsPlaylist( songId: String )
    suspend fun removeFromRecentSongsPlaylist( songId: String )

    fun fetchMostPlayedSongsPlaylist(): Playlist
    suspend fun addToMostPlayedSongsPlaylist( songId: String )
    suspend fun removeFromMostPlayedSongsPlaylist( songId: String )

    fun fetchAllCustomPlaylists(): Set<Playlist>
    suspend fun saveCustomPlaylist( playlist: Playlist )
    suspend fun deleteCustomPlaylist( playlist: Playlist )
    suspend fun addSongToCustomPlaylist( songId: String, playlist: Playlist )

    fun fetchMostPlayedSongsMap(): Map<String, Int>

}