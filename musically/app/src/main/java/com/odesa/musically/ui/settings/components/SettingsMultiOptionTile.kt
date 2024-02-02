package com.odesa.musically.ui.settings.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.ui.components.ScaffoldDialog
import java.util.Collections

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun <T> SettingsMultiOptionTile(
    topBar: ( @Composable () -> Unit )? = null,
    selectedValues: Set<T>,
    possibleValues: Map<T, String>,
    satisfies: ( Set<T> ) -> Boolean = { true },
    onValueChange: ( Set<T> ) -> Unit,
    leadingContentImageVector: ImageVector,
    headlineContentText: String,
    supportingContentText: String,
    cancel: String,
    done: String,
    dialogTitle: String
) {
    var dialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        colors = SettingsTileDefaults.cardColors(),
        onClick = { dialogIsOpen = !dialogIsOpen }
    ) {
        ListItem (
            colors = SettingsTileDefaults.listItemColors( enabled = true ),
            leadingContent = {
                Icon(
                    imageVector = leadingContentImageVector,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text( text = headlineContentText )
            },
            supportingContent = {
                Text( text = supportingContentText )
            }
        )
    }
    if ( dialogIsOpen ) {
        val currentlySelectedValues = remember { selectedValues.toMutableStateList() }
        val sortedValues by remember( currentlySelectedValues, possibleValues ) {
            derivedStateOf {
                mutableSetOf<T>().apply {
                    addAll( currentlySelectedValues )
                    addAll( possibleValues.keys.toSet() )
                }
            }
        }
        val modified by remember( currentlySelectedValues, selectedValues ) {
            derivedStateOf { currentlySelectedValues.toSet() != selectedValues }
        }
        val satisfied by remember( currentlySelectedValues ) {
            derivedStateOf { satisfies( currentlySelectedValues.toSet() ) }
        }

        ScaffoldDialog(
            title = { Text( text = dialogTitle ) },
            topBar = topBar,
            content = {
                LazyColumn {
                    items( sortedValues.toList() ) {
                        val selected = currentlySelectedValues.contains( it )
                        val pos = sortedValues.indexOf( it )
                        CheckedDialogOption(
                            onCheckedChange = {
                                if ( currentlySelectedValues.contains( it ) )
                                    currentlySelectedValues.remove( it )
                                else
                                    currentlySelectedValues.add( it )
                            },
                            selected = selected,
                            value = possibleValues[it]!!,
                            arrowUpEnabled = ( pos - 1 >= 0 ),
                            arrowDownEnabled = ( pos + 1 < currentlySelectedValues.size ),
                            arrowUpClicked = { Collections.swap( currentlySelectedValues, pos - 1, pos )},
                            arrowDownClicked = { Collections.swap( currentlySelectedValues, pos + 1, pos ) }
                        )
                    }
                }
            },
            actions = {
                TextButton(
                    onClick = { dialogIsOpen = false }
                ) {
                    Text( text = cancel )
                }
                TextButton(
                    enabled = modified && satisfied,
                    onClick = {
                        onValueChange( currentlySelectedValues.toSet() )
                        dialogIsOpen = false
                    }
                ) {
                    Text( text = done )
                }
            },
            onDismissRequest = { dialogIsOpen = false }
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