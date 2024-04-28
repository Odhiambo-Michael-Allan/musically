package com.odesa.musicMatters.data.playlists.impl

import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.utils.toSet
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.util.UUID


class PlaylistStoreImpl private constructor( playlistsFile: File, mostPlayedSongsFile: File ) : PlaylistStore {

    private val playlistsFileAdapter: FileAdapter = FileAdapter( playlistsFile )
    private val mostPlayedSongsFileAdapter: FileAdapter = FileAdapter( mostPlayedSongsFile )

    override fun fetchAllPlaylists(): Set<Playlist> {
        val playlistData = fetchPlaylistData()
        val playlists = mutableSetOf( playlistData.favoritesPlaylist, playlistData.recentlyPlayedPlaylist )
        playlists.addAll( playlistData.customPlaylists )
        val mostPlayedSongsPlaylist = fetchMostPlayedSongsPlaylist()
        playlists.add( mostPlayedSongsPlaylist )
        return playlists
    }

    private fun fetchPlaylistData() = PlaylistData.fromJSONObject( playlistsFileAdapter.read() )

    override fun fetchFavoritesPlaylist(): Playlist {
        return fetchPlaylistData().favoritesPlaylist
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val favoritesPlaylist = currentPlaylistData.favoritesPlaylist
        val songIds = favoritesPlaylist.songIds.toMutableSet()
        songIds.add( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy(
            songIds = songIds
        )
        val newPlaylistData = currentPlaylistData.copy(
            favoritesPlaylist = newFavoritesPlaylist
        )
        playlistsFileAdapter.overwrite( newPlaylistData.toJSONObject().toString() )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val favoritesPlaylist = currentPlaylistData.favoritesPlaylist
        val songIds = favoritesPlaylist.songIds.toMutableSet()
        songIds.remove( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy(
            songIds = songIds,
        )
        val newPlaylistData = currentPlaylistData.copy(
            favoritesPlaylist = newFavoritesPlaylist
        )
        playlistsFileAdapter.overwrite( newPlaylistData.toJSONObject().toString() )
    }

    override fun fetchRecentSongsPlaylist() = fetchPlaylistData().recentlyPlayedPlaylist

    override suspend fun addSongIdToRecentSongsPlaylist( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val recentSongsPlaylist = currentPlaylistData.recentlyPlayedPlaylist
        val songIds = recentSongsPlaylist.songIds.toMutableSet()
        songIds.add( songId )
        val newRecentSongsPlaylist = recentSongsPlaylist.copy(
            songIds = songIds
        )
        val newPlaylistData = currentPlaylistData.copy(
            recentlyPlayedPlaylist = newRecentSongsPlaylist
        )
        playlistsFileAdapter.overwrite( newPlaylistData.toJSONObject().toString() )

    }

    override suspend fun removeFromRecentSongsPlaylist( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val recentSongsPlaylist = currentPlaylistData.recentlyPlayedPlaylist
        val songIds = recentSongsPlaylist.songIds.toMutableSet()
        songIds.remove( songId )
        val newRecentSongsPlaylist = recentSongsPlaylist.copy(
            songIds = songIds
        )
        val newPlaylistData = currentPlaylistData.copy(
            recentlyPlayedPlaylist = newRecentSongsPlaylist
        )
        playlistsFileAdapter.overwrite( newPlaylistData.toJSONObject().toString() )
    }

    override fun fetchMostPlayedSongsPlaylist(): Playlist {
        val fileContents = mostPlayedSongsFileAdapter.read()
        return if ( fileContents.isEmpty() ) createMostPlayedSongsPlaylist()
        else loadMostPlayedSongsPlaylistFrom( fileContents )
    }

    private fun createMostPlayedSongsPlaylist() = Playlist(
        id = UUID.randomUUID().toString(),
        title = "Most Played Songs",
        songIds = emptySet(),
    )

    private fun loadMostPlayedSongsPlaylistFrom( fileContents: String ): Playlist {
        Timber.tag( PLAYLIST_STORE_TAG ).d( "LOADING MOST PLAYED SONGS PLAYLIST.." )
        val jsonObject = JSONObject( fileContents )
        val songIdToPlayCountMap = mutableMapOf<String, Int>()
        val keys = jsonObject.keys()
        while ( keys.hasNext() ) {
            val key = keys.next()
            val value = jsonObject.getInt( key )
            songIdToPlayCountMap[ key ] = value
        }
        val sortedMap = songIdToPlayCountMap.toList().sortedByDescending { it.second }.toMap()
        Timber.tag( PLAYLIST_STORE_TAG ).d( "SORTED MAP OF MOST PLAYED SONGS: $sortedMap" )
        return Playlist(
            id = UUID.randomUUID().toString() + PlaylistData.MOST_PLAYED_SONGS_PLAYLIST,
            title = "Most Played Songs",
            songIds = songIdToPlayCountMap.toList().sortedByDescending { it.second }.toMap().keys
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
    }

    override fun fetchAllCustomPlaylists() = fetchPlaylistData().customPlaylists

    override suspend fun saveCustomPlaylist( playlist: Playlist ) {
        val currentPlaylistData = fetchPlaylistData()
        val currentPlaylists = currentPlaylistData.customPlaylists.toMutableSet()
        currentPlaylists.add( playlist )
        playlistsFileAdapter.overwrite(
            currentPlaylistData.copy(
                customPlaylists = currentPlaylists
            ).toJSONObject().toString()
        )
    }

    override suspend fun deleteCustomPlaylist(playlist: Playlist ) {
        val currentPlaylistData = fetchPlaylistData()
        val currentPlaylists = currentPlaylistData.customPlaylists.toMutableSet()
        currentPlaylists.removeIf {
            it.id == playlist.id
        }
        playlistsFileAdapter.overwrite(
            currentPlaylistData.copy(
                customPlaylists = currentPlaylists
            ).toJSONObject().toString()
        )
    }

    override suspend fun addSongToCustomPlaylist( songId: String, playlist: Playlist ) {
        val customPlaylists = fetchPlaylistData().customPlaylists
        customPlaylists.find { it.id == playlist.id }?.let {
            val currentSongIdsInPlaylist = it.songIds.toMutableSet()
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

        fun getInstance( playlistsFile: File, mostPlayedSongsFile: File ): PlaylistStore {
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
    val customPlaylists: Set<Playlist>,
    val favoritesPlaylist: Playlist,
    val recentlyPlayedPlaylist: Playlist
) {

    fun toJSONObject() = JSONObject().apply {
        put( CUSTOM_PLAYLISTS, JSONArray( customPlaylists.map { it.toJSONObject() } ) )
        put( FAVORITES_PLAYLIST, favoritesPlaylist.toJSONObject() )
        put( RECENTLY_PLAYED_PLAYLIST, recentlyPlayedPlaylist.toJSONObject() )
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
                        customPlaylists = emptySet(),
                        favoritesPlaylist = createFavoritesPlaylist(),
                        recentlyPlayedPlaylist = createRecentlyPlayedPlaylist()
                    )
                }
                else -> {
                    val jsonContent = JSONObject( content )
                    PlaylistData(
                        customPlaylists = when {
                            jsonContent.has( CUSTOM_PLAYLISTS ) -> jsonContent.getJSONArray( CUSTOM_PLAYLISTS )
                                .toSet { Playlist.fromJSONObject( getJSONObject( it ) ) }
                            else -> emptySet()
                        },
                        favoritesPlaylist = when {
                            jsonContent.has( FAVORITES_PLAYLIST ) -> Playlist.fromJSONObject( jsonContent.getJSONObject( FAVORITES_PLAYLIST ) )
                            else -> createFavoritesPlaylist()
                        },
                        recentlyPlayedPlaylist = when {
                            jsonContent.has( RECENTLY_PLAYED_PLAYLIST ) -> Playlist.fromJSONObject( jsonContent.getJSONObject( RECENTLY_PLAYED_PLAYLIST ) )
                            else -> createRecentlyPlayedPlaylist()
                        }
                    )
                }
            }
        }

        private fun createFavoritesPlaylist() = Playlist(
            id = UUID.randomUUID().toString() + FAVORITES_PLAYLIST,
            title = "Favorites",
            songIds = emptySet(),
        )

        private fun createRecentlyPlayedPlaylist() = Playlist(
            id = UUID.randomUUID().toString() + RECENTLY_PLAYED_PLAYLIST,
            title = "Recently Played Songs",
            songIds = emptySet(),
        )
    }
}

/**
 * An adapter that is used to read and write from the app's local cache.
 */
class FileAdapter( private val file: File ) {
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

val PLAYLIST_STORE_TAG = "PLAYLIST-STORE"