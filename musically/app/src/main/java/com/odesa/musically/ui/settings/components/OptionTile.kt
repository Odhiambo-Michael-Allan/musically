package com.odesa.musically.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.ui.components.ScaffoldDialog
import kotlinx.coroutines.launch


@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun <T> SettingsOptionTile(
    settingIcon: @Composable () -> Unit,
    settingTitle: @Composable () -> Unit,
    settingValue: T,
    valuesToBeDisplayedInSettingDialog: Map<T, String>,
//    captions: Map<T, String>? = null,
    settingEnabled: Boolean = true,
    onSettingChange: (T ) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var dialogIsOpen by remember { mutableStateOf( false ) }

    Card (
        enabled = settingEnabled,
        colors = SettingsTileDefaults.cardColors(),
        onClick = { dialogIsOpen = !dialogIsOpen }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors(enabled = settingEnabled),
            leadingContent = { settingIcon() },
            headlineContent = { settingTitle() },
            supportingContent = { Text( text = valuesToBeDisplayedInSettingDialog[ settingValue ]!! ) }
        )
    }

    if ( dialogIsOpen ) {
        ScaffoldDialog(
            title = settingTitle,
            onDismissRequest = { dialogIsOpen = false },
            content = {
                val scrollState = rememberScrollState()
                var initialScroll by remember { mutableStateOf( false ) }

                Column(
                    modifier = Modifier
                        .padding( 0.dp, 8.dp )
                        .verticalScroll( scrollState )
                ) {
                    valuesToBeDisplayedInSettingDialog.map {
//                        val caption = captions?.get( it.key )
//                        val verticalSpace = when {
//                            caption != null -> 4.dp
//                            else -> 0.dp
//                        }
                        val selected = settingValue == it.key

                        Card(
                            colors = SettingsTileDefaults.cardColors(),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    if ( selected && !initialScroll ) {
                                        val offset = coordinates.positionInParent()
                                        coroutineScope.launch {
                                            scrollState.scrollTo( offset.y.toInt() )
                                        }
                                        initialScroll = true
                                    }
                                },
                            onClick = {
                                onSettingChange( it.key )
                                dialogIsOpen = false
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding( 12.dp ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selected,
                                    onClick = {
                                        onSettingChange( it.key )
                                        dialogIsOpen = false
                                    }
                                )
                                Spacer( modifier = Modifier.width( 8.dp ) )
                                Column {
                                    Text( it.value )
//                                    caption?.let {
//                                        Spacer( modifier = Modifier.height( 2.dp ) )
//                                        Text(
//                                            text = caption,
//                                            style = MaterialTheme.typography.labelSmall.copy(
//                                                color = LocalContentColor.current
//                                                    .copy( alpha = 0.7f )
//                                            )
//                                        )
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun SettingsOptionTilePreview() {
    SettingsOptionTile(
        settingIcon = { Icon( Icons.Filled.Language, null ) },
        settingTitle = { Text( text = "Language" ) },
        settingValue = null,
        valuesToBeDisplayedInSettingDialog = mapOf(),
        onSettingChange = {}
    )
}