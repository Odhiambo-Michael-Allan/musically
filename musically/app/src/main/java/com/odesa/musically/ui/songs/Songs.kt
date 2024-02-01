package com.odesa.musically.ui.songs

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.odesa.musically.data.songs.impl.SortSongsBy
import com.odesa.musically.data.songs.impl.testSongs
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.ui.components.LoaderScaffold
import com.odesa.musically.ui.components.SongList
import com.odesa.musically.ui.components.TopAppBar
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun SongsScreen(
    viewModel: SongsViewModel,
    onSettingsClicked: () -> Unit
) {
    val songsScreenUiState by viewModel.uiState.collectAsState()

    SongsScreenContent(
        uiState = songsScreenUiState,
        onSortReverseChange = { viewModel.setSortSongsInReverse( it ) },
        onSortTypeChange = { viewModel.setSortSongsBy( it ) },
        onSettingsClicked = onSettingsClicked
    )
}

@Composable
fun SongsScreenContent(
    uiState: SongsScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onSettingsClicked: () -> Unit
) {

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
            isLoading = uiState.isLoadingSongs,
            loading = uiState.language.loading
        ) {
            SongList(
                sortReverse = uiState.sortSongsInReverse,
                onSortReverseChange = onSortReverseChange,
                sortType = uiState.sortSongsBy,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songs
            )
        }
    }
}

@RequiresApi( Build.VERSION_CODES.O )
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
            onSettingsClicked = {}
        )
    }
}

@RequiresApi( Build.VERSION_CODES.O )
val uiState = SongsScreenUiState(
    language = English,
    isLoadingSongs = true,
    sortSongsInReverse = false,
    sortSongsBy = SortSongsBy.TITLE,
    songs = testSongs
)