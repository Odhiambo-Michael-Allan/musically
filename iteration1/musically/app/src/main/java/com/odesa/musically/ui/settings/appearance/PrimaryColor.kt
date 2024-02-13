package com.odesa.musically.ui.settings.appearance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsOptionTile
import com.odesa.musically.ui.theme.PrimaryThemeColors

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun PrimaryColor(
    primaryColor: String,
    language: Language,
    onPrimaryColorChange: ( String ) -> Unit,
    useMaterialYou: Boolean
) {
    SettingsOptionTile(
        currentValue = primaryColor,
        possibleValues = PrimaryThemeColors.entries.toSet().associateBy( { it.name }, { it.name } ),
        enabled = !useMaterialYou,
        dialogTitle = language.primaryColor,
        onValueChange = onPrimaryColorChange,
        leadingContentIcon = Icons.Filled.Colorize,
        headlineContentText = language.primaryColor,
        supportingContentText = primaryColor
    )
}

@Preview( showSystemUi = true )
@Composable
fun PrimaryColorPreview() {
    PrimaryColor(
        PrimaryThemeColors.Blue.name,
        language = English,
        onPrimaryColorChange = {},
        useMaterialYou = true
    )
}