package com.odesa.musicMatters.core.datatesting.playlist

import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.model.Playlist
import java.util.UUID

class FakePlaylistStore : PlaylistStore {

    private val playlists = mutableListOf<Playlist>()
    private val mostPlayedSongsMap = mutableMapOf<String, Int>()

    private var favoritePlaylist = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Favorites",
        songIds = emptyList(),
    )

    private var recentSongsPlaylist = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Recently Played Songs",
        songIds = emptyList()
    )

    private var mostPlayedSongsPlaylist = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Most played songs",
        songIds = emptyList()
    )

    private var customPlaylists = List( 20 ) {
        Playlist(
            id = UUID.randomUUID().toString() + it,
            title = "Playlist-$it",
            songIds = emptyList()
        )
    }.toMutableList()

    init {
        playlists.add( favoritePlaylist )
        playlists.add( mostPlayedSongsPlaylist )
        playlists.add( recentSongsPlaylist )
    }

    override fun fetchAllPlaylists(): List<Playlist> {
        return playlists
    }

    override fun fetchFavoritesPlaylist(): Playlist {
        return favoritePlaylist
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentSongIds = favoritePlaylist.songIds.toMutableList()
        currentSongIds.add( songId )
        favoritePlaylist = favoritePlaylist.copy(
            songIds = currentSongIds
        )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentSongIds = favoritePlaylist.songIds.toMutableList()
        currentSongIds.remove( songId )
        favoritePlaylist = favoritePlaylist.copy(
            songIds = currentSongIds
        )
    }

    override fun fetchRecentlyPlayedSongsPlaylist(): Playlist {
        return recentSongsPlaylist
    }

    override suspend fun addSongIdToRecentlyPlayedSongsPlaylist( songId: String ) {
        val currentSongIds = recentSongsPlaylist.songIds.toMutableList()
        currentSongIds.remove( songId )
        currentSongIds.add( 0, songId )
        recentSongsPlaylist = recentSongsPlaylist.copy(
            songIds = currentSongIds
        )
    }

    override suspend fun removeFromRecentlyPlayedSongsPlaylist(songId: String ) {
        val currentSongIds = recentSongsPlaylist.songIds.toMutableList()
        currentSongIds.remove( songId )
        recentSongsPlaylist = recentSongsPlaylist.copy(
            songIds = currentSongIds
        )
    }

    override fun fetchMostPlayedSongsPlaylist() = mostPlayedSongsPlaylist

    override suspend fun addToMostPlayedSongsPlaylist( songId: String ) {
        val currentSongIds = mostPlayedSongsPlaylist.songIds.toMutableList()
        currentSongIds.add( songId )
        if ( mostPlayedSongsMap.contains( songId ) ) mostPlayedSongsMap[ songId ] = mostPlayedSongsMap[ songId ]!!.plus( 1 )
        else mostPlayedSongsMap[ songId ] = 1
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = currentSongIds
        )
    }

    override suspend fun removeFromMostPlayedSongsPlaylist( songId: String ) {
        val currentSongIds = mostPlayedSongsPlaylist.songIds.toMutableList()
        currentSongIds.remove( songId )
        if ( mostPlayedSongsMap.containsKey( songId ) ) mostPlayedSongsMap.remove( songId )
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = currentSongIds
        )
    }

    override fun fetchAllCustomPlaylists() = customPlaylists

    override suspend fun saveCustomPlaylist(playlist: Playlist ) {
        playlists.add( playlist )
    }

    override suspend fun deleteCustomPlaylist(playlist: Playlist ) {
        playlists.remove( playlist )
    }

    override suspend fun addSongIdToPlaylist(songId: String, playlist: Playlist ) {
        customPlaylists.find { it.id == playlist.id } ?. let {
            val currentSongIds = it.songIds.toMutableList()
            currentSongIds.add( songId )
            customPlaylists.remove( it )
            customPlaylists.add( it.copy(
                songIds = currentSongIds
            ) )
        }
    }

    override fun fetchMostPlayedSongsMap(): Map<String, Int> = mostPlayedSongsMap

}
