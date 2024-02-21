package com.odesa.musically.ui.nowPlaying

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.odesa.musically.R
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.LoopMode
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.extensions.formatMilliseconds
import com.odesa.musically.services.media.testSongs
import com.odesa.musically.services.media.toSamplingInfoString
import com.odesa.musically.ui.components.PlaybackPosition
import com.odesa.musically.ui.components.SongDropdownMenu
import com.odesa.musically.ui.components.swipeable
import com.odesa.musically.ui.navigation.FadeTransition


@Composable
fun NowPlayingScreen(
    nowPlayingViewModel: NowPlayingViewModel,
    onCreateEqualizerActivityContract: () -> ActivityResultContract<Unit, Unit>
) {

    val uiState by nowPlayingViewModel.nowPlayingUiState.collectAsState()

    NowPlayingScreenContent(
        currentlyPlayingSong = uiState.currentlyPlayingSong,
        currentlyPlayingSongIndex = uiState.currentlyPlayingSongIndex,
        queueSize = uiState.queueSize,
        language = uiState.language,
        isFavorite = uiState.currentlyPlayingSongIsFavorite,
        controlsLayoutIsDefault = uiState.controlsLayoutIsDefault,
        isPlaying = uiState.isPlaying,
        enableSeekControls = uiState.enableSeekControls,
        showLyrics = uiState.showLyrics,
        fallbackResourceId = uiState.fallbackResourceId,
        playbackPosition = uiState.playbackPosition,
        shuffle = uiState.shuffle,
        currentLoopMode = uiState.currentLoopMode,
        pauseOnCurrentSongEnd = uiState.pauseOnCurrentSongEnd,
        currentPlayingSpeed = uiState.currentPlayingSpeed,
        currentPlayingPitch = uiState.currentPlayingPitch,
        durationFormatter = { it.formatMilliseconds() },
        onArtistClicked = {},
        onFavorite = { nowPlayingViewModel.onFavorite( it ) },
        onPausePlayButtonClick = { nowPlayingViewModel.playPause() },
        onPreviousButtonClick = { nowPlayingViewModel.playPreviousSong() },
        onNextButtonClick = { nowPlayingViewModel.playNextSong() },
        onFastRewindButtonClick = { nowPlayingViewModel.fastRewind() },
        onFastForwardButtonClick = { nowPlayingViewModel.fastForward() },
        onSeekEnd = { nowPlayingViewModel.onSeekEnd( it ) },
        onArtworkClicked = { nowPlayingViewModel.onArtworkClicked() },
        onSwipeArtworkLeft = { nowPlayingViewModel.playPreviousSong() },
        onSwipeArtworkRight = { nowPlayingViewModel.playNextSong() },
        onQueueClicked = {},
        onShowLyrics = {},
        onToggleLoopMode = { nowPlayingViewModel.toggleLoopMode() },
        onToggleShuffleMode = { nowPlayingViewModel.toggleShuffleMode() },
        onTogglePauseOnCurrentSongEnd = { nowPlayingViewModel.togglePauseOnCurrentSongEnd() },
        onPlayingSpeedChange = { nowPlayingViewModel.onPlayingSpeedChange( it ) },
        onPlayingPitchChange = { nowPlayingViewModel.onPlayingPitchChange( it ) },
        onCreateEqualizerActivityContract = onCreateEqualizerActivityContract
    )
}


