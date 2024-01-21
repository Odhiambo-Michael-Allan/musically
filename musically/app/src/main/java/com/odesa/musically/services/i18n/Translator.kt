package com.odesa.musically.services.i18n

import com.odesa.musically.ui.settings.appearance.language.Language

object Translator {

    val supportedLanguages = listOf(
        Language( "English", "English", English.locale ),
        Language( "Беларуская", "Belarusian", Belarusian.locale ),
        Language( "简体中文", "Chinese (Simplified)", Chinese.locale ),
        Language( "Français", "French", French.locale ),
        Language( "Deutsch", "German", German.locale ),
        Language( "Español", "Spanish", Spanish.locale ),
    )
}