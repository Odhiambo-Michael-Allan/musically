package com.odesa.musically.ui.settings.theme

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.components.DialogOption
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import com.odesa.musically.ui.theme.ThemeMode

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun Theme(
    language: Language,
    themeMode: ThemeMode,
    onThemeChange: ( ThemeMode ) -> Unit
) {

    var themeDialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        enabled = true,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { themeDialogIsOpen = !themeDialogIsOpen }
    ) {
        ListItem (
            colors = SettingsTileDefaults.listItemColors( enabled = true ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.Palette,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = language.theme )
            },
            supportingContent = {
                Text(
                    text = when( themeMode ) {
                        ThemeMode.SYSTEM -> language.systemLightDark
                        ThemeMode.SYSTEM_BLACK -> language.systemLightBlack
                        ThemeMode.LIGHT -> language.light
                        ThemeMode.DARK -> language.dark
                        ThemeMode.BLACK -> language.black
                    }
                )
            }
        )
    }
    if ( themeDialogIsOpen ) {
        ScaffoldDialog(
            title = { Text( text = language.theme ) },
            content = {
                LazyColumn {
                    items( ThemeMode.entries) {
                        DialogOption(
                            selected = it.name == themeMode.name,
                            title = it.name
                        ) {
                            onThemeChange( it )
                            themeDialogIsOpen = false
                        }
                    }
                }
            }
        ) {
            themeDialogIsOpen = false
        }
    }
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