@OptIn( ExperimentalLayoutApi::class )
@Composable
fun NowPlayingScreenContent(
    currentlyPlayingSong: Song,
    currentlyPlayingSongIndex: Long,
    queueSize: Int,
    language: Language,
    isFavorite: Boolean,
    controlsLayoutIsDefault: Boolean,
    isPlaying: Boolean,
    enableSeekControls: Boolean,
    showLyrics: Boolean,
    @DrawableRes fallbackResourceId: Int,
    playbackPosition: PlaybackPosition,
    shuffle: Boolean,
    currentLoopMode: LoopMode,
    pauseOnCurrentSongEnd: Boolean,
    currentPlayingSpeed: Float,
    currentPlayingPitch: Float,
    durationFormatter: (Long ) -> String,
    onArtistClicked: ( String ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onPausePlayButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    onFastRewindButtonClick: () -> Unit,
    onFastForwardButtonClick: () -> Unit,
    onSeekEnd: ( Long ) -> Unit,
    onArtworkClicked: () -> Unit,
    onSwipeArtworkLeft: () -> Unit,
    onSwipeArtworkRight: () -> Unit,
    onQueueClicked: () -> Unit,
    onShowLyrics: () -> Unit,
    onToggleLoopMode: () -> Unit,
    onToggleShuffleMode: () -> Unit,
    onTogglePauseOnCurrentSongEnd: () -> Unit,
    onPlayingSpeedChange: ( Float ) -> Unit,
    onPlayingPitchChange: ( Float ) -> Unit,
    onCreateEqualizerActivityContract: () -> ActivityResultContract<Unit, Unit>
) {
    var showOptionsMenu by remember { mutableStateOf( false ) }
    Column ( modifier = Modifier.padding( 16.dp )) {
        NowPlayingArtwork(
            showLyrics = showLyrics,
            artworkUri = currentlyPlayingSong.artworkUri,
            fallbackResourceId = fallbackResourceId,
            onSwipeLeft = onSwipeArtworkLeft,
            onSwipeRight = onSwipeArtworkRight,
            onArtworkClicked = onArtworkClicked
        )
        Row {
            AnimatedContent(
                modifier = Modifier.weight( 1f ),
                label = "now-playing-body-content",
                targetState = currentlyPlayingSong,
                transitionSpec = {
                    FadeTransition.enterTransition()
                        .togetherWith( FadeTransition.exitTransition() )
                }
            ) { target ->
                Column (
                    modifier = Modifier.padding( 16.dp, 4.dp )
                ) {
                    Text(
                        text = target.title,
                        style = MaterialTheme.typography.headlineSmall
                            .copy( fontWeight = FontWeight.Bold ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    FlowRow {
                        target.artists.forEachIndexed { index, it ->
                            Text(
                                text = it,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.pointerInput( Unit ) {
                                    detectTapGestures { _ ->
                                        onArtistClicked( it )
                                    }
                                }
                            )
                            if ( index != target.artists.size - 1 ) Text( text = ", " )
                        }
                    }
                    target.toSamplingInfoString( language )?.let {
                        val localContentColor = LocalContentColor.current
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall
                                .copy( color = localContentColor.copy( alpha = 0.7f ) ),
                            modifier = Modifier.padding( top = 4.dp )
                        )
                    }
                }
            }
            Row {
                IconButton(
                    modifier = Modifier.offset( 4.dp ),
                    onClick = { onFavorite( currentlyPlayingSong.id ) }
                ) {
                    when {
                        isFavorite -> Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        else -> Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
                IconButton(
                    onClick = {
                        showOptionsMenu = !showOptionsMenu
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null
                    )
                    SongDropdownMenu(
                        language = language,
                        song = currentlyPlayingSong,
                        isFavorite = isFavorite,
                        expanded = showOptionsMenu,
                        onFavorite = onFavorite,
                        onAddToQueue = {},
                        onPlayNext = {},
                        onViewArtist = {},
                        onViewAlbum = {},
                        onShareSong = {},
                        onDismissRequest = { showOptionsMenu = false }
                    )
                }
            }
        }
        Spacer( modifier = Modifier.height( 32.dp ) )
        NowPlayingSeekBar(
            playbackPosition = playbackPosition,
            durationFormatter = durationFormatter,
            onSeekEnd = onSeekEnd
        )
        Spacer( modifier = Modifier.height( 32.dp ) )
        when {
            controlsLayoutIsDefault ->
                NowPlayingDefaultControlsLayout(
                    isPlaying = isPlaying,
                    enableSeekControls = enableSeekControls,
                    onPausePlayButtonClick = onPausePlayButtonClick,
                    onPreviousButtonClick = onPreviousButtonClick,
                    onFastRewindButtonClick = onFastRewindButtonClick,
                    onFastForwardButtonClick = onFastForwardButtonClick,
                    onNextButtonClick = onNextButtonClick
                )
            else ->
                NowPlayingTraditionalControlsLayout(
                    enableSeekControls = enableSeekControls,
                    isPlaying = isPlaying,
                    onPreviousButtonClick = onPreviousButtonClick,
                    onFastRewindButtonClick = onFastRewindButtonClick,
                    onPausePlayButtonClick = onPausePlayButtonClick,
                    onFastForwardButtonClick = onFastForwardButtonClick,
                    onNextButtonClick = onNextButtonClick
                )
        }
        Spacer( modifier = Modifier.height( 16.dp ) )
        NowPlayingBodyBottomBar(
            language = language,
            currentSongIndex = currentlyPlayingSongIndex,
            queueSize = queueSize,
            showLyrics = showLyrics,
            currentLoopMode = currentLoopMode,
            shuffle = shuffle,
            pauseOnCurrentSongEnd = pauseOnCurrentSongEnd,
            currentSpeed = currentPlayingSpeed,
            currentPitch = currentPlayingPitch,
            onQueueClicked = onQueueClicked,
            onShowLyrics = onShowLyrics,
            onToggleLoopMode = onToggleLoopMode,
            onToggleShuffleMode = onToggleShuffleMode,
            onTogglePauseOnCurrentSongEnd = onTogglePauseOnCurrentSongEnd,
            onSpeedChange = onPlayingSpeedChange,
            onPitchChange = onPlayingPitchChange,
            onCreateEqualizerActivityContract = onCreateEqualizerActivityContract
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun NowPlayingScreenContentPreview() {
    NowPlayingScreenContent(
        currentlyPlayingSong = testSongs.first(),
        language = English,
        isFavorite = true,
        controlsLayoutIsDefault = true,
        isPlaying = true,
        enableSeekControls = true,
        showLyrics = false,
        fallbackResourceId = R.drawable.placeholder_light,
        playbackPosition = PlaybackPosition( 3, 5 ),
        currentLoopMode = LoopMode.Song,
        currentPlayingSpeed = 2f,
        currentPlayingPitch = 2f,
        currentlyPlayingSongIndex = 20,
        queueSize = 100,
        shuffle = true,
        pauseOnCurrentSongEnd = false,
        durationFormatter = { "05:33" },
        onArtistClicked = {},
        onFavorite = {},
        onPausePlayButtonClick = { /*TODO*/ },
        onPreviousButtonClick = { /*TODO*/ },
        onNextButtonClick = { /*TODO*/ },
        onFastRewindButtonClick = { /*TODO*/ },
        onFastForwardButtonClick = { /*TODO*/ },
        onSeekEnd = {},
        onArtworkClicked = {},
        onSwipeArtworkLeft = {},
        onSwipeArtworkRight = {},
        onPlayingSpeedChange = {},
        onPlayingPitchChange = {},
        onQueueClicked = {},
        onShowLyrics = {},
        onToggleLoopMode = {},
        onTogglePauseOnCurrentSongEnd = {},
        onToggleShuffleMode = {},
        onCreateEqualizerActivityContract = {
            object : ActivityResultContract<Unit, Unit>() {
                override fun createIntent(context: Context, input: Unit) = Intent()
                override fun parseResult( resultCode: Int, intent: Intent? ) {}

            }
        }
    )
}

@Composable
fun NowPlayingArtwork(
    showLyrics: Boolean,
    artworkUri: Uri?,
    @DrawableRes fallbackResourceId: Int,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onArtworkClicked: () -> Unit
) {
    Box( modifier = Modifier.padding( 16.dp, 0.dp ) ) {
        BoxWithConstraints {
            val dimension = min( maxHeight, maxWidth )

            AnimatedContent(
                modifier = Modifier.size( dimension ),
                label = "now-playing-artwork",
                targetState = artworkUri,
                transitionSpec = {
                    FadeTransition.enterTransition()
                        .togetherWith( FadeTransition.exitTransition() )
                }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder( LocalContext.current ).apply {
                        data( it )
                        placeholder( fallbackResourceId )
                        fallback( fallbackResourceId )
                        error( fallbackResourceId )
                        crossfade( true )
                    }.build(),
                    null,
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.High,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .swipeable(
                            minimumDragAmount = 100f,
                            onSwipeLeft = onSwipeLeft,
                            onSwipeRight = onSwipeRight,
                        )
                        .pointerInput(Unit) {
                            detectTapGestures { _ -> onArtworkClicked() }
                        }
                )
            }
        }
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingArtworkPreview() {
    NowPlayingArtwork(
        showLyrics = false,
        artworkUri = Uri.EMPTY,
        fallbackResourceId = R.drawable.placeholder_light,
        onSwipeLeft = {},
        onSwipeRight = {},
        onArtworkClicked = {}
    )
}

@Composable
fun NowPlayingDefaultControlsLayout(
    isPlaying: Boolean,
    enableSeekControls: Boolean,
    onPausePlayButtonClick: () -> Unit,
    onPreviousButtonClick: () -> Unit,
    onFastRewindButtonClick: () -> Unit,
    onFastForwardButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding( 16.dp, 0.dp ),
        horizontalArrangement = Arrangement.spacedBy( 12.dp )
    ) {
        NowPlayingPlayPauseButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Primary
            ),
            isPlaying = isPlaying,
            onClick = onPausePlayButtonClick
        )
        NowPlayingSkipPreviousButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            ),
            onClick = onPreviousButtonClick
        )
        if ( enableSeekControls ) {
            NowPlayingFastRewindButton(
                style = NowPlayingControlButtonStyle(
                    color = NowPlayingControlButtonColors.Transparent
                ),
                onClick = onFastRewindButtonClick
            )
            NowPlayingFastForwardButton(
                style = NowPlayingControlButtonStyle(
                    color = NowPlayingControlButtonColors.Transparent
                ),
                onClick = onFastForwardButtonClick
            )
        }
        NowPlayingSkipNextButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            ),
            onClick = onNextButtonClick
        )
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingDefaultControlsLayoutPreview() {
    Box(
        modifier = Modifier.padding( 16.dp ),
        contentAlignment = Alignment.Center
    ) {
        NowPlayingDefaultControlsLayout(
            isPlaying = true,
            enableSeekControls = true,
            onPausePlayButtonClick = {},
            onPreviousButtonClick = {},
            onFastRewindButtonClick = {},
            onFastForwardButtonClick = {}
        ) {}
    }
}

@Composable
fun NowPlayingTraditionalControlsLayout(
    enableSeekControls: Boolean,
    isPlaying: Boolean,
    onPreviousButtonClick: () -> Unit,
    onFastRewindButtonClick: () -> Unit,
    onPausePlayButtonClick: () -> Unit,
    onFastForwardButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NowPlayingSkipPreviousButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            ),
            onClick = onPreviousButtonClick
        )
        if ( enableSeekControls ) {
            NowPlayingFastRewindButton(
                style = NowPlayingControlButtonStyle(
                    color = NowPlayingControlButtonColors.Transparent
                ),
                onClick = onFastRewindButtonClick
            )
        }
        NowPlayingPlayPauseButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Surface,
                size = NowPlayingControlButtonSize.Large
            ),
            isPlaying = isPlaying,
            onClick = onPausePlayButtonClick
        )
        if ( enableSeekControls ) {
            NowPlayingFastForwardButton(
                style = NowPlayingControlButtonStyle(
                    color = NowPlayingControlButtonColors.Transparent
                ),
                onClick = onFastForwardButtonClick
            )
        }
        NowPlayingSkipNextButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            ),
            onClick = onNextButtonClick
        )
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingTraditionalControlsPreview() {
    Box(
        modifier = Modifier.padding( 16.dp ),
        contentAlignment = Alignment.Center
    ) {
        NowPlayingTraditionalControlsLayout(
            enableSeekControls = true,
            isPlaying = true,
            onPreviousButtonClick = {},
            onFastRewindButtonClick = {},
            onPausePlayButtonClick = {},
            onFastForwardButtonClick = {}
        ) {}
    }
}

