package com.odesa.musically.data.preferences

import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.StateFlow

interface Preferences {
    val language: StateFlow<Translation>
    fun setLanguage( localeCode: String )
}