package com.odesa.musicMatters.data.playlists.impl

import android.content.Context
import android.provider.MediaStore
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistStore
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID


class PlaylistStoreImpl( context: Context ) : PlaylistStore {

    private var adapter: FileAdapter

    init {
        val file = File( context.dataDir.absolutePath, "favorites-playlist.json" )
        if ( !file.exists() ) {
            // Create the file if it doesn't exist
            Timber.tag( TAG ).d( "Creating new favorites-playlist.json file" )
            try {
                file.createNewFile()
            } catch ( e: IOException ) {
                Timber.d( "Error creating file: ${file.absolutePath}" )
            }
        }
        adapter = FileAdapter( file )
    }

//    override fun fetchPlaylists(): List<Playlist> {
//        val playlists = mutableListOf<Playlist>()
//        try {
//            val favoritesPlaylist = readCachedFavoritePlaylistData()
////            val playlistStoredOnDevice = fetchPlaylistStoredOnDevice()
//            playlists.add( favoritesPlaylist )
////            playlists.addAll( playlistStoredOnDevice )
//        } catch ( exception: Exception ) {
//            Timber.e( "An error occurred while fetching playlists.", exception )
//        }
//        return playlists
//    }

//    override fun savePlaylist( playlist: Playlist ) {}
//
//    override fun savePlaylists( playlists: List<Playlist> ) {
//        TODO("Not yet implemented")
//    }

    override fun fetchFavoritesPlaylist(): Playlist {
        val content = adapter.read()
        if ( content.isEmpty() )
            return Playlist(
                id = UUID.randomUUID().toString(),
                title = "Favorites",
                songIds = emptySet(),
                numberOfTracks = 0
            )
        return Playlist.fromJSONObject( JSONObject( content ) )
    }

//    private fun fetchPlaylistStoredOnDevice(): List<Playlist> {
//        val playlists = mutableListOf<Playlist>()
//        context.contentResolver.query(
//            getExternalVolumeUri(),
//            null,
//            MediaStore.Files.FileColumns.MIME_TYPE + " == ?",
//            arrayOf(M3U.mimeType),
//            null
//        )?.use { cursor ->
//            while ( cursor.moveToNext() ) {
//                val playlistId = cursor.getLongFrom( MediaStore.Files.FileColumns._ID )
//                val path = cursor.getStringFrom( MediaStore.Files.FileColumns.DATA )
//                kotlin.runCatching {
//                    Playlist.fromM3U(context, path, getExternalVolumeUri(playlistId))
//                }.getOrNull() ?. let { playlists.add( it ) }
//            }
//        }
//        return playlists
//    }

    override suspend fun addToFavorites(songId: String ) {
        val currentFavoritePlaylist = fetchFavoritesPlaylist()
        val songIds = currentFavoritePlaylist.songIds.toMutableSet()
        songIds.add( songId )
        adapter.overwrite( currentFavoritePlaylist.copy(
            songIds = songIds,
            numberOfTracks = songIds.size
        ).toJSONObject().toString() )
    }

    override suspend fun removeFromFavorites( songId: String ) {
        val currentFavoritePlaylist = fetchFavoritesPlaylist()
        val songIds = currentFavoritePlaylist.songIds.toMutableSet()
        songIds.remove( songId )
        adapter.overwrite( currentFavoritePlaylist.copy(
            songIds = songIds,
            numberOfTracks = songIds.size
        ).toJSONObject().toString() )
    }

    companion object {

        private const val FILES_EXTERNAL_VOLUME = "external"

        private fun getExternalVolumeUri() =
            MediaStore.Files.getContentUri( FILES_EXTERNAL_VOLUME )

        private fun getExternalVolumeUri( rowId: Long ) =
            MediaStore.Files.getContentUri( FILES_EXTERNAL_VOLUME, rowId )
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