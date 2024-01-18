package com.odesa.musically.ui.app

import com.odesa.musically.MainCoroutineRule
import com.odesa.musically.data.settings.FakeSettingsRepository
import com.odesa.musically.ui.theme.SupportedFonts
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MusicallyAppViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val settingsRepository = FakeSettingsRepository()
    private lateinit var musicallyAppViewModel: MusicallyAppViewModel

    @Before
    fun setup() {
        musicallyAppViewModel = MusicallyAppViewModel( settingsRepository )
    }

    @Test
    fun whenNoFontHasBeenSet_productSansIsUsedAsTheDefaultFont() {
        val currentFont = musicallyAppViewModel.uiState.value.font
        assertEquals( SupportedFonts.ProductSans.fontName, currentFont.fontName )
    }
}