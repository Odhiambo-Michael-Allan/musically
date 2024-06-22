package com.odesa.musicMatters.core.data.playlists

import com.odesa.musicMatters.core.model.Playlist

interface PlaylistStore {
    fun fetchAllPlaylists(): List<Playlist>

    fun fetchFavoritesPlaylist(): Playlist
    fun addToFavorites( songId: String )
    fun removeFromFavorites( songId: String )

    fun fetchRecentlyPlayedSongsPlaylist(): Playlist
    fun addSongIdToRecentlyPlayedSongsPlaylist( songId: String )
    fun removeFromRecentlyPlayedSongsPlaylist( songId: String )

    fun fetchMostPlayedSongsPlaylist(): Playlist
    fun addToMostPlayedSongsPlaylist( songId: String )
    fun removeFromMostPlayedSongsPlaylist( songId: String )

    fun fetchEditablePlaylists(): List<Playlist>
    fun savePlaylist( playlist: Playlist )
    fun deletePlaylist( playlist: Playlist )

    fun addSongIdToPlaylist( songId: String, playlist: Playlist )
    fun fetchMostPlayedSongsMap(): Map<String, Int>
    fun renamePlaylist( playlist: Playlist, newTitle: String )

    fun addSongIdToCurrentPlayingQueue( songId: String )
    fun fetchCurrentPlayingQueue(): Playlist
    fun clearCurrentPlayingQueuePlaylist()

    fun cachePlaylistData()
}