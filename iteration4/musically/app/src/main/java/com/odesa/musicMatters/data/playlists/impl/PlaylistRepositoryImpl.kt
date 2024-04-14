package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class SortPlaylistBy {
    CUSTOM,
    TITLE,
    TRACKS_COUNT,
}

class PlaylistRepositoryImpl( private val playlistStore: PlaylistStore ) : PlaylistRepository {

    private val _favoritesPlaylist = MutableStateFlow( playlistStore.fetchFavoritesPlaylist() )
    override val favoritesPlaylist = _favoritesPlaylist.asStateFlow()

    private val _playlists = MutableStateFlow( playlistStore.fetchPlaylists() )
    override val playlists = _playlists.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return favoritesPlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        playlistStore.addToFavorites( songId )
        _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
    }

    override suspend fun removeFromFavorites( songId: String ) {
        playlistStore.removeFromFavorites( songId )
        _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        playlistStore.savePlaylist( playlist )
        _playlists.value = playlistStore.fetchPlaylists()
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        playlistStore.deletePlaylist( playlist )
        _playlists.value = playlistStore.fetchPlaylists()
    }

}