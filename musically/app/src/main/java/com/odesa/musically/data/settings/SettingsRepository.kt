package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val currentLanguage: StateFlow<Translation>

    fun setLanguage( newLanguage: String )
}