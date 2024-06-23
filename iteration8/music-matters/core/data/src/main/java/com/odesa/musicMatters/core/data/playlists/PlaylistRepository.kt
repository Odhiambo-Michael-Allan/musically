package com.odesa.musicMatters.core.data.playlists

import com.odesa.musicMatters.core.model.Playlist
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    val favoritesPlaylist: StateFlow<Playlist>
    val recentlyPlayedSongsPlaylist: StateFlow<Playlist>
    val mostPlayedSongsPlaylist: StateFlow<Playlist>
    val playlists: StateFlow<List<Playlist>>
    val mostPlayedSongsMap: StateFlow<Map<String, Int>>
    val currentPlayingQueuePlaylist: StateFlow<Playlist>

    fun isFavorite( songId: String ): Boolean
    fun addToFavorites( songId: String )
    fun removeFromFavorites( songId: String )

    fun addToRecentlyPlayedSongsPlaylist( songId: String )
    fun removeFromRecentlyPlayedSongsPlaylist( songId: String )
    fun addToMostPlayedPlaylist( songId: String )

    fun removeFromMostPlayedPlaylist( songId: String )
    fun savePlaylist( playlist: Playlist )
    fun deletePlaylist( playlist: Playlist )

    fun addSongIdToPlaylist( songId: String, playlistId: String )
    fun renamePlaylist( playlist: Playlist, newTitle: String )
    fun saveCurrentQueue( songIds: List<String> )

    fun clearCurrentPlayingQueuePlaylist()
    fun cacheCurrentPlaylistData()
}