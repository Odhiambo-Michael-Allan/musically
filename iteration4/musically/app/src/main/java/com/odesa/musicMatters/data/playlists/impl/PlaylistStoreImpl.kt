package com.odesa.musicMatters.data.playlists.impl

import android.content.Context
import android.provider.MediaStore
import com.odesa.musicMatters.data.playlists.Playlist

import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.utils.toSet
import org.json.JSONArray

import org.json.JSONObject
import timber.log.Timber
import java.io.File

import java.io.IOException
import java.util.UUID


class PlaylistStoreImpl( context: Context ) : PlaylistStore {

    private var adapter: FileAdapter

    init {
        val file = File( context.dataDir.absolutePath, "playlists.json" )
        if ( !file.exists() ) {
            // Create the file if it doesn't exist
            Timber.tag( TAG ).d( "Creating new playlists.json file" )
            try {
                file.createNewFile()
            } catch ( e: IOException ) {
                Timber.d( "Error creating file: ${file.absolutePath}" )
            }
        }
        adapter = FileAdapter( file )
    }

    private fun fetchPlaylistData() = PlaylistData.fromJSONObject( adapter.read() )

    override fun fetchPlaylists() = fetchPlaylistData().playlists

    override fun fetchFavoritesPlaylist(): Playlist {
        return fetchPlaylistData().favorites
    }

    override suspend fun addToFavorites( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val favoritesPlaylist = currentPlaylistData.favorites
        val songIds = favoritesPlaylist.songIds.toMutableSet()
        songIds.add( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy(
            songIds = songIds
        )
        val newPlaylistData = currentPlaylistData.copy(
            favorites = newFavoritesPlaylist
        )
        adapter.overwrite( newPlaylistData.toJSONObject().toString() )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentPlaylistData = fetchPlaylistData()
        val favoritesPlaylist = currentPlaylistData.favorites
        val songIds = favoritesPlaylist.songIds.toMutableSet()
        songIds.remove( songId )
        val newFavoritesPlaylist = favoritesPlaylist.copy(
            songIds = songIds
        )
        val newPlaylistData = currentPlaylistData.copy(
            favorites = newFavoritesPlaylist
        )
        adapter.overwrite( newPlaylistData.toJSONObject().toString() )
    }

    override suspend fun savePlaylist( playlist: Playlist ) {
        val currentPlaylistData = fetchPlaylistData()
        val currentPlaylists = currentPlaylistData.playlists.toMutableSet()
        currentPlaylists.add( playlist )
        adapter.overwrite(
            currentPlaylistData.copy(
                playlists = currentPlaylists
            ).toJSONObject().toString()
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    companion object {

        private const val FILES_EXTERNAL_VOLUME = "external"

        private fun getExternalVolumeUri() =
            MediaStore.Files.getContentUri( FILES_EXTERNAL_VOLUME )

        private fun getExternalVolumeUri( rowId: Long ) =
            MediaStore.Files.getContentUri( FILES_EXTERNAL_VOLUME, rowId )
    }
}

data class PlaylistData(
    val playlists: Set<Playlist>,
    val favorites: Playlist
) {

    fun toJSONObject() = JSONObject().apply {
        put( CUSTOM_PLAYLISTS, JSONArray( playlists.map { it.toJSONObject() } ) )
        put( FAVORITES_PLAYLIST, favorites.toJSONObject() )
    }

    companion object {
        private const val CUSTOM_PLAYLISTS = "0"
        private const val FAVORITES_PLAYLIST = "1"

        fun fromJSONObject( content: String ): PlaylistData {
            return when {
                content.isEmpty() -> PlaylistData(
                    playlists = emptySet(),
                    favorites = createFavoritesPlaylist()
                )
                else -> {
                    val jsonContent = JSONObject( content )
                    PlaylistData(
                        playlists = when {
                            jsonContent.has( CUSTOM_PLAYLISTS ) -> jsonContent.getJSONArray( CUSTOM_PLAYLISTS )
                                .toSet { Playlist.fromJSONObject( getJSONObject( it ) ) }
                            else -> emptySet()
                        },
                        favorites = when {
                            jsonContent.has( FAVORITES_PLAYLIST ) -> Playlist.fromJSONObject( jsonContent.getJSONObject( FAVORITES_PLAYLIST ) )
                            else -> createFavoritesPlaylist()
                        }
                    )
                }
            }
        }

        private fun createFavoritesPlaylist() = Playlist(
            id = UUID.randomUUID().toString(),
            title = "Favorites",
            songIds = emptySet(),
            numberOfTracks = 0
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
        Timber.tag( TAG ).d( "CONTENT READ BY FILE ADAPTER: $contentRead" )
        return contentRead
    }
}

val TAG = "PLAYLIST-STORE"