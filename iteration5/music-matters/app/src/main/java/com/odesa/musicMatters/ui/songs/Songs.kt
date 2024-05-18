package com.odesa.musicMatters.ui.songs

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.R
import com.odesa.musicMatters.data.playlists.Playlist
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
    viewModel: SongsScreenViewModel,
    onViewAlbum: (String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onShareSong: ( Uri, String ) -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SongsScreenContent(
        uiState = uiState,
        onSortReverseChange = {},
        onSortTypeChange = { viewModel.setSortSongsBy( it ) },
        onSettingsClicked = onSettingsClicked,
        onShufflePlay = { viewModel.shuffleAndPlay() },
        playSong = {
            viewModel.playMedia( it.mediaItem )
        },
        onFavorite = { viewModel.addToFavorites( it ) },
        onViewAlbum = onViewAlbum,
        onViewArtist = onViewArtist,
        onPlayNext = onPlayNext,
        onAddToQueue = onAddToQueue,
        onGetSongsInPlaylist = { playlist ->
            uiState.songs.filter { playlist.songIds.contains( it.id ) }
        },
        onAddSongToPlaylist = { playlist, song ->
            viewModel.addSongToPlaylist( playlist, song )
        },
        onSearchSongsMatchingQuery = { viewModel.searchSongsMatching( it ) },
        onCreatePlaylist = { title, songs -> viewModel.createPlaylist( title, songs ) },
        onShareSong = { onShareSong( it, uiState.language.shareFailedX( "" ) ) },
        onNavigateToSearch = onNavigateToSearch,
    )
}

@Composable
fun SongsScreenContent(
    uiState: SongsScreenUiState,
    onSortReverseChange: ( Boolean ) -> Unit,
    onSortTypeChange: ( SortSongsBy ) -> Unit,
    onSettingsClicked: () -> Unit,
    onShufflePlay: () -> Unit,
    playSong: ( Song ) -> Unit,
    onFavorite: ( String ) -> Unit,
    onViewAlbum: ( String ) -> Unit,
    onViewArtist: ( String ) -> Unit,
    onShareSong: ( Uri ) -> Unit,
    onPlayNext: ( MediaItem ) -> Unit,
    onAddToQueue: ( MediaItem ) -> Unit,
    onGetSongsInPlaylist: ( Playlist ) -> List<Song>,
    onAddSongToPlaylist: ( Playlist, Song ) -> Unit,
    onSearchSongsMatchingQuery: ( String ) -> List<Song>,
    onCreatePlaylist: ( String, List<Song> ) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    val fallbackResourceId =
        if ( uiState.themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder_dark

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            onNavigationIconClicked = onNavigateToSearch,
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
                sortSongsBy = uiState.sortSongsBy,
                onSortTypeChange = onSortTypeChange,
                language = uiState.language,
                songs = uiState.songs,
                playlists = uiState.playlists,
                onShufflePlay = onShufflePlay,
                fallbackResourceId = fallbackResourceId,
                currentlyPlayingSongId = uiState.currentlyPlayingSongId,
                playSong = playSong,
                isFavorite = { uiState.favoriteSongIds.contains( it ) },
                onFavorite = onFavorite,
                onViewAlbum = onViewAlbum,
                onViewArtist = onViewArtist,
                onShareSong = onShareSong,
                onPlayNext = onPlayNext,
                onAddToQueue = onAddToQueue,
                onGetSongsInPlaylist = onGetSongsInPlaylist,
                onAddSongToPlaylist = onAddSongToPlaylist,
                onSearchSongsMatchingQuery = onSearchSongsMatchingQuery,
                onCreatePlaylist = onCreatePlaylist
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
            onPlayNext = {},
            onAddToQueue = {},
            onGetSongsInPlaylist = { emptyList() },
            onAddSongToPlaylist = { _, _ -> },
            onSearchSongsMatchingQuery = { emptyList() },
            onCreatePlaylist = { _, _ -> },
            onNavigateToSearch = {}
        )
    }
}


val uiState = SongsScreenUiState(
    language = English,
    songs = testSongs,
    themeMode = ThemeMode.LIGHT,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = testSongs.map { it.id },
    isLoading = true,
    playlists = emptyList(),
    sortSongsBy = SortSongsBy.TITLE
)