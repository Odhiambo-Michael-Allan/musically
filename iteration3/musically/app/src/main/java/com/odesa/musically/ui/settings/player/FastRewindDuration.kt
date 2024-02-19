package com.odesa.musically.ui.settings.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.runtime.Composable
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSliderTile
import kotlin.math.roundToInt

@Composable
fun FastRewindDuration(
    language: Language,
    value: Float,
    onFastRewindDurationChange: ( Float ) -> Unit
) {
    SettingsSliderTile(
        value = value,
        range = 3f..60f,
        imageVector = Icons.Filled.FastRewind,
        headlineContentText = language.fastRewindDuration,
        done = language.done,
        reset = language.reset,
        calculateSliderValue = { currentValue ->
            currentValue.roundToInt().toFloat()
        },
        onValueChange = onFastRewindDurationChange,
        onReset = { onFastRewindDurationChange( SettingsDefaults.fastRewindDuration.toFloat() ) },
        label = { currentValue -> language.xSecs( currentValue.toString() ) }
    )
}