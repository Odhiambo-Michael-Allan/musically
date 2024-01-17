package com.odesa.musically.services.i18n

import com.odesa.musically.ui.settings.SettingOption

object Translator {

    val supportedLanguages = listOf(
        SettingOption( "English", "English" ),
        SettingOption( "Беларуская", "Belarusian" ),
        SettingOption( "中文（简体", "Chinese (Simplified)" ),
        SettingOption( "français", "French" ),
        SettingOption( "Deutsch", "German" ),
        SettingOption( "italiano", "Italian" ),
        SettingOption( "日本語", "Japanese" ),
        SettingOption( "Português", "Portuguese" ),
        SettingOption( "Română", "Romanian" ),
        SettingOption( "русский", "Russian" ),
        SettingOption( "español", "Spanish" ),
        SettingOption( "Türkçe", "Turkish" ),
        SettingOption( "українська", "Ukrainian" )
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