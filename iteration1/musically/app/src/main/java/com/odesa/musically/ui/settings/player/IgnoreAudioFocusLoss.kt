package com.odesa.musically.ui.settings.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSwitchTile

@Composable
fun IgnoreAudioFocusLoss(
    language: Language,
    ignoreAudioFocusLoss: Boolean,
    onIgnoreAudioFocusLossChange: ( Boolean ) -> Unit
) {
    SettingsSwitchTile(
        icon = {
            Icon( imageVector = Icons.Filled.CenterFocusWeak, contentDescription = null )
        },
        title = { Text( text = language.ignoreAudioFocusLoss ) },
        value = ignoreAudioFocusLoss,
        onChange = onIgnoreAudioFocusLossChange
    )
}