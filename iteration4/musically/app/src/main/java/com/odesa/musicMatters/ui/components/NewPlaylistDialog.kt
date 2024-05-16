package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun NewPlaylistDialog(
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onConfirmation: ( String ) -> Unit,
    onDismissRequest: () -> Unit,
) {

    var playlistName by remember { mutableStateOf( "" ) }
    val currentlySelectedSongs = remember { mutableListOf<Song>().toMutableStateList() }
    var showManagePlaylistSongsDialog by remember { mutableStateOf( false ) }

    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.PlaylistAdd,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = language.newPlaylist
            )
        },
        text = {
            OutlinedTextField(
                value = playlistName,
                singleLine = true,
                onValueChange = {
                    playlistName = it
                }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = playlistName.isNotEmpty(),
                onClick = {
                    onConfirmation( playlistName )
                }
            ) {
                Text(
                    text = language.done
                )
            }
        },
        dismissButton = {
            Row (
                modifier = Modifier.fillMaxWidth( 0.7f ),
                horizontalArrangement = Arrangement.Start,
            ) {
                TextButton(
                    onClick = { showManagePlaylistSongsDialog = true }
                ) {
                    Text(
                        text = "${language.addSongs} - (${currentlySelectedSongs.size})"
                    )
                }
            }
        }
    )
    if ( showManagePlaylistSongsDialog ) {
        ManagePlaylistSongsDialog(
            currentlySelectedSongs = currentlySelectedSongs,
            language = language,
            fallbackResourceId = fallbackResourceId,
            onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
            onDismissRequest = {
                currentlySelectedSongs.clear()
                currentlySelectedSongs.addAll( it )
            }
        )
    }
}

@Preview( showBackground = true )
@Composable
fun NewPlaylistDialogPreview() {
    NewPlaylistDialog(
        language = English,
        fallbackResourceId = R.drawable.placeholder_light,
        onSearchSongsMatchingQuery = { emptyList() },
        onConfirmation = {},
        onDismissRequest = {},
    )
}