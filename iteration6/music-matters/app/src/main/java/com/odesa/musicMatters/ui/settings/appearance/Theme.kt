package com.odesa.musicMatters.ui.settings.appearance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.ui.settings.components.SettingsOptionTile
import com.odesa.musicMatters.ui.theme.ThemeMode
import com.odesa.musicMatters.ui.theme.resolveName

@Composable
fun Theme(
    language: Language,
    themeMode: ThemeMode,
    onThemeChange: ( ThemeMode ) -> Unit
) {
    SettingsOptionTile(
        currentValue = themeMode,
        possibleValues = ThemeMode.entries.associateBy({ it }, { it.resolveName(language) }),
        enabled = true,
        dialogTitle = language.theme,
        onValueChange = onThemeChange,
        leadingContentIcon = Icons.Filled.Palette,
        headlineContentText = language.theme,
        supportingContentText = themeMode.resolveName(language)
    )
}

@Preview( showSystemUi = true )
@Composable
fun ThemePreview() {
    Theme(
        language = English,
        themeMode = ThemeMode.SYSTEM,
        onThemeChange = {}
    )
}