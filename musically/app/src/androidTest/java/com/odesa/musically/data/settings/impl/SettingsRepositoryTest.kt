package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.settings.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class FakeSettingsRepository : SettingsRepository {

    override val language: StateFlow<String> = getLanguage() as StateFlow<String>

    private fun getLanguage() = flow {
        emit( "en" )
    }

}