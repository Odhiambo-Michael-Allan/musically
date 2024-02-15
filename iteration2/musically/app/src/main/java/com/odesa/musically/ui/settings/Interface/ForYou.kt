package com.odesa.musically.ui.settings.Interface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.storage.preferences.ForYou
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.Belarusian
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
        possibleValues = ForYou.entries.associateBy( { it }, { it.resolveName( language ) } ),
        onValueChange = onForYouContentChange ,
        leadingContentImageVector = Icons.Filled.Recommend,
        headlineContentText = language.forYou,
        supportingContentText = forYouContent.joinToString { it.resolveName( language ) },
        cancel = language.cancel,
        done = language.done,
        dialogTitle = language.forYou
    )
}

fun ForYou.resolveName(language: Language) = when ( this ) {
    ForYou.Albums -> language.suggestedAlbums
    ForYou.Artists -> language.suggestedArtists
    ForYou.AlbumArtists -> language.suggestedAlbumArtists
}

@Preview( showBackground = true )
@Composable
fun ForYouPreview() {
    ForYou(
        language = Belarusian,
        forYouContent = SettingsDefaults.forYouContents,
        onForYouContentChange = {}
    )
}