@Composable
fun NowPlayingSeekBar(
    playbackPosition: PlaybackPosition,
    durationFormatter: (Long ) -> String,
    onSeekEnd: ( Long ) -> Unit
) {

    Row (
        modifier = Modifier.padding( 16.dp, 0.dp ),
        horizontalArrangement = Arrangement.spacedBy( 8.dp ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var seekRatio by remember { mutableStateOf<Float?>( null ) }

        NowPlayingPlaybackPositionText(
            durationFormatter = durationFormatter,
            duration = seekRatio?.let { it * playbackPosition.total }?.toLong()
                ?: playbackPosition.played,
            alignment = Alignment.CenterStart
        )
        Box(
            modifier = Modifier.weight( 1f )
        ) {
            NowPlayingSeekBar(
                ratio = playbackPosition.ratio,
                onSeekStart = { seekRatio = 0f },
                onSeek = { seekRatio = it },
                onSeekEnd = {
                    onSeekEnd( ( it * playbackPosition.total ).toLong() )
                    seekRatio = null
                },
                onSeekCancel = { seekRatio = null }
            )
        }
        NowPlayingPlaybackPositionText(
            durationFormatter = durationFormatter,
            duration = playbackPosition.total,
            alignment = Alignment.CenterEnd
        )
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingSeekBarPreview() {
    Box(
        modifier = Modifier.padding( 16.dp ),
        contentAlignment = Alignment.Center
    ) {
        NowPlayingSeekBar(
            playbackPosition = PlaybackPosition( 3, 5 ),
            durationFormatter = { "02:45" },
            onSeekEnd = {}
        )
    }
}

@Composable
private fun NowPlayingSeekBar(
    ratio: Float,
    onSeekStart: () -> Unit,
    onSeek: ( Float ) -> Unit,
    onSeekEnd: (Float ) -> Unit,
    onSeekCancel: () -> Unit
) {
    val sliderHeight = 12.dp
    val thumbSize = 12.dp
    val thumbSizeHalf = thumbSize.div( 2 )
    val trackHeight = 4.dp

    var dragging by remember { mutableStateOf( false ) }
    var dragRatio by remember { mutableFloatStateOf( 0f ) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(sliderHeight),
        contentAlignment = Alignment.Center
    ) {
        val sliderWidth = maxWidth

        Box(
            modifier = Modifier
                .height(sliderHeight)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val tapRatio = (offset.x / sliderWidth.toPx()).coerceIn(0f..1f)
                            onSeekEnd(tapRatio)
                        }
                    )
                }
                .pointerInput(Unit) {
                    var offsetX = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            offsetX = offset.x
                            dragging = true
                            onSeekStart()
                        },
                        onDragEnd = {
                            onSeekEnd(dragRatio)
                            offsetX = 0f
                            dragging = false
                            dragRatio = 0f
                        },
                        onDragCancel = {
                            onSeekCancel()
                            offsetX = 0f
                            dragging = false
                            dragRatio = 0f
                        },
                        onHorizontalDrag = { pointer, dragAmount ->
                            pointer.consume()
                            offsetX += dragAmount
                            dragRatio = (offsetX / sliderWidth.toPx()).coerceIn(0f..1f)
                            onSeek(dragRatio)
                        }
                    )
                }
        )
        Box(
            modifier = Modifier
                .padding(thumbSizeHalf, 0.dp)
                .height(trackHeight)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(thumbSizeHalf)
                )
        ) {
            Box(
                modifier = Modifier
                    .height(trackHeight)
                    .fillMaxWidth(if (dragging) dragRatio else ratio)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(thumbSizeHalf)
                    )
            )
        }
        Box( modifier = Modifier.fillMaxWidth() ) {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .offset(
                        sliderWidth
                            .minus(thumbSizeHalf.times(2))
                            .times(if (dragging) dragRatio else ratio),
                        0.dp
                    )
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}

