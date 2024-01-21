package com.odesa.musically.ui.settings.appearance.font

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFormat
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
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.SettingsScreenUiState
import com.odesa.musically.ui.settings.components.DialogOption
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import com.odesa.musically.ui.theme.MusicallyTypography

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun Font(
    uiState: SettingsScreenUiState,
    onFontChange: ( String ) -> Unit
) {

    var fontDialogIsOpen by remember {
        mutableStateOf( false )
    }

    Card (
        enabled = true,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { fontDialogIsOpen = !fontDialogIsOpen }
    ) {
        ListItem (
            colors = SettingsTileDefaults.listItemColors( enabled = true ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.TextFormat,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = uiState.language.font )
            },
            supportingContent = {
                Text( text = uiState.font.name )
            }
        )
    }
    if ( fontDialogIsOpen ) {
        ScaffoldDialog(
            title = { Text( text = uiState.language.font ) },
            content = {
                LazyColumn {
                    items( MusicallyTypography.all.values.toList() ) {
                        DialogOption(
                            selected = uiState.font.name == it.name,
                            title = it.name
                        ) {
                            onFontChange( it.name )
                            fontDialogIsOpen = false
                        }
                    }
                }
            }
        ) {
            fontDialogIsOpen = false
        }
    }

}


//@Preview( showSystemUi = true )
//@Composable
//fun FontPreview() {
//    Font(
//        uiState = SettingsScreenUiState(
//            language = EnglishTranslation,
//            font = SupportedFonts.ProductSans
//        ),
//        onFontChange = {}
//    )
//}