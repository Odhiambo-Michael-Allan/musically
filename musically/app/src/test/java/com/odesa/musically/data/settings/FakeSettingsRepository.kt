package com.odesa.musically.data.settings

import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import com.odesa.musically.ui.theme.MusicallyFont
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSettingsRepository : SettingsRepository {

    private val _currentLanguage: MutableStateFlow<Translation> = MutableStateFlow( EnglishTranslation )
    override val language: StateFlow<Translation>
        get() = _currentLanguage
    override val font: StateFlow<MusicallyFont>
        get() = TODO("Not yet implemented")

    override fun setLanguage( localeCode: String ) {
        _currentLanguage.value = resolveLanguage( localeCode )
    }

    override fun setFont(fontName: String) {
        TODO("Not yet implemented")
    }

    private fun resolveLanguage( language: String ): Translation {
        return when( language ) {
            BelarusianTranslation.locale -> BelarusianTranslation
            ChineseTranslation.locale -> ChineseTranslation
            FrenchTranslation.locale -> FrenchTranslation
            GermanTranslation.locale -> GermanTranslation
            SpanishTranslation.locale -> SpanishTranslation
            else -> EnglishTranslation
        }
    }


}