@Composable
private fun NowPlayingPlaybackPositionText(
    duration: Long,
    durationFormatter: ( Long ) -> String,
    alignment: Alignment
) {
    val textStyle = MaterialTheme.typography.labelMedium
    val durationFormatted = durationFormatter( duration )

    Box( contentAlignment = alignment ) {
        Text(
            text = "0".repeat( durationFormatted.length ),
            style = textStyle.copy( color = Color.Transparent )
        )
        Text(
            text = durationFormatted,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingPlaybackPositionTextPreview() {
    NowPlayingPlaybackPositionText(
        durationFormatter = { "04:00" },
        duration = 40000,
        alignment = Alignment.Center
    )
}

@Composable
private fun NowPlayingFastForwardButton(
    style: NowPlayingControlButtonStyle,
    onClick: () -> Unit
) {
    NowPlayingControlButton(
        style = style,
        icon = Icons.Filled.FastForward
    ) {
        onClick()
    }
}

@Composable
private fun NowPlayingFastRewindButton(
    style: NowPlayingControlButtonStyle,
    onClick: () -> Unit
) {
    NowPlayingControlButton(
        style = style,
        icon = Icons.Filled.FastRewind
    ) {
        onClick()
    }
}

@Composable
private fun NowPlayingSkipNextButton(
    style: NowPlayingControlButtonStyle,
    onClick: () -> Unit
) {
    NowPlayingControlButton(
        style = style,
        icon = Icons.Filled.SkipNext
    ) {
        onClick()
    }
}

@Composable
private fun NowPlayingSkipPreviousButton(
    style: NowPlayingControlButtonStyle,
    onClick: () -> Unit
) {
    NowPlayingControlButton(
        style = style,
        icon = Icons.Filled.SkipPrevious
    ) {
        onClick()
    }
}

@Composable
private fun NowPlayingPlayPauseButton(
    style: NowPlayingControlButtonStyle,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    NowPlayingControlButton(
        style = style,
        icon = when {
            isPlaying -> Icons.Filled.Pause
            else -> Icons.Filled.PlayArrow
        }
    ) {
        onClick()
    }
}

@Preview( showBackground = true )
@Composable
fun NowPlayingControlButtonsPreview() {
    Row(
        modifier = Modifier.padding( 16.dp )
    ) {
        NowPlayingSkipPreviousButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            ),
            onClick = {}
        )
        NowPlayingFastRewindButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            )
        ) {}
        NowPlayingPlayPauseButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Surface
            ),
            isPlaying = true,
            onClick = {}
        )
        NowPlayingFastForwardButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            )
        ) {}
        NowPlayingSkipNextButton(
            style = NowPlayingControlButtonStyle(
                color = NowPlayingControlButtonColors.Transparent
            )
        ) {}
    }
}

