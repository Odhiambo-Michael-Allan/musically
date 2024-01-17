package com.odesa.musically.data.preferences.impl

import android.content.Context
import androidx.core.content.edit
import com.odesa.musically.data.preferences.Preferences
import com.odesa.musically.services.i18n.BelarusianTranslation
import com.odesa.musically.services.i18n.ChineseTranslation
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.FrenchTranslation
import com.odesa.musically.services.i18n.GermanTranslation
import com.odesa.musically.services.i18n.SpanishTranslation
import com.odesa.musically.services.i18n.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesImpl( private val context: Context ) : Preferences {

    private val _language = MutableStateFlow( getLanguage() )
    override val language = _language.asStateFlow()

    private fun getLanguage() : Translation {
        return when ( getSharedPreferences().getString( SettingsKeys.language, null ) ) {
            BelarusianTranslation.locale -> BelarusianTranslation
            ChineseTranslation.locale -> ChineseTranslation
            FrenchTranslation.locale -> FrenchTranslation
            GermanTranslation.locale -> GermanTranslation
            SpanishTranslation.locale -> SpanishTranslation
            else -> EnglishTranslation
        }
    }

    override fun setLanguage( localeCode: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.language, localeCode )
        }
        _language.update {
            getLanguage()
        }
    }

    private fun getSharedPreferences() = context.getSharedPreferences(
        SettingsKeys.identifier,
        Context.MODE_PRIVATE
    )
}

object SettingsKeys {
    const val identifier = "musically_settings"
    const val language = "language"
}