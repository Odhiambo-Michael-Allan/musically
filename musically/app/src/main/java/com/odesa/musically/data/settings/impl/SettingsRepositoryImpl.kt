package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl : SettingsRepository {

    private val _language = MutableStateFlow( getLanguage() )
    override val language: StateFlow<String> = _language.asStateFlow()

    private fun getLanguage() = "en"
}