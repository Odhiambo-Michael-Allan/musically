package com.odesa.musicMatters.ui.settings.Interface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musicMatters.data.preferences.HomeTab
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.ui.settings.components.SettingsMultiOptionTile

@Composable
fun HomeTabs(
    language: Language,
    homeTabs: Set<HomeTab>,
    onHomeTabsChange: ( Set<HomeTab> ) -> Unit
) {

    SettingsMultiOptionTile(
        selectedValues = homeTabs,
        possibleValues = HomeTab.entries.associateBy( { it }, { it.resolveName( language ) } ),
        satisfies = { it.size in 2..5 },
        onValueChange = onHomeTabsChange,
        leadingContentImageVector = Icons.Filled.Home,
        headlineContentText = language.homeTabs,
        supportingContentText = homeTabs.joinToString { it.resolveName( language ) },
        cancel = language.cancel,
        done = language.done,
        dialogTitle = language.homeTabs,
        topBar = {
            Box(
                modifier = Modifier
                    .padding( start = 24.dp, end = 24.dp, top = 16.dp )
                    .alpha( 0.7f )
            ) {
                ProvideTextStyle( value = MaterialTheme.typography.labelMedium ) {
                    Text( text = language.selectAtleast2orAtmost5Tabs )
                }
            }
        }
    )
}

fun HomeTab.resolveName(language: Language) = when ( this ) {
    HomeTab.Songs -> language.songs
    HomeTab.Albums -> language.albums
    HomeTab.Genres -> language.genres
    HomeTab.ForYou -> language.forYou
    HomeTab.Artists -> language.artists
    HomeTab.Playlists -> language.playlists
    HomeTab.Tree -> language.tree

}


@Preview( showBackground = true )
@Composable
fun HomeTabsPreview() {
    HomeTabs(
        language = Belarusian,
        homeTabs = SettingsDefaults.homeTabs,
        onHomeTabsChange = {}
    )
}