package com.odesa.musically.ui.settings.language

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
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Translator
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.components.DialogOption
import com.odesa.musically.ui.settings.components.SettingsTileDefaults

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun Language(
    language: Language,
    onLanguageChange: ( String ) -> Unit
) {

    var languageDialogIsOpen by remember {
        mutableStateOf( false )
    }

    Card (
        enabled = true,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { languageDialogIsOpen = !languageDialogIsOpen }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors( enabled = true ),
            leadingContent = { 
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = language.language )
            },
            supportingContent = {
                Text( text = language.nativeName )
            }
        )
    }
    if ( languageDialogIsOpen ) {
        ScaffoldDialog(
            title = { Text( text = language.language ) },
            content = {
                LazyColumn {
                    items( Translator.supportedLanguages ) {
                        DialogOption(
                            selected = language.nativeName == it.nativeName,
                            title = it.nativeName,
                            caption = it.englishName
                        ) {
                            onLanguageChange( it.localeCode )
                            languageDialogIsOpen = false
                        }
                    }
                }
            }
        ) {
            languageDialogIsOpen = false
        }
    }
}

data class Language (
    val nativeName: String,
    val englishName: String,
    val localeCode: String
)


@Preview( showSystemUi = true )
@Composable
fun LanguagePreview() {
    Language(
        language = English
    ) {}
}