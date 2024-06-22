package com.odesa.musicMatters.core.datatesting.playlist

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.model.Playlist
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
        listOf(
            _favoritePlaylist.value,
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
        if ( isFavorite( songId ) ) currentFavoritesIds.remove( songId )
        else currentFavoritesIds.add( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
        val updatedListOfPlaylists = _playlists.value.filter { it.id != _favoritePlaylist.value.id }.toMutableList()
        updatedListOfPlaylists.add( _favoritePlaylist.value )
        _playlists.value = updatedListOfPlaylists
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
        val songIdsInMostPlayedSongsPlaylist = _mostPlayedSongsPlaylist.value.songIds.toMutableList()
        songIdsInMostPlayedSongsPlaylist.add( songId )
        val mapOfMostPlayedSongs = _mostPlayedSongsMap.value
        if ( mapOfMostPlayedSongs.containsKey( songId ) ) {
            mapOfMostPlayedSongs[ songId ] = mapOfMostPlayedSongs[ songId ]!!.plus( 1 )
        } else {
            mapOfMostPlayedSongs[ songId ] = 1
        }
        _mostPlayedSongsMap.value = mapOfMostPlayedSongs
        _mostPlayedSongsPlaylist.value = _mostPlayedSongsPlaylist.value.copy(
            songIds = songIdsInMostPlayedSongsPlaylist
        )
        println( "Most played songs map size: ${_mostPlayedSongsMap.value.size}" )
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

    override suspend fun addSongIdToPlaylist( songId: String, playlistId: String ) {
        _playlists.value.find { it.id == playlistId }?.let {
            val currentPlaylists = _playlists.value.toMutableList()
            currentPlaylists.remove( it )
            val songIds = it.songIds.toMutableList()
            songIds.add( songId )
            val modifiedPlaylist = it.copy(
                songIds = songIds
            )
            currentPlaylists.add( modifiedPlaylist )
            _playlists.value = currentPlaylists
        }
    }
}