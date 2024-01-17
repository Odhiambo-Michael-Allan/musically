package com.odesa.musically.ui.settings.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
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
import com.odesa.musically.ui.components.DialogOption
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.SettingDialogOption
import com.odesa.musically.ui.theme.MusicallyTheme


@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun SettingsOptionTile(
    settingIcon: @Composable () -> Unit,
    settingTitle: @Composable () -> Unit,
    valuesToBeDisplayedInSettingDialog: List<SettingDialogOption>,
    settingEnabled: Boolean = true,
    settingOptionValue: String,
    onSettingChange: ( String ) -> Unit
) {
    var dialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        enabled = settingEnabled,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { dialogIsOpen = !dialogIsOpen }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors( enabled = settingEnabled ),
            leadingContent = { settingIcon() },
            headlineContent = { settingTitle() },
            supportingContent = { Text( text = settingOptionValue ) }
        )
    }
    if ( dialogIsOpen ) {
        ScaffoldDialog(
            title = settingTitle,
            content = {
                LazyColumn {
                    items( valuesToBeDisplayedInSettingDialog ) {
                        DialogOption(
                            selected = settingOptionValue == it.value,
                            title = it.value,
                            caption = it.caption
                        ) {
                            onSettingChange( it.additionalMetadata!! )
                            dialogIsOpen = false
                        }
                    }
                }
            },
            onDismissRequest = { dialogIsOpen = false }
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsOptionTilePreview() {
    MusicallyTheme {
        SettingsOptionTile(
            settingIcon = { Icon( Icons.Filled.Language, null ) },
            settingTitle = { Text( text = "Language" ) },
            valuesToBeDisplayedInSettingDialog = listOf(
                SettingDialogOption( "English", "English", "en" ),
                SettingDialogOption( "Belarusian", "Belarusian", "be" ),
                SettingDialogOption( "Chinese", "Chinese", "zh-Hans" ),
                SettingDialogOption( "French", "French", "fr" ),
                SettingDialogOption( "Deutsch", "German", "de" ),
                SettingDialogOption( "Spanish", "Spanish", "es" )
            ),
            settingOptionValue = "English",
            onSettingChange = {},
        )
    }
}