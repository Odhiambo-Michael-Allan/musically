package com.odesa.musicMatters.core.data.di

import android.content.Context
import com.odesa.musicMatters.core.data.playlists.impl.PlaylistRepositoryImpl
import com.odesa.musicMatters.core.data.playlists.impl.PlaylistStoreImpl
import com.odesa.musicMatters.core.data.preferences.impl.PreferenceStoreImpl
import com.odesa.musicMatters.core.data.search.impl.SearchHistoryRepositoryImpl
import com.odesa.musicMatters.core.data.search.impl.SearchHistoryStoreImpl
import com.odesa.musicMatters.core.data.settings.impl.SettingsRepositoryImpl
import timber.log.Timber
import java.io.File
import java.io.IOException

class DataDiModule( context: Context ) {

    private val preferenceStore = PreferenceStoreImpl( context )
    private val playlistStore = PlaylistStoreImpl.getInstance(
        playlistsFile = retrievePlaylistFileFromAppExternalCache( context ),
        mostPlayedSongsFile = retrieveMostPlayedSongsFileFromAppExternalCache( context )
    )
    private val searchHistoryStore = SearchHistoryStoreImpl(
        searchHistoryFile = retrieveFileFromAppExternalCache( SEARCH_HISTORY_FILE_NAME, context )
    )
    val settingsRepository = SettingsRepositoryImpl( preferenceStore )
    val playlistRepository = PlaylistRepositoryImpl.getInstance( playlistStore )
    val searchHistoryRepository = SearchHistoryRepositoryImpl( searchHistoryStore )


    companion object {
        fun retrievePlaylistFileFromAppExternalCache( context: Context ) = retrieveFileFromAppExternalCache(
            fileName = "playlists.json",
            context = context
        )

        fun retrieveMostPlayedSongsFileFromAppExternalCache( context: Context ) = retrieveFileFromAppExternalCache(
            fileName = "most-played-songs.json",
            context = context
        )

        private fun retrieveFileFromAppExternalCache( fileName: String, context: Context ): File {
            val file = File( context.dataDir.absolutePath, fileName )
            if ( !file.exists() ) {
                Timber.tag( TAG ).d( "CREATING NEW FILE IN APP EXTERNAL CACHE: $fileName" )
                try {
                    file.createNewFile()
                } catch ( exception: IOException) {
                    Timber.tag( TAG ).d( "ERROR WHILE CREATE FILE: ${file.absolutePath}" )
                }
            }
            return file
        }
    }
}

private const val PLAYLISTS_FILE_NAME = "playlists.json"
private const val MOST_PLAYED_SONGS_FILE_NAME = "most-played-songs.json"
private const val SEARCH_HISTORY_FILE_NAME = "search-history.json"
private const val TAG = "DATA-DI-MODULE-TAG"