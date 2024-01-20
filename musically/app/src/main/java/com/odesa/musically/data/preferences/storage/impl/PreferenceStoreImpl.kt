package com.odesa.musically.data.preferences.storage.impl

import android.content.Context
import androidx.core.content.edit
import com.odesa.musically.data.preferences.storage.PreferenceStore
import com.odesa.musically.services.i18n.English
import com.odesa.musically.ui.theme.SupportedFonts
import com.odesa.musically.ui.theme.ThemeMode

class PreferenceStoreImpl( private val context: Context ) : PreferenceStore {

    override fun setLanguage( localeCode: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.language, localeCode )
        }
    }

    override fun getLanguage(): String {
        val language = getSharedPreferences().getString(
            SettingsKeys.language, null
        )
        return language ?: SettingsDefaults.translation.locale
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
        return fontName ?: SettingsDefaults.font.name
    }

    override fun setFontScale( fontScale: Float ) {
        getSharedPreferences().edit {
            putFloat( SettingsKeys.fontScale, fontScale )
        }
    }

    override fun getFontScale(): Float {
        return getSharedPreferences().getFloat(
            SettingsKeys.fontScale, SettingsDefaults.fontScale
        )
    }

    override fun setThemeMode( themeMode: ThemeMode ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.themeMode, themeMode.name )
        }
    }

    override fun getThemeMode() = getSharedPreferences().getString( SettingsKeys.themeMode, null )
        ?.let { ThemeMode.valueOf( it ) }
        ?: SettingsDefaults.themeMode

    private fun getSharedPreferences() = context.getSharedPreferences(
        SettingsKeys.identifier,
        Context.MODE_PRIVATE
    )
}

object SettingsDefaults {
    val translation = English
    val font = SupportedFonts.ProductSans
    const val fontScale = 1.0f
    val themeMode = ThemeMode.SYSTEM
}

object SettingsKeys {
    const val identifier = "musically_settings"
    const val language = "language"
    const val fontName = "font_name"
    const val fontScale = "font_scale"
    const val themeMode = "theme_mode"
}