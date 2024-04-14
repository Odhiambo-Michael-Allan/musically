package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class SortPlaylistBy {
    CUSTOM,
    TITLE,
    TRACKS_COUNT,
}

class PlaylistRepositoryImpl( private val playlistStore: PlaylistStore ) : PlaylistRepository {

    private val _favoritesPlaylist = MutableStateFlow( playlistStore.fetchFavoritesPlaylist() )
    override val favoritesPlaylist = _favoritesPlaylist.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return favoritesPlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        playlistStore.addToFavorites( songId )
        _favoritesPlaylist.update {
            playlistStore.fetchFavoritesPlaylist()
        }
    }

    override suspend fun removeFromFavorites( songId: String ) {
        playlistStore.removeFromFavorites( songId )
        _favoritesPlaylist.update {
            playlistStore.fetchFavoritesPlaylist()
        }
    }

}