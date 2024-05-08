package com.odesa.musicMatters.data

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FakePlaylistRepository : PlaylistRepository {

    private val _favoritePlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Favorites",
            songIds = emptyList(),
        )
    )
    override val favoritesPlaylist = _favoritePlaylist.asStateFlow()
    override val recentlyPlayedSongsPlaylist: StateFlow<Playlist>
        get() = TODO("Not yet implemented")
    override val mostPlayedSongsPlaylist: StateFlow<Playlist>
        get() = TODO("Not yet implemented")

    private val _playlists = MutableStateFlow( emptyList<Playlist>() )
    override val playlists = _playlists.asStateFlow()
    override val mostPlayedSongsMap: StateFlow<Map<String, Int>>
        get() = TODO("Not yet implemented")

    override fun isFavorite( songId: String ): Boolean {
        return _favoritePlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableList()
        currentFavoritesIds.add( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableList()
        currentFavoritesIds.remove( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
    }

    override suspend fun addToRecentlyPlayedSongsPlaylist(songId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromRecentlyPlayedSongsPlaylist(songId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addToMostPlayedPlaylist(songId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromMostPlayedPlaylist(songId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        val mutablePlaylist = _playlists.value.toMutableList()
        mutablePlaylist.add( playlist )
        _playlists.value = mutablePlaylist
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        _playlists.value = _playlists.value.filter { it.id != playlist.id }
    }
}