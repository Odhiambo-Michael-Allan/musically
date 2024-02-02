package com.odesa.musically.ui.songs

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.songs.FakeSongsRepository
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SongsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var songsRepository: SongsRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var songsViewModel: SongsViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        songsRepository = FakeSongsRepository()
        songsViewModel = SongsViewModel(
            settingsRepository = settingsRepository,
            songsRepository = songsRepository,
            mediaPermissionGranted = true
        )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", songsViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = songsViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testSongsAreCorrectlyFetched() = runTest {
        assertFalse( songsViewModel.uiState.value.isLoadingSongs )
        assertEquals( 100, songsViewModel.uiState.value.songs.size )
    }

    @Test
    fun testSortSongsInReverseChange() = runTest {
        assertFalse( songsViewModel.uiState.value.sortSongsInReverse )
        songsRepository.setSortSongsInReverse( true )
        assertTrue( songsViewModel.uiState.value.sortSongsInReverse )
        songsRepository.setSortSongsInReverse( false )
        assertFalse( songsViewModel.uiState.value.sortSongsInReverse )
    }

    @Test
    fun testSortSongsByChange() = runTest {
        assertEquals( SettingsDefaults.sortSongsBy, songsViewModel.uiState.value.sortSongsBy )
        SortSongsBy.entries.forEach {
            songsRepository.setSortSongsBy( it )
            assertEquals( it, songsViewModel.uiState.value.sortSongsBy )
        }
    }


}