package com.odesa.musically.ui.settings.nowPlaying

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.preferences.storage.NowPlayingControlsLayout
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsOptionTile

@Composable
fun ControlsLayout(
    nowPlayingControlsLayout: NowPlayingControlsLayout,
    language: Language,
    onNowPlayingControlsLayoutChange: ( NowPlayingControlsLayout ) -> Unit
) {
    SettingsOptionTile(
        currentValue = nowPlayingControlsLayout,
        possibleValues = NowPlayingControlsLayout.entries.toList().associateBy({ it }, { it.name }),
        enabled = true,
        dialogTitle = language.controlsLayout,
        onValueChange = onNowPlayingControlsLayoutChange,
        leadingContentIcon = Icons.Filled.Dashboard,
        headlineContentText = language.controlsLayout,
        supportingContentText = nowPlayingControlsLayout.name
    )
}

@Preview( showBackground = true )
@Composable
fun ControlsLayoutPreview() {
    ControlsLayout(
        nowPlayingControlsLayout = SettingsDefaults.nowPlayingControlsLayout,
        language = English,
        onNowPlayingControlsLayoutChange = {}
    )
}