@Composable
private fun NowPlayingControlButton(
    style: NowPlayingControlButtonStyle,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val backgroundColor = when ( style.color ) {
        NowPlayingControlButtonColors.Primary -> MaterialTheme.colorScheme.primary
        NowPlayingControlButtonColors.Surface -> MaterialTheme.colorScheme.surfaceVariant
        NowPlayingControlButtonColors.Transparent -> Color.Transparent
    }
    val contentColor = when ( style.color ) {
        NowPlayingControlButtonColors.Primary -> MaterialTheme.colorScheme.onPrimary
        else -> LocalContentColor.current
    }
    val iconSize = when ( style.size ) {
        NowPlayingControlButtonSize.Default -> 24.dp
        NowPlayingControlButtonSize.Large -> 32.dp
    }
    IconButton(
        modifier = Modifier.background( backgroundColor, RoundedCornerShape( 8.dp ) ),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size( iconSize )
        )
    }
}

private data class NowPlayingControlButtonStyle(
    val color: NowPlayingControlButtonColors,
    val size: NowPlayingControlButtonSize = NowPlayingControlButtonSize.Default
)

private enum class NowPlayingControlButtonColors {
    Primary,
    Surface,
    Transparent
}

private enum class NowPlayingControlButtonSize {
    Default,
    Large,
}
