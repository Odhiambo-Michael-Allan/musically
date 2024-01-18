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
















    private val supportedLocales = mapOf(
        "en" to "English",
        "be" to "Беларуская",
        "zh" to "中文（简体",
        "fr" to "français",
        "de" to "Deutsch",
        "it" to "italiano",
        "ja" to "日本語",
        "pt" to "Português",
        "ro" to "Română",
        "ru" to "русский",
        "es" to "español",
        "tr" to "Türkçe",
        "uk" to "українська"
    )

    private val localeDisplayNames = mapOf(
        "en" to "English",
        "be" to "Belarusian",
        "zh" to "Chinese (Simplified)",
        "fr" to "French",
        "de" to "German",
        "it" to "italian",
        "ja" to "Japanese",
        "pt" to "Portuguese",
        "ro" to "Romanian",
        "ru" to "Russian",
        "es" to "Spanish",
        "tr" to "Turkish",
        "uk" to "Ukrainian"
    )

    private val translations = mapOf(
        "en" to EnglishTranslation,
        "es" to SpanishTranslation
    )

    fun parse( localeCode: String? ): Translation {
        return localeCode?.let { translations[ localeCode ] } ?: EnglishTranslation
    }


//    fun getCurrentTranslation() = language.value?.let { translations[ it ] }
//        ?: getDefaultTranslation()

//    private fun getDefaultTranslation(): Translation = translations[ getDefaultLocaleCode() ]
//        ?: EnglishTranslation()
//
//    fun getDefaultLocaleDisplayName() = getLocaleDisplayName( getDefaultLocaleCode() )
//    private fun getLocaleDisplayName( localeCode: String ) = localeDisplayNames[ localeCode ]
//    fun getDefaultLocaleNativeName() = getLocaleNativeName( getDefaultLocaleCode() )
//    private fun getLocaleNativeName( localeCode: String ) = supportedLocales[ localeCode ]
//    private fun getDefaultLocaleCode() = LocaleListCompat.getDefault()[0]?.language
//        ?.takeIf { supports( it ) }
//        ?: defaultLocaleCode
//
//    private fun supports( locale: String ) = supportedLocales.keys.contains( locale )
}