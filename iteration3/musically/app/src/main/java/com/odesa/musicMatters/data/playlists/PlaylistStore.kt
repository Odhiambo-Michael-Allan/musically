package com.odesa.musicMatters.data.playlists

interface PlaylistStore {
//    fun fetchPlaylists(): List<Playlist>
//    fun savePlaylist( playlist: Playlist )
//    fun savePlaylists( playlists: List<Playlist> )
    fun fetchFavoritesPlaylist(): Playlist
    suspend fun addToFavorites( songId: String )
    suspend fun removeFromFavorites( songId: String )

}