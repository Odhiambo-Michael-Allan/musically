package com.odesa.musicMatters.data.playlists

interface PlaylistStore {
    fun fetchPlaylists(): Set<Playlist>
//    fun savePlaylist( playlist: Playlist )
//    fun savePlaylists( playlists: List<Playlist> )
    fun fetchFavoritesPlaylist(): Playlist
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )
    suspend fun savePlaylist( playlist: Playlist )
    suspend fun deletePlaylist( playlist: Playlist )

}