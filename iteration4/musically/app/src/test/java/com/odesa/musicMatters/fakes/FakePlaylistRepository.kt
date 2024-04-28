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
            songIds = emptySet(),
        )
    )
    override val favoritesPlaylist = _favoritePlaylist.asStateFlow()

    private val _recentSongsPlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Recently Played Songs",
            songIds = emptySet()
        )
    )
    override val recentSongsPlaylist = _recentSongsPlaylist.asStateFlow()

    private val _mostPlayedSongsPlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "Most Played Songs",
            songIds = emptySet()
        )
    )
    override val mostPlayedSongsPlaylist = _mostPlayedSongsPlaylist.asStateFlow()

    private val _playlists = MutableStateFlow( emptySet<Playlist>() )
    override val playlists = _playlists.asStateFlow()

    private val _mostPlayedSongsMap = MutableStateFlow( mutableMapOf<String, Int>() )
    override val mostPlayedSongsMap = _mostPlayedSongsMap.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return _favoritePlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableSet()
        currentFavoritesIds.add( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableSet()
        currentFavoritesIds.remove( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
    }

    override suspend fun addToRecentSongsPlaylist( songId: String ) {
        val currentValue = _recentSongsPlaylist.value.songIds.toMutableSet()
        currentValue.add( songId )
        _recentSongsPlaylist.value = _recentSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override suspend fun removeFromRecentSongsPlaylist( songId: String ) {
        val currentValue = _recentSongsPlaylist.value.songIds.toMutableSet()
        currentValue.remove( songId )
        _recentSongsPlaylist.value = _recentSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override suspend fun addToMostPlayedPlaylist( songId: String ) {
        val currentValue = _mostPlayedSongsPlaylist.value.songIds.toMutableSet()
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
        val currentValue = _mostPlayedSongsPlaylist.value.songIds.toMutableSet()
        currentValue.remove( songId )
        val currentMap = _mostPlayedSongsMap.value
        currentMap.remove( songId )
        _mostPlayedSongsMap.value = currentMap
        _mostPlayedSongsPlaylist.value = _mostPlayedSongsPlaylist.value.copy(
            songIds = currentValue
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