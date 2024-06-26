package com.odesa.musically.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.odesa.musically.R
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.media.Song
import com.odesa.musically.ui.navigation.FadeTransition
import com.odesa.musically.ui.navigation.TransitionDurations
import com.odesa.musically.ui.testSongs
import com.odesa.musically.utils.runFunctionIfTrueElseReturnThisObject
import kotlin.math.absoluteValue

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun NowPlayingBottomBar(
    currentlyPlayingSong: Song?,
    playbackPosition: PlaybackPosition,
    onNowPlayingBottomBarSwipeUp: () -> Unit,
    onNowPlayingBottomBarSwipeDown: () -> Unit,
    onNowPlayingBottomBarClick: () -> Unit,
    loadSongArtwork: ( Song ) -> Int,
    nextSong: () -> Boolean,
    previousSong: () -> Boolean,
    textMarquee: Boolean,
    showTrackControls: Boolean,
    showSeekControls: Boolean,
    seekBack: () -> Unit,
    seekForward: () -> Unit,
    playPause: () -> Unit,
    isPlaying: Boolean,
) {
    AnimatedContent(
        modifier = Modifier.fillMaxWidth(),
        label = "now-playing-container",
        targetState = currentlyPlayingSong,
        contentKey = { true },
        transitionSpec = {
            slideInVertically().plus( fadeIn() )
                .togetherWith( slideOutVertically().plus( fadeOut() ) )
        }
    ) { playingSong ->
        playingSong?.let {
            Column (
                modifier = Modifier
                    .background( MaterialTheme.colorScheme.surfaceColorAtElevation( 1.dp ) )
            ) {

                ElevatedCard (
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .swipeable(
                            onSwipeUp = onNowPlayingBottomBarSwipeUp,
                            onSwipeDown = onNowPlayingBottomBarSwipeDown
                        ),
                    shape = RectangleShape,
                    onClick = onNowPlayingBottomBarClick
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding( 0.dp, 8.dp )
                    ) {
                        Spacer( modifier = Modifier.width( 12.dp ) )
                        AnimatedContent(
                            label = "now-playing-card-image",
                            targetState = playingSong,
                            transitionSpec = {
                                val from = fadeIn(
                                    animationSpec = TransitionDurations.Normal.asTween(
                                        delayMillis = 150
                                    )
                                )
                                val to = fadeOut(
                                    animationSpec = TransitionDurations.Fast.asTween()
                                )
                                from togetherWith to
                            },
                        ) { song ->
                            AsyncImage(
                                model = loadSongArtwork( song ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size( 45.dp )
                                    .clip( RoundedCornerShape( 10.dp ) )
                            )
                        }
                        Spacer( modifier = Modifier.width( 15.dp ) )
                        AnimatedContent(
                            modifier = Modifier.weight( 1f ),
                            label = "now-playing-card-content",
                            targetState = playingSong,
                            transitionSpec = {
                                val from = fadeIn(
                                    animationSpec = TransitionDurations.Normal.asTween(
                                        delayMillis = 150
                                    )
                                ) + scaleIn(
                                    initialScale = 0.99f,
                                    animationSpec = TransitionDurations.Normal.asTween(
                                        delayMillis = 150
                                    )
                                )
                                val to = fadeOut(
                                    animationSpec = TransitionDurations.Fast.asTween()
                                )
                                from togetherWith to
                            }
                        ) {
                            NowPlayingBottomBarContent(
                                song = it,
                                nextSong = nextSong,
                                previousSong = previousSong,
                                textMarquee = textMarquee
                            )
                        }
                        Spacer( modifier = Modifier.width( 15.dp ) )
                        if ( showTrackControls ) {
                            IconButton(
                                onClick = { previousSong() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SkipPrevious,
                                    contentDescription = null
                                )
                            }
                        }
                        if ( showSeekControls ) {
                            IconButton(
                                onClick = seekBack
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FastRewind,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton( onClick = playPause ) {
                            Icon(
                                imageVector = when {
                                    !isPlaying -> Icons.Filled.PlayArrow
                                    else -> Icons.Filled.Pause
                                },
                                contentDescription = null
                            )
                        }
                        if ( showSeekControls ) {
                            IconButton(
                                onClick = seekForward
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FastForward,
                                    contentDescription = null
                                )
                            }
                        }
                        if ( showTrackControls ) {
                            IconButton(
                                onClick = { nextSong() }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SkipNext,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer( modifier = Modifier.width( 8.dp ) )
                    }
                }
                // ------------------------- Progress Bar ------------------------------
                Box(
                    modifier = Modifier
                        .height( 2.dp )
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background( MaterialTheme.colorScheme.primary.copy( 0.3f ) )
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                    Box(
                        modifier = Modifier
                            .align( Alignment.CenterStart )
                            .background( MaterialTheme.colorScheme.primary )
                            .fillMaxWidth( playbackPosition.ratio )
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun NowPlayingBottomBarContent(
    song: Song,
    nextSong: () -> Boolean,
    previousSong: () -> Boolean,
    textMarquee: Boolean,
) {
    BoxWithConstraints {
        val cardWidthInPixels = constraints.maxWidth
        var offsetX by remember { mutableFloatStateOf( 0f ) }
        val cardOffsetX = animateIntAsState(
            targetValue = offsetX.toInt(),
            label = "now-playing-card-offset-x"
        )
        val cardOpacity = animateFloatAsState(
            targetValue = if ( offsetX != 0f ) 0.7f else 1f,
            label = "now-playing-card-opacity"
        )

        Box(
            modifier = Modifier
                .alpha( cardOpacity.value )
                .absoluteOffset {
                    IntOffset( cardOffsetX.value.div( 2 ), 0 )
                }
                .pointerInput( Unit ) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val threshHold = cardWidthInPixels / 4
                            offsetX = when {
                                -offsetX > threshHold -> {
                                    val changed = nextSong()
                                    if ( changed ) -cardWidthInPixels.toFloat() else 0f
                                }

                                offsetX > threshHold -> {
                                    val changed = previousSong()
                                    if ( changed ) cardWidthInPixels.toFloat() else 0f
                                }

                                else -> 0f
                            }
                        },
                        onDragCancel = {
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount, ->
                            offsetX += dragAmount
                        },
                    )
                }
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                NowPlayingBottomBarContentText(
                    text = song.title,
                    style = MaterialTheme.typography.bodyMedium,
                    textMarquee = textMarquee

                )
                NowPlayingBottomBarContentText(
                    song.artists ?: "<Unknown>",
                    style = MaterialTheme.typography.bodySmall,
                    textMarquee = textMarquee
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NowPlayingBottomBarContentText(
    text: String,
    style: TextStyle,
    textMarquee: Boolean
) {
    var showOverlay by remember { mutableStateOf( false ) }
    Box {
        Text(
            text = text,
            style = style,
            maxLines = 1,
            overflow = when {
                textMarquee -> TextOverflow.Clip
                else -> TextOverflow.Ellipsis
            },
            modifier = Modifier
                .runFunctionIfTrueElseReturnThisObject<Modifier>( textMarquee ) {
                    basicMarquee( iterations = Int.MAX_VALUE )
                }
                .onGloballyPositioned {
                    val offsetX = it.boundsInParent().centerLeft.x
                    showOverlay = offsetX.absoluteValue != 0f
                }
        )
        AnimatedVisibility(
            visible = showOverlay,
            modifier = Modifier.matchParentSize(),
            enter = FadeTransition.enterTransition(),
            exit = FadeTransition.exitTransition()
        ) {
            val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation( 1.dp )
            Row {
                Box(
                    modifier = Modifier
                        .width( 12.dp )
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf( backgroundColor, Color.Transparent )
                            )
                        )
                )
                Spacer( modifier = Modifier.weight( 1f ) )
                Box (
                    modifier = Modifier
                        .width( 12.dp )
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf( Color.Transparent, backgroundColor )
                            )
                        )
                )
            }
        }
    }
}

data class PlaybackPosition(
    val played: Long,
    val total: Long,
) {
    val ratio: Float
        get() = ( played.toFloat() / total ).takeIf { it.isFinite() } ?: 0f

    companion object {
        val zero = PlaybackPosition( 0L, 0L )
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingBottomBarPreview() {
    NowPlayingBottomBar(
        currentlyPlayingSong = testSongs.first(),
        playbackPosition = PlaybackPosition( 3L, 5L ),
        onNowPlayingBottomBarSwipeUp = {},
        onNowPlayingBottomBarSwipeDown = {},
        onNowPlayingBottomBarClick = {},
        loadSongArtwork = { R.drawable.placeholder },
        nextSong = { true },
        previousSong = { true },
        textMarquee = SettingsDefaults.miniPlayerTextMarquee,
        seekBack = {},
        seekForward = {},
        showTrackControls = SettingsDefaults.miniPlayerShowTrackControls,
        showSeekControls = true,
        playPause = {},
        isPlaying = true
    )
}