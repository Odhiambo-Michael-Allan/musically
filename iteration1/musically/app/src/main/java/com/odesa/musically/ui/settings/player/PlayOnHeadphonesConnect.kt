package com.odesa.musically.ui.settings.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSwitchTile

@Composable
fun PlayOnHeadphonesConnect(
    language: Language,
    playOnHeadphonesConnect: Boolean,
    onPlayOnHeadphonesConnectChange: ( Boolean ) -> Unit
) {
    SettingsSwitchTile(
        icon = { Icon( imageVector = Icons.Filled.Headphones, contentDescription = null ) },
        title = { Text( text = language.playOnHeadphonesConnect ) },
        value = playOnHeadphonesConnect,
        onChange = onPlayOnHeadphonesConnectChange
    )
}