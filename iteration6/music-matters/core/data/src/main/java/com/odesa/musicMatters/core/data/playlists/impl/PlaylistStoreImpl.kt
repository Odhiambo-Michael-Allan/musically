package com.odesa.musicMatters.core.data.playlists.impl

import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.toList
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.util.UUID

class PlaylistStoreImpl(playlistsFile: File, mostPlayedSongsFile: File) : PlaylistStore {

    private val playlistsFileAdapter: FileAdapter = FileAdapter( playlistsFile )
    private val mostPlayedSongsFileAdapter: FileAdapter = FileAdapter( mostPlayedSongsFile )
    private var currentPlaylistData: PlaylistData
    private var mostPlayedSongsPlaylist: Playlist

    init {
        currentPlaylistData = fetchPlaylistData()
        mostPlayedSongsPlaylist = loadMostPlayedSongsPlaylist()
    }


    override fun fetchAllPlaylists(): List<Playlist> {
        val playlists = mutableListOf( currentPlaylistData.favoritesPlaylist, currentPlaylistData.recentlyPlayedSongsPlaylist, mostPlayedSongsPlaylist )
        playlists.addAll( currentPlaylistData.customPlaylists )
        return playlists
    }

    private fun fetchPlaylistData() = PlaylistData.fromJSONObject( playlistsFileAdapter.read() )

    override fun fetchFavoritesPlaylist(): Playlist {
        return currentPlaylistData.favoritesPlaylist
    }

