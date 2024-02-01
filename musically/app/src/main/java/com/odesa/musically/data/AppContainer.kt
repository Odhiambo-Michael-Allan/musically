package com.odesa.musically.data

import android.content.Context
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.settings.impl.SettingsRepositoryImpl
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.songs.impl.SongsRepositoryImpl
import com.odesa.musically.data.storage.database.MusicDatabase
import com.odesa.musically.data.storage.database.impl.MusicDatabaseImpl
import com.odesa.musically.data.storage.preferences.PreferenceStore
import com.odesa.musically.data.storage.preferences.impl.PreferenceStoreImpl

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val preferenceStore: PreferenceStore
    val musicDatabase: MusicDatabase
    val settingsRepository: SettingsRepository
    val songsRepository: SongsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl( context: Context ) : AppContainer {

    private val _preferenceStore = PreferenceStoreImpl( context )
    private val _musicDatabase = MusicDatabaseImpl( context )
    private val _settingsRepository = SettingsRepositoryImpl( _preferenceStore )
    private val _songsRepository = SongsRepositoryImpl( _preferenceStore, _musicDatabase )
    override val preferenceStore = _preferenceStore
    override val musicDatabase = _musicDatabase
    override val settingsRepository = _settingsRepository
    override val songsRepository = _songsRepository

}
