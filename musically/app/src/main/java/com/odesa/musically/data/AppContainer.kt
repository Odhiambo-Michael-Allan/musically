package com.odesa.musically.data

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.settings.impl.SettingsRepositoryImpl

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val settingRepository: SettingsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl : AppContainer {

    override val settingRepository: SettingsRepository
        get() = SettingsRepositoryImpl( PreferencePlaceHolder() )

}

class PreferencePlaceHolder : Preferences {
    override var language: String = "en"

}