    override suspend fun addToFavorites( songId: String ) {
        val favoritesPlaylist = currentPlaylistData.favoritesPlaylist
        val songIds = favoritesPlaylist.songIds.toMutableList()
        if ( songIds.contains( songId ) ) return
        songIds.add( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy( songIds = songIds )
        currentPlaylistData = currentPlaylistData.copy( favoritesPlaylist = newFavoritesPlaylist )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val favoritesPlaylist = currentPlaylistData.favoritesPlaylist
        val songIds = favoritesPlaylist.songIds.toMutableList()
        songIds.remove( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy( songIds = songIds )
        currentPlaylistData = currentPlaylistData.copy( favoritesPlaylist = newFavoritesPlaylist )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override fun fetchRecentlyPlayedSongsPlaylist() = currentPlaylistData.recentlyPlayedSongsPlaylist

    override suspend fun addSongIdToRecentlyPlayedSongsPlaylist( songId: String ) {
        val recentSongsPlaylist = currentPlaylistData.recentlyPlayedSongsPlaylist
        val songIds = recentSongsPlaylist.songIds.toMutableList()
        songIds.remove( songId )
        songIds.add( 0, songId )
        val newRecentSongsPlaylist = recentSongsPlaylist.copy( songIds = songIds )
        currentPlaylistData = currentPlaylistData.copy(
            recentlyPlayedSongsPlaylist = newRecentSongsPlaylist
        )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override suspend fun removeFromRecentlyPlayedSongsPlaylist(songId: String ) {
        val recentSongsPlaylist = currentPlaylistData.recentlyPlayedSongsPlaylist
        val songIds = recentSongsPlaylist.songIds.toMutableList()
        songIds.remove( songId )
        val newRecentSongsPlaylist = recentSongsPlaylist.copy( songIds = songIds )
        currentPlaylistData = currentPlaylistData.copy(
            recentlyPlayedSongsPlaylist = newRecentSongsPlaylist
        )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override fun fetchMostPlayedSongsPlaylist() = mostPlayedSongsPlaylist

    private fun loadMostPlayedSongsPlaylist(): Playlist {
        val fileContents = mostPlayedSongsFileAdapter.read()
        return if ( fileContents.isEmpty() ) createMostPlayedSongsPlaylist()
        else loadMostPlayedSongsPlaylistFrom( fileContents )
    }

    private fun createMostPlayedSongsPlaylist() = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Most Played Songs",
        songIds = emptyList(),
    )

    private fun loadMostPlayedSongsPlaylistFrom( fileContents: String ): Playlist {
        val jsonObject = JSONObject( fileContents )
        val songIdToPlayCountMap = mutableMapOf<String, Int>()
        val keys = jsonObject.keys()
        while ( keys.hasNext() ) {
            val key = keys.next()
            val value = jsonObject.getInt( key )
            songIdToPlayCountMap[ key ] = value
        }
        return Playlist(
            id = UUID.randomUUID().toString() + PlaylistData.MOST_PLAYED_SONGS_PLAYLIST,
            title = "Most Played Songs",
            songIds = songIdToPlayCountMap.toList().sortedByDescending { it.second }.toMap().keys.toList()
        )
    }

    override suspend fun addToMostPlayedSongsPlaylist( songId: String ) {
        var jsonObject = JSONObject()
        val fileContent = mostPlayedSongsFileAdapter.read()
        if ( fileContent.isNotEmpty() )
            jsonObject = JSONObject( fileContent )
        val songIdToPlayCountMap = mutableMapOf<String, Int>()
        val keys = jsonObject.keys()
        while ( keys.hasNext() ) {
            val key = keys.next()
            val value = jsonObject.getInt( key )
            songIdToPlayCountMap[ key ] = value
        }
        if ( songIdToPlayCountMap.contains( songId ) ) songIdToPlayCountMap[ songId ] = songIdToPlayCountMap[ songId ]!!.plus( 1 )
        else songIdToPlayCountMap[ songId ] = 1
        val newJsonObject = JSONObject()
        val sortedMap = songIdToPlayCountMap.toList().sortedByDescending { it.second }.toMap()
        for ( entry in sortedMap )
            newJsonObject.put( entry.key, entry.value )
        mostPlayedSongsFileAdapter.overwrite( newJsonObject.toString() )
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = sortedMap.keys.toList()
        )
    }

    override suspend fun removeFromMostPlayedSongsPlaylist( songId: String ) {
        var jsonObject = JSONObject()
        val fileContent = mostPlayedSongsFileAdapter.read()
        if ( fileContent.isNotEmpty() )
            jsonObject = JSONObject( fileContent )
        val songIdToPlayCountMap = mutableMapOf<String, Int>()
        val keys = jsonObject.keys()
        while ( keys.hasNext() ) {
            val key = keys.next()
            val value = jsonObject.getInt( key )
            songIdToPlayCountMap[ key ] = value
        }
        if ( songIdToPlayCountMap.contains( songId ) ) songIdToPlayCountMap.remove( songId )
        val newJsonObject = JSONObject()
        for ( entry in songIdToPlayCountMap )
            newJsonObject.put( entry.key, entry.value )
        mostPlayedSongsFileAdapter.overwrite( newJsonObject.toString() )
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = songIdToPlayCountMap.keys.toList()
        )
    }

    override fun fetchAllCustomPlaylists() = currentPlaylistData.customPlaylists

    override suspend fun saveCustomPlaylist( playlist: Playlist ) {
        val currentPlaylists = currentPlaylistData.customPlaylists.toMutableList()
        currentPlaylists.add( playlist )
        currentPlaylistData = currentPlaylistData.copy(
            customPlaylists = currentPlaylists
        )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override suspend fun deleteCustomPlaylist( playlist: Playlist ) {
        val currentCustomPlaylists = currentPlaylistData.customPlaylists.toMutableList()
        currentCustomPlaylists.removeIf { it.id == playlist.id }
        currentPlaylistData = currentPlaylistData.copy( customPlaylists = currentCustomPlaylists )
        playlistsFileAdapter.overwrite( currentPlaylistData.toJSONObject().toString() )
    }

    override suspend fun addSongIdToPlaylist( songId: String, playlist: Playlist ) {
        if ( playlist.id == currentPlaylistData.favoritesPlaylist.id ) addToFavorites( songId )
        else addSongIdToCustomPlaylist( songId, playlist )
    }

    private suspend fun addSongIdToCustomPlaylist( songId: String, playlist: Playlist ) {
        val customPlaylists = currentPlaylistData.customPlaylists
        customPlaylists.find { it.id == playlist.id }?.let {
            val currentSongIdsInPlaylist = it.songIds.toMutableList()
            if ( currentSongIdsInPlaylist.contains( songId ) ) return
            currentSongIdsInPlaylist.add( songId )
            val modifiedPlaylist = it.copy(
                songIds = currentSongIdsInPlaylist
            )
            deleteCustomPlaylist( it )
            saveCustomPlaylist( modifiedPlaylist )
        }
    }

    override fun fetchMostPlayedSongsMap(): Map<String, Int> {
        val fileContents = mostPlayedSongsFileAdapter.read()
        return when {
            fileContents.isEmpty() -> mutableMapOf()
            else -> {
                val jsonObject = JSONObject( fileContents )
                val songIdToPlayCountMap = mutableMapOf<String, Int>()
                val keys = jsonObject.keys()
                while ( keys.hasNext() ) {
                    val key = keys.next()
                    val value = jsonObject.getInt( key )
                    songIdToPlayCountMap[ key ] = value
                }
                songIdToPlayCountMap
            }
        }
    }

    companion object {

        @Volatile
        private var instance: PlaylistStore? = null

        fun getInstance(playlistsFile: File, mostPlayedSongsFile: File): PlaylistStore {
            if ( instance == null ) {
                synchronized( this ) {
                    if ( instance == null ) {
                        instance = PlaylistStoreImpl(
                            playlistsFile = playlistsFile,
                            mostPlayedSongsFile = mostPlayedSongsFile
                        )
                    }
                }
            }
            return instance!!
        }
    }
}

data class PlaylistData(
    val customPlaylists: List<Playlist>,
    val favoritesPlaylist: Playlist,
    val recentlyPlayedSongsPlaylist: Playlist,
) {

    fun toJSONObject() = JSONObject().apply {
        put( CUSTOM_PLAYLISTS, JSONArray( customPlaylists.map { it.toJSONObject() } ) )
        put( FAVORITES_PLAYLIST, favoritesPlaylist.toJSONObject() )
        put( RECENTLY_PLAYED_PLAYLIST, recentlyPlayedSongsPlaylist.toJSONObject() )
    }

    companion object {
        private const val CUSTOM_PLAYLISTS = "0"
        private const val FAVORITES_PLAYLIST = "1"
        private const val RECENTLY_PLAYED_PLAYLIST = "2"
        const val MOST_PLAYED_SONGS_PLAYLIST = "3"

        fun fromJSONObject( content: String ): PlaylistData {
            return when {
                content.isEmpty() -> {
                    PlaylistData(
                        customPlaylists = emptyList(),
                        favoritesPlaylist = createFavoriteSongsPlaylist(),
                        recentlyPlayedSongsPlaylist = createRecentlyPlayedSongsPlaylist()
                    )
                }
                else -> {
                    val jsonContent = JSONObject( content )
                    PlaylistData(
                        customPlaylists = when {
                            jsonContent.has( CUSTOM_PLAYLISTS ) -> jsonContent.getJSONArray( CUSTOM_PLAYLISTS )
                                .toList { Playlist.fromJSONObject( getJSONObject( it ) ) }
                            else -> emptyList()
                        },
                        favoritesPlaylist = when {
                            jsonContent.has( FAVORITES_PLAYLIST ) -> Playlist.fromJSONObject( jsonContent.getJSONObject( FAVORITES_PLAYLIST ) )
                            else -> createFavoriteSongsPlaylist()
                        },
                        recentlyPlayedSongsPlaylist = when {
                            jsonContent.has( RECENTLY_PLAYED_PLAYLIST ) -> Playlist.fromJSONObject( jsonContent.getJSONObject( RECENTLY_PLAYED_PLAYLIST ) )
                            else -> createRecentlyPlayedSongsPlaylist()
                        }
                    )
                }
            }
        }

        private fun createFavoriteSongsPlaylist() = Playlist(
            id = UUID.randomUUID().toString() + FAVORITES_PLAYLIST,
            title = "Favorites",
            songIds = emptyList(),
        )

        private fun createRecentlyPlayedSongsPlaylist() = Playlist(
            id = UUID.randomUUID().toString() + RECENTLY_PLAYED_PLAYLIST,
            title = "Recently Played Songs",
            songIds = emptyList(),
        )
    }
}

/**
 * An adapter that is used to read and write from the app's local cache.
 */
class FileAdapter( private val file: File) {
    fun overwrite( content: String ) = overwrite( content.toByteArray() )
    private fun overwrite( bytes: ByteArray ) {
        file.outputStream().use {
            it.write( bytes )
        }
    }
    fun read(): String {
        val contentRead = file.inputStream().use { String( it.readBytes() ) }
        Timber.tag( PLAYLIST_STORE_TAG ).d( "CONTENT READ BY FILE ADAPTER: $contentRead" )
        return contentRead
    }
}

private const val PLAYLIST_STORE_TAG = "PLAYLIST-STORE"