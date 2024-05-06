package com.odesa.musicMatters.ui.album

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.ui.components.AlbumDropdownMenu
import com.odesa.musicMatters.ui.components.Banner
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.MinimalAppBar
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun AlbumScreen(
    albumName: String,
    albumScreenViewModel: AlbumScreenViewModel,
    onNavigateBack: () -> Unit
) {

    val uiState by albumScreenViewModel.uiState.collectAsState()

    AlbumScreenContent(
        uiState = uiState,
        albumName = albumName,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = {},
        onNavigateBack = onNavigateBack,
        playSong = { albumScreenViewModel.playMedia( it.mediaItem ) },
        onFavorite = {},
    )
}

@Composable
fun AlbumScreenContent(
    uiState: AlbumScreenUiState,
    albumName: String,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onShufflePlay: () -> Unit,
    onNavigateBack: () -> Unit,
    playSong: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
) {

    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        MinimalAppBar(
            onNavigationIconClicked = onNavigateBack,
            title = albumName
        )
        LoaderScaffold(
            isLoading = uiState.isLoadingSongsInAlbum,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                sortSongsBy = SortSongsBy.TITLE,
                language = uiState.language,
                songs = uiState.songsInAlbum,
                fallbackResourceId = fallbackResourceId,
                onShufflePlay = onShufflePlay,
                onSortTypeChange = onSortTypeChange,
                onSortReverseChange = onSortReverseChange,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                leadingContent = {
                    item {
                        AlbumArtwork(
                            album = uiState.album!!,
                            language = uiState.language,
                            fallbackResourceId = fallbackResourceId
                        )
                    }
                }
            )
        }

    }
}

@Composable
private fun AlbumArtwork(
    album: Album,
    language: Language,
    @DrawableRes fallbackResourceId: Int,
) {
    Banner(
        imageRequest = ImageRequest.Builder( LocalContext.current ).apply {
            data( album.artworkUri )
            placeholder( fallbackResourceId )
            fallback( fallbackResourceId )
            error( fallbackResourceId )
            crossfade( true )
        }.build(),
        options = { expanded, onDismissRequest ->
            AlbumDropdownMenu(
                album = album,
                language = language,
                expanded = expanded,
                onShufflePlay = { /*TODO*/ },
                onPlayNext = { /*TODO*/ },
                onAddToQueue = { /*TODO*/ },
                onViewArtist = {},
                onDismissRequest = onDismissRequest
            )
        }
    ) {
        Column {
            Text( text = album.name )
            if ( album.artists.isNotEmpty() ) {
                Text(
                    text = album.artists.joinToString(),
                    style = MaterialTheme.typography.bodyMedium
                        .copy( fontWeight = FontWeight.Bold )
                )
            }
        }
    }
}

@Preview( showBackground = true )
@Composable
fun AlbumArtworkPreview() {
    AlbumArtwork(
        album = testAlbums.first(),
        language = English,
        fallbackResourceId = R.drawable.placeholder_light
    )
}

@Preview( showSystemUi = true )
@Composable
fun AlbumScreenContentPreview() {
    AlbumScreenContent(
        uiState = testAlbumScreenUiState,
        albumName = testAlbums.first().name,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onShufflePlay = { /*TODO*/ },
        onNavigateBack = { /*TODO*/ },
        playSong = {},
        onFavorite = {}
    )
}