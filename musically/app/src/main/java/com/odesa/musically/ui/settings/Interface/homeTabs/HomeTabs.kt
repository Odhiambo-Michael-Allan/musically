package com.odesa.musically.ui.settings.Interface.homeTabs

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
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.settings.components.SettingsMultiOptionTile

@Composable
fun HomeTabs(
    language: Language,
    homeTabs: Set<HomeTab>,
    onValueChange: ( Set<HomeTab> ) -> Unit
) {

    SettingsMultiOptionTile(
        selectedValues = homeTabs,
        possibleValues = HomeTab.entries.toSet(),
        satisfies = { it.size in 2..5 },
        onValueChange = onValueChange,
        leadingContentImageVector = Icons.Filled.Home,
        headlineContentText = language.homeTabs,
        supportingContentText = homeTabs.joinToString { it.name },
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



@Preview( showBackground = true )
@Composable
fun HomeTabsPreview() {
    HomeTabs(
        language = English,
        homeTabs = SettingsDefaults.homeTabs,
        onValueChange = {}
    )
}

