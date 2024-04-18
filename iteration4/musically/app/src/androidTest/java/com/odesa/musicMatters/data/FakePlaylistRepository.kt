package com.odesa.musicMatters.data

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FakePlaylistRepository : PlaylistRepository {

    private val _favoritePlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Favorites",
            numberOfTracks = 0,
            songIds = emptySet(),
        )
    )
    override val favoritesPlaylist = _favoritePlaylist.asStateFlow()

    private val _playlists = MutableStateFlow( emptySet<Playlist>() )
    override val playlists = _playlists.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return _favoritePlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableSet()
        currentFavoritesIds.add( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds,
            numberOfTracks = currentFavoritesIds.size
        )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableSet()
        currentFavoritesIds.remove( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds,
            numberOfTracks = currentFavoritesIds.size
        )
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        val mutablePlaylist = _playlists.value.toMutableSet()
        mutablePlaylist.add( playlist )
        _playlists.value = mutablePlaylist
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        _playlists.value = _playlists.value.filter { it.id != playlist.id }.toSet()
    }
}