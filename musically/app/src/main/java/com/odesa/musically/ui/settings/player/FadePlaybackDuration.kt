package com.odesa.musically.ui.settings.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsSliderTile
import kotlin.math.roundToInt

@Composable
fun FadePlaybackDuration(
    language: Language,
    value: Float,
    onFadePlaybackDurationChange: ( Float ) -> Unit
) {
    SettingsSliderTile(
        value = value,
        range = 0.5f..6f,
        imageVector = Icons.Filled.GraphicEq,
        headlineContentText = language.fadePlaybackInOut,
        done = language.done,
        reset = language.reset,
        calculateSliderValue = { currentValue ->
            currentValue.times( 2 ).roundToInt().toFloat().div( 2 )
        },
        onValueChange = onFadePlaybackDurationChange,
        onReset = { onFadePlaybackDurationChange( SettingsDefaults.fadePlaybackDuration ) },
        label = { currentValue -> language.xSecs( currentValue.toString() ) }
    )
}

@Preview( showSystemUi = true )
@Composable
fun FadePlaybackDurationPreview() {
    FadePlaybackDuration(
        language = English,
        value = SettingsDefaults.fadePlaybackDuration
    ) {}
}



