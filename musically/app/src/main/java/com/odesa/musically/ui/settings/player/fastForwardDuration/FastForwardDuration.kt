package com.odesa.musically.ui.settings.player.fastForwardDuration

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.runtime.Composable
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSliderTile
import kotlin.math.roundToInt

@Composable
fun FastForwardDuration(
    language: Language,
    value: Float,
    onFastForwardDurationChange: ( Float ) -> Unit
) {
    SettingsSliderTile(
        value = value,
        range = 3f..60f,
        imageVector = Icons.Filled.FastForward,
        headlineContentText = language.fastForwardDuration,
        done = language.done,
        reset = language.reset,
        calculateSliderValue = { currentValue ->
            currentValue.roundToInt().toFloat()
        },
        onValueChange = onFastForwardDurationChange,
        onReset = { onFastForwardDurationChange( SettingsDefaults.fastForwardDuration.toFloat() ) },
        label = { currentValue -> language.xSecs( currentValue.toString() ) }
    )
}