package com.odesa.musically.ui.nowPlaying

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MotionPhotosPaused
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.LoopMode
import com.odesa.musically.services.media.allowedSpeedPitchValues
import com.odesa.musically.ui.components.ScaffoldDialog
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import timber.log.Timber

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun NowPlayingBodyBottomBar(
    language: Language,
    currentSongIndex: Int,
    queueSize: Int,
    showLyrics: Boolean,
    currentLoopMode: LoopMode,
    shuffle: Boolean,
    pauseOnCurrentSongEnd: Boolean,
    currentSpeed: Float,
    currentPitch: Float,
    onQueueClicked: () -> Unit,
    onShowLyrics: () -> Unit,
    onToggleLoopMode: () -> Unit,
    onToggleShuffleMode: () -> Unit,
    onTogglePauseOnCurrentSongEnd: () -> Unit,
    onSpeedChange: ( Float ) -> Unit,
    onPitchChange: ( Float ) -> Unit,
    onCreateEqualizerActivityContract: () -> ActivityResultContract<Unit, Unit>,
) {

    val coroutineScope = rememberCoroutineScope()
    val equalizerActivity = rememberLauncherForActivityResult(
        contract = onCreateEqualizerActivityContract(),
        onResult = {}
    )

    var showExtraOptions by remember { mutableStateOf( false ) }
    var showSpeedDialog by remember { mutableStateOf( false ) }
    var showPitchDialog by remember { mutableStateOf( false ) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onQueueClicked
        ) {
            Icon(
                imageVector = Icons.Filled.Sort,
                contentDescription = null
            )
            Spacer( modifier = Modifier.width( 8.dp ) )
            Text(
                text = language.playingXofY(
                    ( currentSongIndex + 1 ).toString(),
                    queueSize.toString()
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer( modifier = Modifier.weight( 1f ) )
        IconButton(
            onClick = onShowLyrics
        ) {
            Icon(
                imageVector = Icons.Outlined.Article,
                contentDescription = null,
                tint = when {
                    showLyrics -> MaterialTheme.colorScheme.primary
                    else -> LocalContentColor.current
                }
            )
        }
        IconButton(
            onClick = onToggleLoopMode
        ) {
            Icon(
                imageVector = when ( currentLoopMode ) {
                    LoopMode.Song -> Icons.Filled.RepeatOne
                    else -> Icons.Filled.Repeat
                },
                contentDescription = null,
                tint = when ( currentLoopMode ) {
                    LoopMode.None -> LocalContentColor.current
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
        IconButton(
            onClick = onToggleShuffleMode
        ) {
            Icon(
                imageVector = Icons.Filled.Shuffle,
                contentDescription = null,
                tint = if ( shuffle ) MaterialTheme.colorScheme.primary else LocalContentColor.current
            )
        }
        IconButton(
            onClick = { showExtraOptions = !showExtraOptions }
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreHoriz,
                contentDescription = null
            )
        }
    }

    if ( showExtraOptions ) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                showExtraOptions = false
            }
        ) {
            Column {
                Card (
                    onClick = {
                        showExtraOptions = false
                        try {
                            equalizerActivity.launch()
                        } catch ( exception: Exception ) {
                            Timber.tag( "NOW-PLAYING-BOTTOM-BAR" ).d(
                                "Launching equalizer failed: $exception"
                            )

                        }
                    }
                ) {
                    ListItem(
                        leadingContent = {
                            Icon( imageVector = Icons.Filled.GraphicEq, contentDescription = null )
                        },
                        headlineContent = { Text( text = language.equalizer ) }
                    )
                }
                Card (
                    onClick = {
                        showExtraOptions = false
                        onTogglePauseOnCurrentSongEnd()
                    }
                ) {
                    ListItem(
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.MotionPhotosPaused,
                                contentDescription = null,
                                tint = when {
                                    pauseOnCurrentSongEnd -> MaterialTheme.colorScheme.primary
                                    else -> LocalContentColor.current
                                }
                            )
                        },
                        headlineContent = {
                            Text( text = language.pauseOnCurrentSongEnd )
                        },
                        supportingContent = {
                            Text(
                                text = if ( pauseOnCurrentSongEnd ) language.enabled
                                else language.disabled
                            )
                        }
                    )
                }
                Card (
                    onClick = {
                        showExtraOptions = false
                        showSpeedDialog = !showSpeedDialog
                    }
                ) {
                    ListItem(
                        leadingContent = {
                            Icon( imageVector = Icons.Outlined.Speed, contentDescription = null )
                        },
                        headlineContent = {
                            Text( text = language.speed )
                        },
                        supportingContent = {
                            Text( text = "x$currentSpeed" )
                        }
                    )
                }
                Card (
                    onClick = {
                        showExtraOptions = false
                        showPitchDialog = !showPitchDialog
                    }
                ) {
                    ListItem(
                        leadingContent = {
                            Icon(imageVector = Icons.Outlined.Speed, contentDescription = null)
                        },
                        headlineContent = {
                            Text(text = language.pitch)
                        },
                        supportingContent = {
                            Text(text = "x$currentPitch" )
                        }
                    )
                }
            }
        }
    }

    if ( showSpeedDialog ) {
        NowPlayingOptionDialog(
            title = language.speed,
            currentValue = currentSpeed,
            onValueChange = onSpeedChange,
            onDismissRequest = { showSpeedDialog = false }
        )
    }

    if ( showPitchDialog ) {
        NowPlayingOptionDialog(
            title = language.pitch,
            currentValue = currentPitch,
            onValueChange = onPitchChange,
            onDismissRequest = { showPitchDialog = false }
        )
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun NowPlayingOptionDialog(
    title: String,
    currentValue: Float,
    onValueChange: ( Float ) -> Unit,
    onDismissRequest: () -> Unit
) {
    ScaffoldDialog(
        title = { Text( text = title ) },
        onDismissRequest = onDismissRequest,
        content = {
            Column ( modifier = Modifier.padding( 0.dp, 8.dp ) ) {
                allowedSpeedPitchValues.map {
                    val onClick = {
                        onDismissRequest()
                        onValueChange( it )
                    }
                    Card (
                        colors = SettingsTileDefaults.cardColors(),
                        shape = MaterialTheme.shapes.small,
                        onClick = onClick
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentValue == it,
                                onClick = onClick
                            )
                            Spacer( modifier = Modifier.width( 8.dp ) )
                            Text( text = "x$it" )
                        }
                    }
                }
            }
        }
    )
}

@Preview( showSystemUi = true )
@Composable
fun NowPlayingBodyBottomBarPreview() {
    NowPlayingBodyBottomBar(
        language = English,
        currentSongIndex = 3,
        queueSize = 126,
        showLyrics = true,
        currentLoopMode = LoopMode.Song,
        shuffle = true,
        pauseOnCurrentSongEnd = false,
        currentSpeed = 2f,
        currentPitch = 2f,
        onQueueClicked = {},
        onShowLyrics = {},
        onToggleLoopMode = {},
        onToggleShuffleMode = {},
        onTogglePauseOnCurrentSongEnd = {},
        onSpeedChange = {},
        onPitchChange = {}
    ) {
        object : ActivityResultContract<Unit, Unit>() {
            override fun createIntent(
                context: Context,
                input: Unit,
            ) = Intent()

            override fun parseResult(resultCode: Int, intent: Intent?) {}
        }
    }
}