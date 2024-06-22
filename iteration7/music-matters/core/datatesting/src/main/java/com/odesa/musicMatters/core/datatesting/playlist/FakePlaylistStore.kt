package com.odesa.musicMatters.core.datatesting.playlist

import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.model.Playlist

class FakePlaylistStore : PlaylistStore {

    private val mostPlayedSongsMap = mutableMapOf<String, Int>()
    private val FAVORITES_PLAYLIST_ID = "--FAVORITES-PLAYLIST-ID--"
    private val RECENTLY_PLAYED_SONGS_PLAYLIST_ID = "--RECENTLY-PLAYED-SONGS-PLAYLIST-ID--"
    private val MOST_PLAYED_SONGS_PLAYLIST_ID = "--MOST-PLAYED-SONGS-PLAYLIST-ID--"
    private val CURRENT_QUEUE_PLAYLIST_ID = "--CURRENT-QUEUE-PLAYLIST-ID--"

    private var favoritePlaylist = Playlist(
        id = FAVORITES_PLAYLIST_ID,
        title = "Favorites",
        songIds = emptyList(),
    )

    private var recentSongsPlaylist = Playlist(
        id = RECENTLY_PLAYED_SONGS_PLAYLIST_ID,
        title = "Recently Played Songs",
        songIds = emptyList()
    )

    private var mostPlayedSongsPlaylist = Playlist(
        id = MOST_PLAYED_SONGS_PLAYLIST_ID,
        title = "Most played songs",
        songIds = emptyList()
    )

    private var currentQueuePlaylist = Playlist(
        id = CURRENT_QUEUE_PLAYLIST_ID,
        title = "",
        songIds = emptyList()
    )

    private var customPlaylists = mutableListOf<Playlist>()

    override fun fetchAllPlaylists() = mutableListOf<Playlist>().apply {
        add( favoritePlaylist )
        add( recentSongsPlaylist )
        add( mostPlayedSongsPlaylist )
        addAll( customPlaylists )
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

    override fun fetchEditablePlaylists() = customPlaylists

    override suspend fun savePlaylist(playlist: Playlist ) {
        println( "SAVING PLAYLIST WITH ID: ${playlist.id} AND TITLE: ${playlist.title}" )
        customPlaylists.add( playlist )
    }

    override suspend fun deletePlaylist(playlist: Playlist ) {
        customPlaylists.remove( playlist )
    }

    override suspend fun addSongIdToPlaylist( songId: String, playlist: Playlist ) {
        customPlaylists.find { it.id == playlist.id } ?. let {
            val currentSongIds = it.songIds.toMutableList()
            currentSongIds.add( songId )
            customPlaylists.remove( it )
            customPlaylists.add(
                it.copy(
                    songIds = currentSongIds
                )
            )
        }
    }

    override fun fetchMostPlayedSongsMap(): Map<String, Int> = mostPlayedSongsMap

    override suspend fun renamePlaylist( playlist: Playlist, newTitle: String ) {
        println( "RENAMING PLAYLIST TO $newTitle FROM: ${playlist.title}" )
        customPlaylists.find { it.id == playlist.id }?.let {
            println( "RENAMING PLAYLIST TO $newTitle" )
            customPlaylists.remove( it )
            customPlaylists.add( it.copy( title = newTitle ) )
        }
    }

}