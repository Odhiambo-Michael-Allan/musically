package com.odesa.musicMatters.data.playlists

import android.webkit.MimeTypeMap
import com.odesa.musicMatters.utils.toSet
import org.json.JSONArray
import org.json.JSONObject

data class Playlist(
    val id: String,
    val title: String,
    val songIds: Set<String>,
    val numberOfTracks: Int
) {

    fun toJSONObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put( PLAYLIST_ID_KEY, id )
        jsonObject.put( PLAYLIST_TITLE_KEY, title )
        jsonObject.put( PLAYLIST_SONGS_KEY, JSONArray( songIds ) )
        jsonObject.put( PLAYLIST_NUMBER_OF_TRACKS_KEY, numberOfTracks )
        return jsonObject
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "id"
        private const val PLAYLIST_TITLE_KEY = "title"
        private const val PLAYLIST_SONGS_KEY = "songs"
        private const val PLAYLIST_NUMBER_OF_TRACKS_KEY = "n_tracks"

        fun fromJSONObject( serialized: JSONObject ) = Playlist(
            id = serialized.getString( PLAYLIST_ID_KEY ),
            title = serialized.getString( PLAYLIST_TITLE_KEY ),
            songIds = serialized.getJSONArray( PLAYLIST_SONGS_KEY ).toSet { getString( it ) },
            numberOfTracks = serialized.getInt( PLAYLIST_NUMBER_OF_TRACKS_KEY ),
        )

//        fun fromM3U( context: Context, path: String, uri: Uri ): Playlist {
//            val filePath = FileExplorer.Path( path )
//            val directory = filePath.directoryName
//            val content = context.contentResolver.openInputStream( uri ) ?.use {
//                String( it.readBytes() )
//            } ?: ""
//            val m3u = M3U.parse( content )
//            val songPaths = mutableListOf<String>()
//            m3u.entries.forEach { entry ->
//                val resolvedPath = when {
//                    FileExplorer.Path.isAbsolute( entry.path ) -> entry.path
//                    else -> "/" + directory.resolve( FileExplorer.Path( entry.path ) ).toString()
//                }
//                songPaths.add( resolvedPath )
//            }
//            return Playlist(
//                id = path,
//                title = filePath.basename.removeSuffix( ".m3u" ),
//                songIds = songPaths,
//                numberOfTracks = songPaths.size,
//            )
//        }
    }
}

data class M3UEntry( val index: Int, val info: String, val path: String )

data class M3U( val entries: List<M3UEntry> ) {
    fun stringify(): String {
        val buffer = StringBuilder()
        buffer.append( "#EXTM3U" )
        entries.forEach {
            buffer.append( "\\n\\n\\n#EXTINF:${it.index},${it.info}\\n${it.path}" )
        }
        buffer.append( "\n\n" )
        return buffer.toString()
    }

    companion object {
        private val entryRegex = Regex( """#EXTINF:(\d+),(.+?)\n(.+)""" )
        val mimeType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension( "m3u" )
            ?: "application/x-mpegURL"

        fun parse( content: String ): M3U {
            val entries = entryRegex.findAll( content ).map {
                M3UEntry(
                    index = it.groupValues[1].toInt(),
                    info = it.groupValues[2],
                    path = it.groupValues[3],
                )
            }
            return M3U( entries.toList() )
        }
    }
}
