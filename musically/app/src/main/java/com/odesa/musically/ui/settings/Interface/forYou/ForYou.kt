package com.odesa.musically.ui.settings.Interface.forYou

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.preferences.storage.ForYou
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsMultiOptionTile

@Composable
fun ForYou(
    language: Language,
    forYouContent: Set<ForYou>,
    onForYouContentChange: ( Set<ForYou> ) -> Unit
) {
    SettingsMultiOptionTile(
        selectedValues = forYouContent,
        possibleValues = ForYou.entries.toSet(),
        onValueChange = onForYouContentChange ,
        leadingContentImageVector = Icons.Filled.Recommend,
        headlineContentText = language.forYou,
        supportingContentText = forYouContent.joinToString { it.name },
        cancel = language.cancel,
        done = language.done,
        dialogTitle = language.forYou
    )
}

@Preview( showBackground = true )
@Composable
fun ForYouPreview() {
    com.odesa.musically.ui.settings.Interface.forYou.ForYou(
        language = English,
        forYouContent = SettingsDefaults.forYouContents,
        onForYouContentChange = {}
    )
}