package com.odesa.musically.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.odesa.musically.MusicallyApplication
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.settings.impl.SettingsRepositoryImpl
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.services.i18n.Translator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Dependency Injection container at the application level
 */
interface AppContainer {
    val settingRepository: SettingsRepository
    val translation: Translation
}

/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole
 * app
 */
class AppContainerImpl( private val application: MusicallyApplication ) : AppContainer {
    override val settingRepository: SettingsRepository
        get() = SettingsRepositoryImpl()

    private val translator: Translator = Translator( this )

    override var translation by mutableStateOf( translator.getCurrentTranslation() )

    private val applicationScope = CoroutineScope( Dispatchers.Default )

    init {
        applicationScope.launch {
            translator.onChange {
                translation = it
            }
        }
    }
}