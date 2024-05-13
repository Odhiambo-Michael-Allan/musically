package com.odesa.musicMatters.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.bitrate
import com.odesa.musicMatters.services.media.bitsPerSample
import com.odesa.musicMatters.services.media.codec
import com.odesa.musicMatters.services.media.dateModifiedString
import com.odesa.musicMatters.services.media.samplingRate
import com.odesa.musicMatters.services.media.sizeString
import com.odesa.musicMatters.ui.theme.MusicMattersTheme
import java.util.Date
import java.util.UUID


@Composable
fun SongDetailsDialog(
    song: Song,
    language: Language,
    durationFormatter: ( Long ) -> String,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        BoxWithConstraints {
            val dialogWidth = maxWidth * 0.75f
            val dialogHeight = maxHeight * 0.75f

            Card (
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight),
                shape = RoundedCornerShape( 16.dp )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        ,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = language.details,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    HorizontalDivider()
                    Column (
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(0.dp, 4.dp)
                    ) {
                        SongDetailsItem(
                            key = language.trackName,
                            value = song.title
                        )
                        SongDetailsItem(
                            key = language.artist,
                            value = song.artists.joinToString()
                        )
                        song.albumTitle?.let {
                            SongDetailsItem(
                                key = language.album,
                                value = it
                            )
                        }
                        song.composer?.let {
                            SongDetailsItem(
                                key = language.composer,
                                value = it
                            )
                        }
                        song.genre?.let {
                            SongDetailsItem(
                                key = language.genre,
                                value = it
                            )
                        }
                        song.year?.let {
                            SongDetailsItem(
                                key = language.year,
                                value = it.toString()
                            )
                        }
                        song.trackNumber?.let {
                            SongDetailsItem(
                                key = language.trackNumber,
                                value = ( it % 1000 ).toString()
                            )
                        }
                        SongDetailsItem(
                            key = language.duration,
                            value = durationFormatter( song.duration )
                        )
                        SongDetailsItem(
                            key = language.path,
                            value = song.path
                        )
                        SongDetailsItem(
                            key = language.size,
                            value = song.sizeString
                        )
                        SongDetailsItem(
                            key = language.dateAdded,
                            value = song.dateModifiedString
                        )
                        song.bitrate?.let {
                            SongDetailsItem(
                                key = language.bitrate,
                                value = language.xKbps( it.toString() )
                            )
                        }
                        song.bitsPerSample?.let {
                            SongDetailsItem(
                                key = language.bitDepth,
                                value = language.xBit( it.toString() )
                            )
                        }
                        song.samplingRate?.let {
                            SongDetailsItem(
                                key = language.samplingRate,
                                value = language.xKHZ( it.toString() )
                            )
                        }
                        song.codec?.let {
                            SongDetailsItem(
                                key = language.codec,
                                value = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongDetailsItem(
    key: String,
    value: String
) {
    Column (
        modifier = Modifier.padding( 0.dp, 4.dp )
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview( showSystemUi = true )
@Composable
fun SongDetailsDialogPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.FONT_SCALE,
        useMaterialYou = true
    ) {
        SongDetailsDialog(
            song = testSong,
            language = English,
            durationFormatter = { "3:44" },
            onDismissRequest = {}
        )
    }
}

val testSong = Song(
    id = UUID.randomUUID().toString(),
    mediaUri = Uri.EMPTY,
    title = "I'm In Control",
    displayTitle = "I'm In Control",
    trackNumber = 3,
    year = 2019,
    duration = 30000L,
    albumTitle = "I Remember",
    genre = "Pop",
    artists = setOf( "AlunaGeorge", "Popcaan" ),
    composer = "AlunaGeorge",
    dateModified = Date().time,
    size = 2000L,
    path = "storage/emulated/I Remember/I'm In Control",
    artworkUri = Uri.EMPTY,
    mediaItem = MediaItem.EMPTY
)