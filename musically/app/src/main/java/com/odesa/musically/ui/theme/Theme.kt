package com.odesa.musically.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.SettingsScreenUiState

const val contentScale = 1.0f

enum class ThemeMode {
    SYSTEM,
    SYSTEM_BLACK,
    LIGHT,
    DARK,
    BLACK,

}

enum class ColorSchemeMode {
    LIGHT,
    DARK,
    BLACK
}

@Composable
fun MusicallyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    uiState: SettingsScreenUiState,
    content: @Composable () -> Unit
) {

    val colorSchemeMode = uiState.themeMode.toColorSchemeMode( isSystemInDarkTheme() )
    val colorScheme = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && uiState.useMaterialYou ) {
        val context = LocalContext.current
        when ( colorSchemeMode ) {
            ColorSchemeMode.LIGHT -> dynamicLightColorScheme( context )
            ColorSchemeMode.DARK -> dynamicDarkColorScheme( context )
            ColorSchemeMode.BLACK -> ThemeColorSchemes.toBlackColorScheme(
                dynamicDarkColorScheme( context )
            )
        }
    } else {
        val primaryColor = ThemeColors.resolvePrimaryColorName( uiState.primaryColorName )
        when ( colorSchemeMode ) {
            ColorSchemeMode.LIGHT -> ThemeColorSchemes.createLightColorScheme( primaryColor )
            ColorSchemeMode.DARK -> ThemeColorSchemes.createDarkColorScheme( primaryColor )
            ColorSchemeMode.BLACK -> ThemeColorSchemes.createBlackColorScheme( primaryColor )
        }
    }

    val view = LocalView.current
    if ( !view.isInEditMode ) {
        SideEffect {
            val window = ( view.context as Activity ).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController( window, view ).isAppearanceLightStatusBars = colorSchemeMode == ColorSchemeMode.LIGHT
        }
    }

    val typography = MusicallyTypography.toTypography(
        MusicallyTypography.resolveFont( uiState.font.name ),
        TextDirection.Ltr
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    LocalDensity.current.density * contentScale,
                    LocalDensity.current.fontScale * uiState.fontScale

                )
            )
            {
                content()
            }
        }
    )
}

fun ThemeMode.resolveName( language: Language ) = when ( this ) {
    ThemeMode.SYSTEM -> language.systemLightDark
    ThemeMode.SYSTEM_BLACK -> language.systemLightBlack
    ThemeMode.LIGHT -> language.light
    ThemeMode.DARK -> language.dark
    ThemeMode.BLACK -> language.black
}

fun ThemeMode.toColorSchemeMode( isSystemInDarkTheme: Boolean ) = when ( this ) {
    ThemeMode.SYSTEM -> if ( isSystemInDarkTheme ) ColorSchemeMode.DARK else ColorSchemeMode.LIGHT
    ThemeMode.SYSTEM_BLACK -> if ( isSystemInDarkTheme ) ColorSchemeMode.BLACK else ColorSchemeMode.LIGHT
    ThemeMode.LIGHT -> ColorSchemeMode.LIGHT
    ThemeMode.DARK -> ColorSchemeMode.DARK
    ThemeMode.BLACK -> ColorSchemeMode.BLACK
}
