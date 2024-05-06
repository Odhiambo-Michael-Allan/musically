package com.odesa.musicMatters.fakes

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
            songIds = emptyList(),
        )
    )
    override val favoritesPlaylist = _favoritePlaylist.asStateFlow()

    private val _recentlyPlayedSongsPlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Recently Played Songs",
            songIds = emptyList()
        )
    )
    override val recentlyPlayedSongsPlaylist = _recentlyPlayedSongsPlaylist.asStateFlow()

    private val _mostPlayedSongsPlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Most Played Songs",
            songIds = emptyList()
        )
    )
    override val mostPlayedSongsPlaylist = _mostPlayedSongsPlaylist.asStateFlow()

    private val _playlists = MutableStateFlow(
        listOf( _favoritePlaylist.value,
            _recentlyPlayedSongsPlaylist.value,
            _mostPlayedSongsPlaylist.value
        )
    )
    override val playlists = _playlists.asStateFlow()

    private val _mostPlayedSongsMap = MutableStateFlow( mutableMapOf<String, Int>() )
    override val mostPlayedSongsMap = _mostPlayedSongsMap.asStateFlow()

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

    override suspend fun addToRecentlyPlayedSongsPlaylist( songId: String ) {
        val currentSongIds = _recentlyPlayedSongsPlaylist.value.songIds.toMutableList()
        currentSongIds.remove( songId )
        currentSongIds.add( 0, songId )
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = currentSongIds
        )
    }

    override suspend fun removeFromRecentlyPlayedSongsPlaylist(songId: String ) {
        val currentValue = _recentlyPlayedSongsPlaylist.value.songIds.toMutableList()
        currentValue.remove( songId )
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override suspend fun addToMostPlayedPlaylist( songId: String ) {
        val currentValue = _mostPlayedSongsPlaylist.value.songIds.toMutableList()
        currentValue.add( songId )
        val currentMap = _mostPlayedSongsMap.value
        if ( currentMap.containsKey( songId ) ) currentMap[ songId ] =
            currentMap[ songId ]!!.plus( 1 ) else currentMap[ songId] = 1
        _mostPlayedSongsMap.value = currentMap
        _mostPlayedSongsPlaylist.value = _mostPlayedSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override suspend fun removeFromMostPlayedPlaylist( songId: String ) {
        val currentValue = _mostPlayedSongsPlaylist.value.songIds.toMutableList()
        currentValue.remove( songId )
        val currentMap = _mostPlayedSongsMap.value
        currentMap.remove( songId )
        _mostPlayedSongsMap.value = currentMap
        _mostPlayedSongsPlaylist.value = _mostPlayedSongsPlaylist.value.copy(
            songIds = currentValue
        )
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