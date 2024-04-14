package com.odesa.musicMatters.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun NewPlaylistDialog(
    language: Language,
    onConfirmation: ( String ) -> Unit,
    onDismissRequest: () -> Unit,
) {

    var input by remember { mutableStateOf( "" ) }

    AlertDialog(
        title = {
            Text(
                text = language.newPlaylist
            )
        },
        text = {
            TextField(
                value = input,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                placeholder = {
                    Text(
                        text = language.playlist,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Unspecified.copy(
                                alpha = 0.5f
                            )
                        )
                    )
                },
                onValueChange = {
                    input = it
                }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = input.isNotEmpty(),
                onClick = {
                    onConfirmation( input )
                }
            ) {
                Text(
                    text = language.done
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = language.cancel
                )
            }
        }
    )
}

@Preview( showBackground = true )
@Composable
fun NewPlaylistDialogPreview() {
    NewPlaylistDialog(
        language = English,
        onConfirmation = {},
        onDismissRequest = {},
    )
}