package com.odesa.musically.data.settings.impl

import com.odesa.musically.data.preferences.FakePreferences
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SettingsRepositoryImplTest {

    private val preferences: FakePreferences = FakePreferences()
    private val settingsRepository = SettingsRepositoryImpl( preferences )

    @Test
    fun whenLanguageIsEnglish_englishLanguageIsUsed() {
        assertEquals( "Settings", settingsRepository.currentLanguage.value.settings )
    }

    @Test
    fun whenLanguageIsSpanish_spanishLanguageIsUsed() {
        settingsRepository.setLanguage( "es" )
        assertEquals( "Configuración", settingsRepository.currentLanguage.value.settings )
        assertEquals( "es", preferences.language )
    }

}