package com.odesa.musically.ui.settings.Interface.bottomBarLabelVisibility

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.preferences.storage.HomePageBottomBarLabelVisibility
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsOptionTile

@Composable
fun BottomBarLabelVisibility(
    value: HomePageBottomBarLabelVisibility,
    language: Language,
    onValueChange: ( HomePageBottomBarLabelVisibility ) -> Unit
) {
    SettingsOptionTile(
        currentValue = value,
        possibleValues = HomePageBottomBarLabelVisibility.entries.associateBy( { it }, { it.resolveName( language ) } ) ,
        enabled = true,
        dialogTitle = language.bottomBarLabelVisibility,
        onValueChange = onValueChange,
        leadingContentIcon = Icons.Filled.Label,
        headlineContentText = language.bottomBarLabelVisibility,
        supportingContentText = value.resolveName( language )
    )
}

fun HomePageBottomBarLabelVisibility.resolveName( language: Language ) = when ( this ) {
    HomePageBottomBarLabelVisibility.INVISIBLE -> language.invisible
    HomePageBottomBarLabelVisibility.VISIBLE_WHEN_ACTIVE -> language.visibleWhenActive
    HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE -> language.alwaysVisible
}

@Preview( showBackground = true )
@Composable
fun BottomBarLabelVisibilityPreview() {
    BottomBarLabelVisibility(
        value = HomePageBottomBarLabelVisibility.ALWAYS_VISIBLE,
        language = Chinese,
        onValueChange = {}
    )
}