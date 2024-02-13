package com.odesa.musically.ui.settings.appearance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Translator
import com.odesa.musically.ui.settings.components.SettingsOptionTile

@Composable
fun Language(
    language: Language,
    onLanguageChange: ( Language ) -> Unit
) {
    SettingsOptionTile(
        currentValue = language,
        possibleValues = Translator.supportedLanguages.associateBy( { it }, { it.nativeName } ),
        captions = Translator.supportedLanguages.associateBy( { it }, { it.englishName } ),
        enabled = true,
        dialogTitle = language.language,
        onValueChange = onLanguageChange,
        leadingContentIcon = Icons.Filled.Language,
        headlineContentText = language.language,
        supportingContentText = language.nativeName
    )
}



@Preview( showSystemUi = true )
@Composable
fun LanguagePreview() {
    Language(
        language = Belarusian
    ) {}
}