package com.odesa.musically.ui.settings.appearance.font

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsOptionTile
import com.odesa.musically.ui.theme.MusicallyFont
import com.odesa.musically.ui.theme.MusicallyTypography
import com.odesa.musically.ui.theme.SupportedFonts

@Composable
fun Font(
    font: MusicallyFont,
    language: Language,
    onFontChange: ( MusicallyFont ) -> Unit
) {
    SettingsOptionTile(
        currentValue = font,
        possibleValues = MusicallyTypography.all.values.associateBy( { it }, { it.name } ),
        enabled = true,
        dialogTitle = language.font,
        onValueChange = onFontChange,
        leadingContentIcon = Icons.Filled.TextFormat,
        headlineContentText = language.font,
        supportingContentText = font.name
    )
}


@Preview( showSystemUi = true )
@Composable
fun FontPreview() {
    Font(
        font = SupportedFonts.ProductSans,
        language = English,
        onFontChange = {}
    )
}