package com.odesa.musically.ui.nowPlaying

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odesa.musically.services.media.Song
import com.odesa.musically.ui.navigation.FadeTransition

@Composable
fun NowPlayingScreenContent(
    currentlyPlayingSong: Song,
) {
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
                    modifier = Modifier.padding( 8.dp, 0.dp )
                ) {
                    Text(
                        text = target.title,
                        style = MaterialTheme.typography.headlineSmall
                            .copy( fontWeight = FontWeight.Bold ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    target.artist?.let {
//                        FlowRow {
//
//                        }
                    }
                }

            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun NowPlayingScreenPreview() {
//    NowPlayingScreenContent()
}