package com.odesa.musicMatters.core.data.playlists.impl

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.model.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber


class PlaylistRepositoryImpl( private val playlistStore: PlaylistStore ) : PlaylistRepository {

    private val _favoritesPlaylist = MutableStateFlow( playlistStore.fetchFavoritesPlaylist() )
    override val favoritesPlaylist = _favoritesPlaylist.asStateFlow()

    private val _recentlyPlayedSongsPlaylist = MutableStateFlow(
        playlistStore.fetchRecentlyPlayedSongsPlaylist()
    )
    override val recentlyPlayedSongsPlaylist = _recentlyPlayedSongsPlaylist.asStateFlow()

    private val _mostPlayedSongsPlaylist = MutableStateFlow(
        playlistStore.fetchMostPlayedSongsPlaylist()
    )
    override val mostPlayedSongsPlaylist = _mostPlayedSongsPlaylist.asStateFlow()

    private val _playlists = MutableStateFlow( playlistStore.fetchAllPlaylists() )
    override val playlists = _playlists.asStateFlow()

    private val _mostPlayedSongsMap = MutableStateFlow( playlistStore.fetchMostPlayedSongsMap() )
    override val mostPlayedSongsMap = _mostPlayedSongsMap.asStateFlow()

    private val _currentPlayingQueuePlaylist = MutableStateFlow(
        playlistStore.fetchCurrentPlayingQueue()
    )
    override val currentPlayingQueuePlaylist = _currentPlayingQueuePlaylist.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return favoritesPlaylist.value.songIds.contains( songId )
    }

    override fun addToFavorites( songId: String ) {
        if ( isFavorite( songId ) ) playlistStore.removeFromFavorites( songId )
        else playlistStore.addToFavorites( songId )
        _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun removeFromFavorites( songId: String ) {
        playlistStore.removeFromFavorites( songId )
        _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun addToRecentlyPlayedSongsPlaylist( songId: String ) {
        Timber.tag( PLAYLIST_REPOSITORY_TAG ).d( "ADDING SONG TO RECENTLY PLAYED PLAYLIST. ID: $songId" )
        playlistStore.addSongIdToRecentlyPlayedSongsPlaylist( songId )
        val newSongIds = playlistStore.fetchRecentlyPlayedSongsPlaylist().songIds
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = newSongIds
        )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun removeFromRecentlyPlayedSongsPlaylist( songId: String ) {
        playlistStore.removeFromRecentlyPlayedSongsPlaylist( songId )
        val newSongIds = playlistStore.fetchRecentlyPlayedSongsPlaylist().songIds
        _recentlyPlayedSongsPlaylist.value = _recentlyPlayedSongsPlaylist.value.copy(
            songIds = newSongIds
        )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun addToMostPlayedPlaylist( songId: String ) {
        Timber.tag( PLAYLIST_REPOSITORY_TAG ).d( "ADDING SONG TO MOST PLAYED PLAYLIST. ID: $songId" )
        playlistStore.addToMostPlayedSongsPlaylist( songId )
        _playlists.value = playlistStore.fetchAllPlaylists()
        _mostPlayedSongsPlaylist.value = playlistStore.fetchMostPlayedSongsPlaylist()
        _mostPlayedSongsMap.value = playlistStore.fetchMostPlayedSongsMap()
    }

    override fun removeFromMostPlayedPlaylist( songId: String ) {
        playlistStore.removeFromMostPlayedSongsPlaylist( songId )
        _playlists.value = playlistStore.fetchAllPlaylists()
        _mostPlayedSongsMap.value = playlistStore.fetchMostPlayedSongsMap()
        _mostPlayedSongsPlaylist.value = playlistStore.fetchMostPlayedSongsPlaylist()
    }

    override fun savePlaylist( playlist: Playlist ) {
        playlistStore.savePlaylist( playlist )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun deletePlaylist( playlist: Playlist ) {
        playlistStore.deletePlaylist( playlist )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun addSongIdToPlaylist( songId: String, playlistId: String ) {
        _playlists.value.find { it.id == playlistId }?.let {
            playlistStore.addSongIdToPlaylist( songId, it )
            _playlists.value = playlistStore.fetchAllPlaylists()
            if ( playlistId == _favoritesPlaylist.value.id )
                _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
        }
    }

    override fun renamePlaylist( playlist: Playlist, newTitle: String ) {
        playlistStore.renamePlaylist( playlist, newTitle )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override fun saveCurrentQueue( songIds: List<String> ) {
        songIds.forEach { playlistStore.addSongIdToCurrentPlayingQueue( it ) }
        _currentPlayingQueuePlaylist.value = playlistStore.fetchCurrentPlayingQueue()
    }

    override fun clearCurrentPlayingQueuePlaylist() {
        playlistStore.clearCurrentPlayingQueuePlaylist()
        _currentPlayingQueuePlaylist.value = playlistStore.fetchCurrentPlayingQueue()
    }

    override fun cacheCurrentPlaylistData() {
        playlistStore.cachePlaylistData()
    }

    companion object {

        @Volatile
        private var INSTANCE: PlaylistRepository? = null

        fun getInstance( playlistStore: PlaylistStore ): PlaylistRepository {
            return INSTANCE ?: synchronized( this ) {
                PlaylistRepositoryImpl( playlistStore ).also { INSTANCE = it }
            }

        }
    }
}

const val PLAYLIST_REPOSITORY_TAG = "PLAYLIST-REPOSITORY-TAG"