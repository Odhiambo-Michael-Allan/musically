package com.odesa.musically.data.preferences.storage.impl

import android.content.Context
import androidx.core.content.edit
import com.odesa.musically.data.preferences.impl.SettingsKeys
import com.odesa.musically.data.preferences.storage.PreferenceStore
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.ui.theme.SupportedFonts

class PreferenceStoreImpl( private val context: Context) : PreferenceStore {
    override fun setLanguage( localeCode: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.language, localeCode )
        }
    }

    override fun getLanguage(): String {
        val language = getSharedPreferences().getString(
            SettingsKeys.language, null
        )
        return language ?: EnglishTranslation.locale
    }

    override fun setFontName( fontName: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.fontName, fontName )
        }
    }

    override fun getFontName(): String {
        val fontName = getSharedPreferences().getString(
            SettingsKeys.fontName, null
        )
        return fontName ?: SupportedFonts.ProductSans.fontName
    }

    private fun getSharedPreferences() = context.getSharedPreferences(
        SettingsKeys.identifier,
        Context.MODE_PRIVATE
    )
}