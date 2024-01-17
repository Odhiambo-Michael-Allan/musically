package com.odesa.musically.data.preferences

import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FakePreferences : Preferences {

    private var _language = MutableStateFlow( getLanguage() )
    override val language: StateFlow<Translation>
        get() = _language

    override fun setLanguage( localeCode: String ) {
        when ( localeCode ) {
            BelarusianTranslation.locale -> _language.value = BelarusianTranslation
            ChineseTranslation.locale -> _language.value = ChineseTranslation
            FrenchTranslation.locale -> _language.value = FrenchTranslation
            GermanTranslation.locale -> _language.value = GermanTranslation
            SpanishTranslation.locale -> _language.value = SpanishTranslation
            else -> EnglishTranslation
        }
    }

    private fun getLanguage() : Translation {
        return EnglishTranslation
    }

}