package com.odesa.musicMatters.data

import android.content.ComponentName
import android.content.Context
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.PlaylistStore
import com.odesa.musicMatters.data.playlists.impl.PlaylistRepositoryImpl
import com.odesa.musicMatters.data.playlists.impl.PlaylistStoreImpl
import com.odesa.musicMatters.data.preferences.PreferenceStore
import com.odesa.musicMatters.data.preferences.impl.PreferenceStoreImpl
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.data.settings.impl.SettingsRepositoryImpl
import com.odesa.musicMatters.services.media.MusicService
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.MusicServiceConnectionImpl
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val preferenceStore: PreferenceStore
    val playlistStore: PlaylistStore
    val settingsRepository: SettingsRepository
    val playlistRepository: PlaylistRepository
    val musicServiceConnection: MusicServiceConnection
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl( private val context: Context ) : AppContainer {

    override val preferenceStore = PreferenceStoreImpl( context )
    override val playlistStore = PlaylistStoreImpl.getInstance(
        retrievePlaylistFileFromAppExternalCache( context ),
        retrieveMostPlayedSongsFileFromAppExternalCache( context )
    )
    override val settingsRepository = SettingsRepositoryImpl( preferenceStore )
    override val playlistRepository = PlaylistRepositoryImpl.getInstance( playlistStore )
    override val musicServiceConnection = MusicServiceConnectionImpl.getInstance(
        context,
        ComponentName( context, MusicService::class.java )
    )

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
                } catch ( exception: IOException ) {
                    Timber.tag( TAG ).d( "ERROR WHILE CREATE FILE: ${file.absolutePath}" )
                }
            }
            return file
        }
    }

}

const val TAG = "APP CONTAINER TAG"
