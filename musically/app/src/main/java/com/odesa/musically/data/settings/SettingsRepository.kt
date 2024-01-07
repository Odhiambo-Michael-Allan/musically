package com.odesa.musically.data.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val language: StateFlow<String>
}