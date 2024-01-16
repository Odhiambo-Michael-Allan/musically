package com.odesa.musically.ui.settings

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SettingsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val settingsRepository = FakeSettingsRepository()
    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsViewModel = SettingsViewModel( settingsRepository )
    }


    @Test
    fun whenNoLanguageHasBeenSet_theDefaultIsEnglish() {
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( "Settings", currentLanguage.settings )
    }

    @Test
    fun whenLanguageChangesToSpanish_spanishLanguageIsUsed() {
        settingsRepository.setLanguage( "es" )
        val currentLanguage = settingsViewModel.uiState.value.language
        assertEquals( "Configuración", currentLanguage.settings )
    }
}