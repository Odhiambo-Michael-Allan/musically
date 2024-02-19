package com.odesa.musically.ui.settings.miniPlayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSwitchTile

@Composable
fun TextMarquee(
    language: Language,
    value: Boolean,
    onValueChange: ( Boolean ) -> Unit
) {
    SettingsSwitchTile(
        icon = {
            Icon(
                imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                contentDescription = null
            )
        },
        title = {
            Text( text = language.miniPlayerTextMarquee )
        },
        value = value,
        onChange = onValueChange
    )
}
