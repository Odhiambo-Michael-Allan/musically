package com.odesa.musically.ui.settings.appearance.materialYou

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSwitchTile

@Composable
fun MaterialYou(
    language: Language,
    useMaterialYou: Boolean,
    onUseMaterialYouChange: ( Boolean ) -> Unit
) {
    SettingsSwitchTile(
        icon = {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = null
            )
        },
        title = {
            Text( text = language.materialYou )
        },
        value = useMaterialYou,
        onChange = onUseMaterialYouChange
    )
}

@Preview( showBackground = true )
@Composable
fun MaterialYouPreview() {
    MaterialYou(
        language = English,
        useMaterialYou = true,
        onUseMaterialYouChange = {}
    )
}