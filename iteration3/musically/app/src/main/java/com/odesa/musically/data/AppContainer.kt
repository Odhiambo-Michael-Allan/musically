package com.odesa.musically.data

import android.content.ComponentName
import android.content.Context
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.settings.impl.SettingsRepositoryImpl
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.data.storage.preferences.impl.PreferenceStoreImpl
import com.odesa.musically.services.media.MusicService
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.MusicServiceConnectionImpl

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val preferenceStore: PreferenceStore
    val settingsRepository: SettingsRepository
    val musicServiceConnection: MusicServiceConnection
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl( context: Context ) : AppContainer {

    private val _preferenceStore = PreferenceStoreImpl( context )
    private val _settingsRepository = SettingsRepositoryImpl( _preferenceStore )
    override val preferenceStore = _preferenceStore
    override val settingsRepository = _settingsRepository

    private val _musicServiceConnection = MusicServiceConnectionImpl.getInstance(
        context,
        ComponentName( context, MusicService::class.java )
    )
    override val musicServiceConnection = _musicServiceConnection

}
