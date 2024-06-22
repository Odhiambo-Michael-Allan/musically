package com.odesa.musicMatters.core.data.playlists.impl

import com.odesa.musicMatters.core.data.playlists.PlaylistStore
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.toList
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File

class PlaylistStoreImpl( playlistsFile: File, mostPlayedSongsFile: File ) : PlaylistStore {

    private val playlistsFileAdapter: FileAdapter = FileAdapter( playlistsFile )
    private val mostPlayedSongsFileAdapter: FileAdapter = FileAdapter( mostPlayedSongsFile )
    private var mostPlayedSongsPlaylist: Playlist

    init {
        mostPlayedSongsPlaylist = loadMostPlayedSongsPlaylist()
    }

    override fun fetchAllPlaylists(): List<Playlist> {
        val playlists = mutableListOf(
            mostPlayedSongsPlaylist
        )
        playlists.addAll(
            fetchPlaylistData().playlists
        )
        return playlists
    }

    private fun fetchPlaylistData() = PlaylistData.fromJSONObject( playlistsFileAdapter.read() )

    override fun fetchFavoritesPlaylist() = fetchPlaylistData().playlists.find {
        it.id == FAVORITES_PLAYLIST_ID
    }!!

    override suspend fun addToFavorites( songId: String ) {
        addSongIdToPlaylist( FAVORITES_PLAYLIST_ID, songId )
    }

    private fun addSongIdToPlaylist( playlistId: String, songId: String ) {
        fetchPlaylistData().playlists.find { it.id == playlistId }?.let {
            val songIdsCurrentlyInPlaylist = it.songIds.toMutableList()
            if ( songIdsCurrentlyInPlaylist.contains( songId ) ) songIdsCurrentlyInPlaylist.remove( songId )
            songIdsCurrentlyInPlaylist.add( 0, songId )
            val newPlaylist = it.copy( songIds = songIdsCurrentlyInPlaylist )
            val currentPlaylists = fetchPlaylistData().playlists.toMutableList()
            currentPlaylists.remove( it )
            currentPlaylists.add( newPlaylist )
            playlistsFileAdapter.overwrite(
                fetchPlaylistData().copy( playlists = currentPlaylists ).toJSONObject().toString()
            )
        }
    }

    override suspend fun removeFromFavorites( songId: String ) {
        removeSongIdFromPlaylist( FAVORITES_PLAYLIST_ID, songId )
    }

    private fun removeSongIdFromPlaylist( playlistId: String, songId: String ) {
        fetchPlaylistData().playlists.find { it.id == playlistId }?.let {
            val songIdsCurrentlyInPlaylist = it.songIds.toMutableList()
            if ( songIdsCurrentlyInPlaylist.contains( songId ) ) songIdsCurrentlyInPlaylist.remove( songId )
            val newPlaylist = it.copy( songIds = songIdsCurrentlyInPlaylist )
            val currentPlaylists = fetchPlaylistData().playlists.toMutableList()
            currentPlaylists.remove( it )
            currentPlaylists.add( newPlaylist )
            playlistsFileAdapter.overwrite(
                fetchPlaylistData().copy( playlists = currentPlaylists ).toJSONObject().toString()
            )
        }
    }

    override fun fetchRecentlyPlayedSongsPlaylist() = fetchPlaylistData().playlists.find {
        it.id == RECENTLY_PLAYED_SONGS_PLAYLIST_ID
    }!!

    override suspend fun addSongIdToRecentlyPlayedSongsPlaylist( songId: String ) {
        addSongIdToPlaylist( RECENTLY_PLAYED_SONGS_PLAYLIST_ID, songId )
    }

    override suspend fun removeFromRecentlyPlayedSongsPlaylist( songId: String ) {
        removeSongIdFromPlaylist( RECENTLY_PLAYED_SONGS_PLAYLIST_ID, songId )
    }

    override fun fetchMostPlayedSongsPlaylist() = mostPlayedSongsPlaylist

    private fun loadMostPlayedSongsPlaylist(): Playlist {
        val fileContents = mostPlayedSongsFileAdapter.read()
        return if ( fileContents.isEmpty() ) createMostPlayedSongsPlaylist()
        else Playlist(
            id = MOST_PLAYED_SONGS_PLAYLIST_ID,
            title = MOST_PLAYED_SONGS_PLAYLIST_TITLE,
            songIds = fetchMostPlayedSongsMap().toList().sortedByDescending { it.second }.toMap().keys.toList()
        )
    }

    private fun createMostPlayedSongsPlaylist() = Playlist(
        id = MOST_PLAYED_SONGS_PLAYLIST_ID,
        title = MOST_PLAYED_SONGS_PLAYLIST_TITLE,
        songIds = emptyList(),
    )

