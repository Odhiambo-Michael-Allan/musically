package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.StateFlow

class SettingsRepositoryImpl(
    private val preferences: Preferences
) : SettingsRepository {

    override val currentLanguage: StateFlow<Translation> = preferences.language
    override fun setLanguage( localeCode: String ) {
        preferences.setLanguage( localeCode )
    }
}