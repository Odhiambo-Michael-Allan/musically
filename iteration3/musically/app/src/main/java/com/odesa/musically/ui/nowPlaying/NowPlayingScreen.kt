package com.odesa.musically.ui.nowPlaying

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.toSamplingInfoString
import com.odesa.musically.ui.components.SongDropdownMenu
import com.odesa.musically.ui.navigation.FadeTransition
import com.odesa.musically.ui.testSongs

@OptIn( ExperimentalLayoutApi::class )
@Composable
fun NowPlayingScreenContent(
    currentlyPlayingSong: Song,
    language: Language,
    isFavorite: Boolean,
    controlsLayoutIsDefault: Boolean,
    onArtistClicked: ( String ) -> Unit,
    onFavorite: ( String ) -> Unit,
) {
    var showOptionsMenu by remember { mutableStateOf( false ) }
    Column {
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
                    modifier = Modifier.padding( 16.dp, 0.dp )
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
        when {
            controlsLayoutIsDefault ->
                Row(
                    modifier = Modifier.padding( 16.dp, 0.dp ),
                    horizontalArrangement = Arrangement.spacedBy( 12.dp )
                ) {
                    NowPlayingPauseButton()
                }
            else ->
                NowPlayingTraditionalControls()
        }
    }
}

@Composable
fun NowPlayingTraditionalControls() {
    Row (
        modifier = Modifier
            .padding( 16.dp, 0.dp )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

    }
}

@Composable
private fun NowPlayingPlayPauseButton(

) {

}

@Composable
private fun NowPlayingControlButton(
    style:
)

@Preview( showSystemUi = true )
@Composable
fun NowPlayingScreenPreview() {
    NowPlayingScreenContent(
        currentlyPlayingSong = testSongs.first(),
        language = English,
        isFavorite = false,
        onFavorite = {},
        onArtistClicked = {},
    )
}