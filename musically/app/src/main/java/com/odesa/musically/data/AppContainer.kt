package com.odesa.musically.data

import android.content.Context
import com.odesa.musically.data.preferences.storage.impl.PreferenceStoreImpl
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.settings.impl.SettingsRepositoryImpl

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val settingsRepository: SettingsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl( context: Context ) : AppContainer {

    private val _settingsRepository = SettingsRepositoryImpl( PreferenceStoreImpl( context ) )

    override val settingsRepository: SettingsRepository
        get() = _settingsRepository

}
