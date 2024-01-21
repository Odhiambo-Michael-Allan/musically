package com.odesa.musically.ui.settings.Interface.homeTabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.data.preferences.storage.HomeTab
import com.odesa.musically.data.preferences.storage.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import java.util.Collections

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun HomeTabs(
    language: Language,
    homeTabs: Set<HomeTab>,
    onValueChange: ( Set<HomeTab> ) -> Unit
) {

    var homeTabsDialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        colors = SettingsTileDefaults.cardColors(),
        onClick = { homeTabsDialogIsOpen = !homeTabsDialogIsOpen }
    ) {
        ListItem (
            colors = SettingsTileDefaults.listItemColors( enabled = true ),
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = language.homeTabs )
            },
            supportingContent = {
                Text( text = homeTabs.joinToString { it.name } )
            }
        )
    }
    if ( homeTabsDialogIsOpen ) {
        val selectedValues = remember { homeTabs.toMutableStateList() }
        val sortedValues by remember( selectedValues, HomeTab.entries.toSet() ) {
            derivedStateOf {
                mutableSetOf<HomeTab>().apply {
                    addAll( selectedValues )
                    addAll( HomeTab.entries.toSet() )
                }
            }
        }

        val modified by remember( selectedValues, homeTabs ) {
            derivedStateOf { selectedValues.toSet() != homeTabs }
        }

        val satisfied by remember( selectedValues ) {
            derivedStateOf { selectedValues.toSet().size in 2..5 }
        }

        ScaffoldDialog(
            title = { Text( text = language.homeTabs ) },
            topBar = {
                Box(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                        .alpha(0.7f)
                ) {
                    ProvideTextStyle( value = MaterialTheme.typography.labelMedium ) {
                        Text( text = language.selectAtleast2orAtmost5Tabs )
                    }
                }
            },
            content = {
                LazyColumn {
                    items( sortedValues.toList() ) {
                        val selected = selectedValues.contains( it )
                        val pos = sortedValues.indexOf( it )
                        CheckedDialogOption(
                            onCheckedChange = {
                                if ( selectedValues.contains( it ) )
                                    selectedValues.remove( it )
                                else
                                    selectedValues.add( it )
                            },
                            selected = selected,
                            value = it.name,
                            arrowUpEnabled = ( pos - 1 >= 0 ),
                            arrowDownEnabled = ( pos + 1 < selectedValues.size ),
                            arrowUpClicked = { Collections.swap( selectedValues, pos - 1, pos )},
                            arrowDownClicked = { Collections.swap( selectedValues, pos + 1, pos ) }
                        )
                    }
                }
            },
            actions = {
                TextButton(
                    onClick = { homeTabsDialogIsOpen = false }
                ) {
                    Text( text = language.cancel )
                }
                TextButton(
                    enabled = modified && satisfied,
                    onClick = {
                        onValueChange( selectedValues.toSet() )
                        homeTabsDialogIsOpen = false
                    }
                ) {
                    Text( text = language.done )
                }
            },
            onDismissRequest = { homeTabsDialogIsOpen = false }
        )
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun CheckedDialogOption(
    onCheckedChange: () -> Unit,
    selected: Boolean,
    value: String,
    arrowUpEnabled: Boolean,
    arrowDownEnabled: Boolean,
    arrowUpClicked: () -> Unit,
    arrowDownClicked: () -> Unit
) {
    Card (
        colors = SettingsTileDefaults.cardColors(),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        onClick = onCheckedChange
    ) {
        Row (
            modifier = Modifier.padding( 12.dp, 0.dp ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier.weight( 1f ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onCheckedChange() }
                )
                Spacer( modifier = Modifier.width( 8.dp ) )
                Text( text = value )
            }
            if ( selected ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        enabled = arrowUpEnabled,
                        onClick = arrowUpClicked
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        enabled = arrowDownEnabled,
                        onClick = arrowDownClicked
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null
                        )
                    }
                }
            }
        }

    }
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

@Preview( showBackground = true )
@Composable
fun CheckedDialogOptionPreview() {
    CheckedDialogOption(
        onCheckedChange = { /*TODO*/ },
        selected = true,
        value = SettingsDefaults.homeTabs.first().name,
        arrowUpEnabled = true,
        arrowDownEnabled = true,
        arrowUpClicked = { /*TODO*/ },
        arrowDownClicked = {}
    )
}