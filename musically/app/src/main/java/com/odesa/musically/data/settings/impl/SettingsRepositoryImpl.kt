package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepositoryImpl(
    private val preferences: Preferences
) : SettingsRepository {

    private val _currentLanguage = MutableStateFlow( getLanguage() )
    override val currentLanguage: StateFlow<Translation> = _currentLanguage
    override fun setLanguage( newLanguage: String ) {
        preferences.language = newLanguage
        _currentLanguage.value = resolveLanguage( newLanguage )
    }

    private fun getLanguage(): Translation {
        val languageFromSharedPreferences: String = preferences.language
        return resolveLanguage( languageFromSharedPreferences )
    }

    private fun resolveLanguage( language: String ): Translation {
        return when( language ) {
            "en" -> EnglishTranslation()
            else -> SpanishTranslation()
        }
    }
}