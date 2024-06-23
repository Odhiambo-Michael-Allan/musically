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

    private val _currentPlayingQueuePlaylist = MutableStateFlow(
        Playlist(
            id = UUID.randomUUID().toString(),
            title = "",
            songIds = emptyList()
        )
    )
    override val currentPlayingQueuePlaylist = _currentPlayingQueuePlaylist.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return _favoritePlaylist.value.songIds.contains( songId )
    }

    override fun addToFavorites( songId: String ) {
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

    override fun removeFromFavorites( songId: String ) {
        val currentFavoritesIds = _favoritePlaylist.value.songIds.toMutableList()
        currentFavoritesIds.remove( songId )
        _favoritePlaylist.value = _favoritePlaylist.value.copy(
            songIds = currentFavoritesIds
        )
    }

    override fun addToRecentlyPlayedSongsPlaylist( songId: String ) {
        val currentSongIds = _recentlyPlayedSongsPlaylist.value.songIds.toMutableList()
        currentSongIds.remove( songId )
        currentSongIds.add( 0, songId )
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = currentSongIds
        )
    }

    override fun removeFromRecentlyPlayedSongsPlaylist(songId: String ) {
        val currentValue = _recentlyPlayedSongsPlaylist.value.songIds.toMutableList()
        currentValue.remove( songId )
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override fun addToMostPlayedPlaylist( songId: String ) {
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

    override fun removeFromMostPlayedPlaylist( songId: String ) {
        val currentValue = _mostPlayedSongsPlaylist.value.songIds.toMutableList()
        currentValue.remove( songId )
        val currentMap = _mostPlayedSongsMap.value
        currentMap.remove( songId )
        _mostPlayedSongsMap.value = currentMap
        _mostPlayedSongsPlaylist.value = _mostPlayedSongsPlaylist.value.copy(
            songIds = currentValue
        )
    }

    override fun savePlaylist( playlist: Playlist ) {
        val mutablePlaylist = _playlists.value.toMutableList()
        mutablePlaylist.add( playlist )
        _playlists.value = mutablePlaylist
    }

    override fun deletePlaylist( playlist: Playlist ) {
        _playlists.value = _playlists.value.filter { it.id != playlist.id }
    }

    override fun addSongIdToPlaylist( songId: String, playlistId: String ) {
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

    override fun renamePlaylist( playlist: Playlist, newTitle: String ) {
        _playlists.value.find { it.id == playlist.id }?.let {
            val currentPlaylists = _playlists.value.toMutableList()
            val renamedPlaylist = it.copy( title = newTitle )
            currentPlaylists.remove( it )
            currentPlaylists.add( renamedPlaylist )
            _playlists.value = currentPlaylists
        }
    }

    override fun saveCurrentQueue( songIds: List<String> ) {
        val currentSongsIds = _currentPlayingQueuePlaylist.value.songIds.toMutableList()
        songIds.forEach { currentSongsIds.add( it ) }
        _currentPlayingQueuePlaylist.value = _currentPlayingQueuePlaylist.value.copy(
            songIds = currentSongsIds
        )
    }

    override fun clearCurrentPlayingQueuePlaylist() {
        _currentPlayingQueuePlaylist.value = _currentPlayingQueuePlaylist.value.copy(
            songIds = emptyList()
        )
    }

    override fun cacheCurrentPlaylistData() {
        TODO("Not yet implemented")
    }
}