    override suspend fun addToMostPlayedSongsPlaylist( songId: String ) {
        val songIdToPlayCountMap = fetchMostPlayedSongsMap().toMutableMap()
        if ( songIdToPlayCountMap.contains( songId ) ) songIdToPlayCountMap[ songId ] = songIdToPlayCountMap[ songId ]!!.plus( 1 )
        else songIdToPlayCountMap[ songId ] = 1
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = songIdToPlayCountMap.toList().sortedByDescending { it.second }.map { it.first }
        )
        cachedMostPlayedSongsMap( songIdToPlayCountMap )
    }

    override suspend fun removeFromMostPlayedSongsPlaylist( songId: String ) {
        val songIdToPlayCountMap = fetchMostPlayedSongsMap().toMutableMap()
        if ( songIdToPlayCountMap.contains( songId ) ) songIdToPlayCountMap.remove( songId )
        mostPlayedSongsPlaylist = mostPlayedSongsPlaylist.copy(
            songIds = songIdToPlayCountMap.keys.toList()
        )
        cachedMostPlayedSongsMap( songIdToPlayCountMap )
    }

    private fun cachedMostPlayedSongsMap( map: Map<String, Int> ) {
        val newJsonObject = JSONObject()
        val sortedMap = map.toList().sortedByDescending { it.second }.toMap()
        for ( entry in sortedMap )
            newJsonObject.put( entry.key, entry.value )
        mostPlayedSongsFileAdapter.overwrite( newJsonObject.toString() )
    }

    override fun fetchEditablePlaylists() = fetchPlaylistData().playlists.filter {
        it.id != FAVORITES_PLAYLIST_ID && it.id != RECENTLY_PLAYED_SONGS_PLAYLIST_ID
                && it.id != MOST_PLAYED_SONGS_PLAYLIST_ID
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        val currentPlaylists = fetchPlaylistData().playlists.toMutableList()
        currentPlaylists.add( playlist )
        playlistsFileAdapter.overwrite(
            fetchPlaylistData().copy(
                playlists = currentPlaylists
            ).toJSONObject().toString()
        )
    }

    override suspend fun deletePlaylist( playlist: Playlist ) {
        val currentEditablePlaylists = fetchEditablePlaylists().toMutableList()
        currentEditablePlaylists.removeIf { it.id == playlist.id }
        playlistsFileAdapter.overwrite(
            fetchPlaylistData().copy(
                playlists = currentEditablePlaylists.apply {
                    add( fetchFavoritesPlaylist() )
                    add( fetchRecentlyPlayedSongsPlaylist() )
                }
            ).toJSONObject().toString()
        )
    }

    override suspend fun addSongIdToPlaylist( songId: String, playlist: Playlist ) {
        addSongIdToPlaylist( playlist.id, songId )
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

    override suspend fun renamePlaylist( playlist: Playlist, newTitle: String ) {
        this.fetchEditablePlaylists().find { it.id == playlist.id }?.let {
            val renamedPlaylist = it.copy(
                title = newTitle
            )
            deletePlaylist( it )
            savePlaylist( renamedPlaylist )
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
    val playlists: List<Playlist>,
) {

    fun toJSONObject() = JSONObject().apply {
        put( PLAYLISTS_KEY, JSONArray( playlists.map { it.toJSONObject() } ) )
    }

    companion object {

        fun fromJSONObject( content: String ): PlaylistData {
            return when {
                content.isEmpty() -> {
                    PlaylistData(
                        playlists = listOf(
                            // These three must be present to create them..
                            createFavoriteSongsPlaylist(),
                            createRecentlyPlayedSongsPlaylist(),
                        ),
                    )
                }
                else -> {
                    val jsonContent = JSONObject( content )
                    verifyCachedPlaylistData( jsonContent )
                }
            }
        }

        private fun verifyCachedPlaylistData( data: JSONObject ): PlaylistData {
            val cachedPlaylists = when {
                data.has( PLAYLISTS_KEY ) -> {
                    data.getJSONArray( PLAYLISTS_KEY )
                        .toList { Playlist.fromJSONObject( getJSONObject( it ) ) }
                }
                else -> emptyList()
            }.toMutableList()
            // Just in case, check whether Favorites playlist and recently
            // played songs playlist are present. If not, add them since they MUST BE PRESENT!!
            cachedPlaylists.find { it.id == FAVORITES_PLAYLIST_ID }
                ?: cachedPlaylists.add( createFavoriteSongsPlaylist() )
            cachedPlaylists.find { it.id == RECENTLY_PLAYED_SONGS_PLAYLIST_ID }
                ?: cachedPlaylists.add( createRecentlyPlayedSongsPlaylist() )
            return PlaylistData(
                playlists = cachedPlaylists
            )
        }

        private fun createFavoriteSongsPlaylist() = Playlist(
            id = FAVORITES_PLAYLIST_ID,
            title = FAVORITES_PLAYLIST_TITLE,
            songIds = emptyList(),
        )

        private fun createRecentlyPlayedSongsPlaylist() = Playlist(
            id = RECENTLY_PLAYED_SONGS_PLAYLIST_ID,
            title = RECENTLY_PLAYED_SONGS_PLAYLIST_TITLE,
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

private const val FAVORITES_PLAYLIST_ID = "--MUSIC-MATTERS-FAVORITES-PLAYLIST-ID--"
private const val FAVORITES_PLAYLIST_TITLE = "Favorites"

private const val RECENTLY_PLAYED_SONGS_PLAYLIST_ID = "--MUSIC-MATTERS-RECENTLY-PLAYED-SONGS-PLAYLIST-ID"
private const val RECENTLY_PLAYED_SONGS_PLAYLIST_TITLE = "Recently Played Songs"

private const val MOST_PLAYED_SONGS_PLAYLIST_ID = "--MUSIC-MATTERS-MOST-PLAYED-SONGS-PLAYLIST-ID--"
private const val MOST_PLAYED_SONGS_PLAYLIST_TITLE = "Most Played Songs"

private const val PLAYLISTS_KEY = "--MUSIC-MATTERS-CUSTOM-PLAYLISTS-ID--"