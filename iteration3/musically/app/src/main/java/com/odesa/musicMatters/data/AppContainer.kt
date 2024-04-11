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
class AppContainerImpl( context: Context ) : AppContainer {

    override val preferenceStore = PreferenceStoreImpl( context )
    override val playlistStore = PlaylistStoreImpl( context )
    override val settingsRepository = SettingsRepositoryImpl( preferenceStore )
    override val playlistRepository = PlaylistRepositoryImpl( playlistStore )
    override val musicServiceConnection = MusicServiceConnectionImpl.getInstance(
        context,
        ComponentName( context, MusicService::class.java )
    )

}
