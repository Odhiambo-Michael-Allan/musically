package com.odesa.musically.ui.songs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.R
import com.odesa.musically.data.storage.preferences.SortSongsBy
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.library.MUSICALLY_TRACKS_ROOT
import com.odesa.musically.ui.components.LoaderScaffold
import com.odesa.musically.ui.components.SongList
import com.odesa.musically.ui.components.TopAppBar
import com.odesa.musically.ui.testSongs
import com.odesa.musically.ui.theme.MusicallyTheme
import com.odesa.musically.ui.theme.ThemeMode
import com.odesa.musically.ui.theme.isLight

@Composable
fun SongsScreen(
    songsViewModel: SongsViewModel,
    onSettingsClicked: () -> Unit
) {
    val songsScreenUiState by songsViewModel.uiState.collectAsState()

    SongsScreenContent(
        uiState = songsScreenUiState,
        onSortReverseChange = {},
        onSortTypeChange = {},
        onSettingsClicked = onSettingsClicked,
        onShufflePlay = {},
        playSong = {
            songsViewModel.playMedia(
            it.mediaItem,
            false,
            parentMediaId = MUSICALLY_TRACKS_ROOT )
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
    playSong: (Song) -> Unit,
) {
    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = { /*TODO*/ },
            title = uiState.language.songs,
            rescan = uiState.language.rescan,
            onRefreshClicked = { /*TODO*/ },
            settings = uiState.language.settings,
            onSettingsClicked = onSettingsClicked
        )
        LoaderScaffold(
            isLoading = true,
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
                playSong = playSong,
            )
        }
    }
}


@Preview( showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_MASK )
@Composable
fun SongsScreenContentPreview() {
    MusicallyTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.primaryColorName,
        fontName = SettingsDefaults.font.name,
        fontScale = 1.75f,
        useMaterialYou = SettingsDefaults.useMaterialYou
    ) {
        SongsScreenContent(
            uiState = uiState,
            onSortReverseChange = {},
            onSortTypeChange = {},
            onSettingsClicked = {},
            onShufflePlay = {},
            playSong = {}
        )
    }
}


val uiState = SongsScreenUiState(
    language = English,
//    isLoadingSongs = true,
//    sortSongsInReverse = false,
//    sortSongsBy = SortSongsBy.TITLE,
    songs = testSongs,
    themeMode = ThemeMode.LIGHT
)