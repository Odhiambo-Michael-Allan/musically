package com.odesa.musically.ui.settings.miniPlayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSwitchTile

@Composable
fun ShowSeekControls(
    language: Language,
    value: Boolean, 
    onValueChange: ( Boolean ) -> Unit
) {
    SettingsSwitchTile(
        icon = { Icon( imageVector = Icons.Filled.Forward30, contentDescription = null ) },
        title = { Text( text = language.showSeekControls ) },
        value = value,
        onChange = onValueChange
    )
}