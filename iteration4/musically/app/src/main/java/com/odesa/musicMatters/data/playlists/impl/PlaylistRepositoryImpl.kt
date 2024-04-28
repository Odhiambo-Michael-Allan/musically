package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

enum class SortPlaylistBy {
    CUSTOM,
    TITLE,
    TRACKS_COUNT,
}

class PlaylistRepositoryImpl( private val playlistStore: PlaylistStore ) : PlaylistRepository {

    private val _favoritesPlaylist = MutableStateFlow( playlistStore.fetchFavoritesPlaylist() )
    override val favoritesPlaylist = _favoritesPlaylist.asStateFlow()

    private val _recentSongsPlaylist = MutableStateFlow( playlistStore.fetchRecentSongsPlaylist() )
    override val recentSongsPlaylist = _recentSongsPlaylist.asStateFlow()

    private val _mostPlayedSongsPlaylist = MutableStateFlow( playlistStore.fetchMostPlayedSongsPlaylist() )
    override val mostPlayedSongsPlaylist = _mostPlayedSongsPlaylist.asStateFlow()

    private val _playlists = MutableStateFlow( playlistStore.fetchAllPlaylists() )
    override val playlists = _playlists.asStateFlow()

    private val _mostPlayedSongsMap = MutableStateFlow( playlistStore.fetchMostPlayedSongsMap() )
    override val mostPlayedSongsMap = _mostPlayedSongsMap.asStateFlow()

    override fun isFavorite( songId: String ): Boolean {
        return favoritesPlaylist.value.songIds.contains( songId )
    }

    override suspend fun addToFavorites( songId: String ) {
        withContext( Dispatchers.IO ) {
            playlistStore.addToFavorites( songId )
            _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
        }
    }

    override suspend fun removeFromFavorites( songId: String ) {
        withContext( Dispatchers.IO ) {
            playlistStore.removeFromFavorites( songId )
            _favoritesPlaylist.value = playlistStore.fetchFavoritesPlaylist()
        }
    }

    override suspend fun addToRecentSongsPlaylist( songId: String ) {
        withContext( Dispatchers.IO ) {
            playlistStore.addSongIdToRecentSongsPlaylist( songId )
            _recentSongsPlaylist.value = playlistStore.fetchRecentSongsPlaylist()
        }
    }

    override suspend fun removeFromRecentSongsPlaylist( songId: String ) {
        withContext( Dispatchers.IO ) {
            playlistStore.removeFromRecentSongsPlaylist( songId )
            _recentSongsPlaylist.value = playlistStore.fetchRecentSongsPlaylist()
        }
    }

    override suspend fun addToMostPlayedPlaylist( songId: String ) {
        withContext( Dispatchers.IO ) {
            Timber.tag( PLAYLIST_REPOSITORY_TAG ).d( "ADDING SONG TO MOST PLAYED PLAYLIST. ID: $songId" )
            playlistStore.addToMostPlayedSongsPlaylist( songId )
            _mostPlayedSongsPlaylist.value = playlistStore.fetchMostPlayedSongsPlaylist()
            _mostPlayedSongsMap.value = playlistStore.fetchMostPlayedSongsMap()
        }
    }

    override suspend fun removeFromMostPlayedPlaylist( songId: String ) {
        withContext( Dispatchers.IO ) {
            playlistStore.removeFromMostPlayedSongsPlaylist( songId )
            _mostPlayedSongsMap.value = playlistStore.fetchMostPlayedSongsMap()
            _mostPlayedSongsPlaylist.value = playlistStore.fetchMostPlayedSongsPlaylist()
        }
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        playlistStore.saveCustomPlaylist( playlist )
        _playlists.value = playlistStore.fetchAllPlaylists()
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        playlistStore.deleteCustomPlaylist( playlist )
        _playlists.value = playlistStore.fetchAllPlaylists()
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