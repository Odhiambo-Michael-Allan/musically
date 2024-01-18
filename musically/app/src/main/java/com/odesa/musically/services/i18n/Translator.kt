package com.odesa.musically.services.i18n

import com.odesa.musically.ui.settings.language.Language

object Translator {

    val supportedLanguages = listOf(
        Language( "English", "English", EnglishTranslation.locale ),
        Language( "Беларуская", "Belarusian", BelarusianTranslation.locale ),
        Language( "简体中文", "Chinese (Simplified)", ChineseTranslation.locale ),
        Language( "Français", "French", FrenchTranslation.locale ),
        Language( "Deutsch", "German", GermanTranslation.locale ),
        Language( "Español", "Spanish", SpanishTranslation.locale ),
    )
}