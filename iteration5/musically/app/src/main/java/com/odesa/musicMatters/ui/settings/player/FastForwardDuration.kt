package com.odesa.musicMatters.ui.settings.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.runtime.Composable
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.ui.settings.components.SettingsSliderTile
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