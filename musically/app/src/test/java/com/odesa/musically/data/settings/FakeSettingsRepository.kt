package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSettingsRepository : SettingsRepository {

    private val _currentLanguage: MutableStateFlow<Translation> = MutableStateFlow( EnglishTranslation )
    override val currentLanguage: StateFlow<Translation>
        get() = _currentLanguage

    override fun setLanguage( newLanguage: String ) {
        _currentLanguage.value = resolveLanguage( newLanguage )
    }

    private fun resolveLanguage( language: String ): Translation {
        return when( language ) {
            "en" -> EnglishTranslation
            else -> SpanishTranslation
        }
    }


}