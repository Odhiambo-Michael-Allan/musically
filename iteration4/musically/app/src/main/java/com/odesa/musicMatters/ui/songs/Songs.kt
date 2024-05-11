package com.odesa.musicMatters.ui.songs

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.components.LoaderScaffold
import com.odesa.musicMatters.ui.components.SongList
import com.odesa.musicMatters.ui.components.TopAppBar
import com.odesa.musicMatters.ui.theme.MusicMattersTheme
import com.odesa.musicMatters.ui.theme.ThemeMode
import com.odesa.musicMatters.ui.theme.isLight

@Composable
fun SongsScreen(
    songsScreenViewModel: SongsScreenViewModel,
    onViewAlbum: (String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onSettingsClicked: () -> Unit
) {
    val songsScreenUiState by songsScreenViewModel.uiState.collectAsState()
    val context = LocalContext.current

    SongsScreenContent(
        uiState = songsScreenUiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onSettingsClicked = onSettingsClicked,
        onShufflePlay = {},
        playSong = {
            songsScreenViewModel.playMedia( it.mediaItem )
        },
        onFavorite = { songsScreenViewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onShareSong = {
            try {
                val intent = Intent( Intent.ACTION_SEND ).apply {
                    addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    putExtra( Intent.EXTRA_STREAM, it )
                    type = context.contentResolver.getType( it )
                }
                context.startActivity( intent )
            }
            catch ( exception: Exception ) {
                Toast.makeText(
                    context,
                    uiState.language.shareFailedX( exception.localizedMessage ?: exception.toString() ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}

@Composable
fun SongsScreenContent(
    uiState: SongsScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: (SortSongsBy) -> Unit,
    onSettingsClicked: () -> Unit,
    onShufflePlay: () -> Unit,
    playSong: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
) {
    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = { /*TODO*/ },
            title = uiState.language.songs,
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = uiState.isLoading,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = true,
                onSortReverseChange = onSortReverseChange,
                sortSongsBy = SortSongsBy.TITLE,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songs,
                onShufflePlay = onShufflePlay,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong
            )
        }
    }
}


@Preview( showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_MASK )
@Composable
fun SongsScreenContentPreview() {
    MusicMattersTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.PRIMARY_COLOR_NAME,
        fontName = SettingsDefaults.font.name,
        fontScale = 1.75f,
        useMaterialYou = SettingsDefaults.USE_MATERIAL_YOU
    ) {
        SongsScreenContent(
            uiState = uiState,
            onSortReverseChange = {},
            onSortTypeChange = {},
            onSettingsClicked = {},
            onShufflePlay = {},
            playSong = {},
            onFavorite = {},
            onViewAlbum = {},
            onViewArtist = {},
            onShareSong = {},
        )
    }
}


val uiState = SongsScreenUiState(
    language = English,
    songs = testSongs,
    themeMode = ThemeMode.LIGHT,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = testSongs.map { it.id },
    isLoading = true
)