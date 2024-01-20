package com.odesa.musically.ui.settings.primaryColor

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
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
import com.odesa.musically.ui.theme.PrimaryThemeColors

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun PrimaryColor(
    primaryColor: String,
    language: Language,
    onPrimaryColorChange: ( String ) -> Unit,
    useMaterialYou: Boolean
) {

    var primaryColorDialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        enabled = !useMaterialYou,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { primaryColorDialogIsOpen = !primaryColorDialogIsOpen }
    ) {
        ListItem (
            colors = SettingsTileDefaults.listItemColors( enabled = !useMaterialYou ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.Colorize,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = language.primaryColor )
            },
            supportingContent = {
                Text( text = primaryColor )
            }
        )
    }
    if ( primaryColorDialogIsOpen ) {
        ScaffoldDialog(
            title = { Text( text = language.primaryColor ) },
            content = {
                LazyColumn {
                    items( PrimaryThemeColors.entries.toList() ) {
                        DialogOption(
                            selected = it.name == primaryColor,
                            title = it.toHumanString()
                        ) {
                            onPrimaryColorChange( it.name )
                            primaryColorDialogIsOpen = false
                        }
                    }
                }
            }
        ) {
            primaryColorDialogIsOpen = false
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun PrimaryColorPreview() {
    PrimaryColor(
        PrimaryThemeColors.Blue.name,
        language = English,
        onPrimaryColorChange = {},
        useMaterialYou = false
    )
}