package com.odesa.musicMatters.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.ui.theme.MusicMattersTheme

@Composable
fun AlbumRow(
    albums: List<Album>,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
    onPlaySongsInAlbum: ( Album ) -> Unit,
    onAddToQueue: () -> Unit,
    onPlayNext: () -> Unit,
    onShufflePlay: () -> Unit,
    onViewAlbum: ( Album ) -> Unit,
) {
    BoxWithConstraints {
        val maxSize = min( maxHeight, maxWidth ).div( 2f )
        val width = min( maxSize, 200.dp )

        LazyRow {
            items( albums ) {
                Box(
                    modifier = Modifier.width( width.minus( 15.dp ) )
                ) {
                    AlbumTile(
                        modifier = Modifier.width( width ),
                        album = it,
                        language = language,
                        fallbackResourceId = fallbackResourceId,
                        onPlayAlbum = { onPlaySongsInAlbum( it ) },
                        onAddToQueue = onAddToQueue,
                        onPlayNext = onPlayNext,
                        onShufflePlay = onShufflePlay,
                        onViewArtist = {},
                        onClick = { onViewAlbum( it ) }
                    )
                }
            }
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun AlbumRowPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.primaryColorName,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.fontScale,
        useMaterialYou = SettingsDefaults.useMaterialYou
    ) {
        AlbumRow(
            albums = testAlbums,
            language = English,
            fallbackResourceId = R.drawable.placeholder_light,
            onPlaySongsInAlbum = {},
            onAddToQueue = { /*TODO*/ },
            onPlayNext = { /*TODO*/ },
            onShufflePlay = { /*TODO*/ },
            onViewAlbum = {}
        )
    }
}