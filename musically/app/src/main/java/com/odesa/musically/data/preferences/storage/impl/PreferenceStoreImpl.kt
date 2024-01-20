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
        return language ?: SettingsDefaults.language.locale
    }

    override fun setFontName( fontName: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.fontName, fontName )
        }
    }

    override fun getFontName() = getSharedPreferences().getString(
        SettingsKeys.fontName, SettingsDefaults.font.name
    ) ?: SettingsDefaults.font.name

    override fun setFontScale( fontScale: Float ) {
        getSharedPreferences().edit {
            putFloat( SettingsKeys.fontScale, fontScale )
        }
    }

    override fun getFontScale() = getSharedPreferences().getFloat(
        SettingsKeys.fontScale, SettingsDefaults.fontScale
    )

    override fun setThemeMode( themeMode: ThemeMode ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.themeMode, themeMode.name )
        }
    }

    override fun getThemeMode() = getSharedPreferences().getString( SettingsKeys.themeMode, null )
        ?.let { ThemeMode.valueOf( it ) }
        ?: SettingsDefaults.themeMode

    override fun getUseMaterialYou() = getSharedPreferences().getBoolean(
        SettingsKeys.useMaterialYou, SettingsDefaults.useMaterialYou
    )

    override fun setUseMaterialYou( useMaterialYou: Boolean ) {
        getSharedPreferences().edit {
            putBoolean( SettingsKeys.useMaterialYou, useMaterialYou )
        }
    }

    override fun setPrimaryColorName( primaryColorName: String ) {
        getSharedPreferences().edit {
            putString( SettingsKeys.primaryColorName, primaryColorName )
        }
    }

    override fun getPrimaryColorName() = getSharedPreferences()
        .getString( SettingsKeys.primaryColorName, null ) ?: SettingsDefaults.primaryColorName

    private fun getSharedPreferences() = context.getSharedPreferences(
        SettingsKeys.identifier,
        Context.MODE_PRIVATE
    )
}

object SettingsDefaults {
    val language = English
    val font = SupportedFonts.ProductSans
    const val fontScale = 1.0f
    val themeMode = ThemeMode.SYSTEM
    const val useMaterialYou = true
    const val primaryColorName = "Blue"
}

object SettingsKeys {
    const val identifier = "musically_settings"
    const val language = "language"
    const val fontName = "font_name"
    const val fontScale = "font_scale"
    const val themeMode = "theme_mode"
    const val useMaterialYou = "use_material_you"
    const val primaryColorName = "primary_color_name"
}