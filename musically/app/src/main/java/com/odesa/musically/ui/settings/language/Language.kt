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
import com.odesa.musically.services.i18n.EnglishTranslation
import com.odesa.musically.services.i18n.Translator
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.SettingsScreenUiState
import com.odesa.musically.ui.settings.components.DialogOption
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import com.odesa.musically.ui.theme.SupportedFonts

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun Language(
    uiState: SettingsScreenUiState,
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
                Text( text = uiState.language.language )
            },
            supportingContent = {
                Text( text = uiState.language.nativeLanguageName )
            }
        )
    }
    if ( languageDialogIsOpen ) {
        ScaffoldDialog(
            title = { Text( text = uiState.language.language ) },
            content = {
                LazyColumn {
                    items( Translator.supportedLanguages ) {
                        DialogOption(
                            selected = uiState.language.nativeLanguageName == it.nativeName,
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
        uiState = SettingsScreenUiState(
            language = EnglishTranslation,
            font = SupportedFonts.ProductSans
        ),
        onLanguageChange = {}
    )
}