package com.odesa.musicMatters.fakes

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistStore
import java.util.UUID

class FakePlaylistStore : PlaylistStore {

    private val playlists = mutableSetOf<Playlist>()

    private var favoritePlaylist = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Favorites",
        numberOfTracks = 0,
        songIds = emptySet(),
    )

    override fun fetchPlaylists(): Set<Playlist> {
        return playlists
    }

    override fun fetchFavoritesPlaylist(): Playlist {
        return favoritePlaylist
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentSongIds = favoritePlaylist.songIds.toMutableSet()
        currentSongIds.add( songId )
        favoritePlaylist = favoritePlaylist.copy(
            songIds = currentSongIds,
            numberOfTracks = currentSongIds.size
        )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentSongIds = favoritePlaylist.songIds.toMutableSet()
        currentSongIds.remove( songId )
        favoritePlaylist = favoritePlaylist.copy(
            songIds = currentSongIds,
            numberOfTracks = currentSongIds.size
        )
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        playlists.add( playlist )
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        playlists.remove( playlist )
    }

}
